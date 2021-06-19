package com.sg.vue.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

@SuppressWarnings({"ALL", "AlibabaThreadPoolCreation"})
@Slf4j
@Component
public class IDUtils {

    public String generateid(NumberStrategy st, boolean fullWithZero, boolean isBatch, boolean isHex) {
        if (isBatch) {
            return "{0}";
        } else {
            // db获取id
            long id = generateId(st, isHex);
            String idstr = String.valueOf(id);
            if (isHex) {
                idstr = Long.toHexString(id);
            }
            return fullWithZero ? processDigit(st.getNoLength().intValue(), idstr) : idstr;
        }

    }

    private String processDigit(int digit, String data) {
        int len = data.length();
        if (len < digit) {
            while (len < digit) {
                StringBuilder sb = new StringBuilder();
                sb.append("0").append(data);
                data = sb.toString();
                ++len;
            }
        }
        return data;
    }


    @Autowired
    CaffeineCache cache;

    @Value("${init.retry.time:300000}")
    int retryTime;

    @Autowired
    SignerFeign feign;

    Executor threadPool = new ThreadPoolExecutor(4, 8, 1, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(20));

    public static final String NOW_QUEUE_FIELD = "now";
    public static final String THRESHOLD_FIELD = "threshold";
    public static final String PENDING_QUEUE_FIELD = "pending";
    public static final String INITIAL_FIELD = "initialFlag";
    public static final String ASYNC_TIME_FIELD = "async_time";

    public Long generateId(NumberStrategy st, boolean ishex) {
        String generateType = st.getGenType();
        Long noLength = st.getNoLength();
        String stNo = st.getStNo();
        String typeString = null;
        if (noLength < 5L) {
            Map map = JSON.parseObject(JSONObject.toJSONString(st), Map.class);
            if (ishex) {
                map.put("hex", "1");
            }
            Long id = (Long) Optional.of(feign.generateId(map)).get();
            return id;
        } else {
            typeString = transformType(generateType);
            String key = "NUMBER_ID_" + stNo;
            synchronized (st) {
                Object check = cache.hget(key, typeString);
                if (check == null) {
                    cache.del(key);
                }

                Boolean initial = (Boolean) Optional.ofNullable(cache.hgetObj(key, typeString, INITIAL_FIELD)).orElse(false);
                LinkedList<Long> queue = cache.hgetObj(key, typeString, NOW_QUEUE_FIELD) == null ? new LinkedList<>() : (LinkedList) cache.hgetObj(key, typeString, NOW_QUEUE_FIELD);
                ConcurrentLinkedQueue<Map> pendingQueue = cache.hgetObj(key, typeString, PENDING_QUEUE_FIELD) == null ? new ConcurrentLinkedQueue<>() : (ConcurrentLinkedQueue) cache.hgetObj(key, typeString, PENDING_QUEUE_FIELD);
                Long rs = null;
                Long asyncTime;
                if (queue.isEmpty()) {
                    if (pendingQueue.isEmpty()) {

                        if (initial) {
                            asyncTime = (Long) Optional.ofNullable(cache.hgetObj(key, typeString, ASYNC_TIME_FIELD)).orElse(0L);
                            Map map = JSON.parseObject(JSONObject.toJSONString(st), Map.class);
                            if (ishex) {
                                map.put("hex", "1");
                            }
                            rs = (Long) Optional.of(feign.generateId(map)).get();
                            log.info("等待队列为空，已初始化，直接降级为从发号器获取，知道服务恢复,ids={}", rs);
                            long nowTime = System.currentTimeMillis();
                            if (asyncTime != 0L && nowTime - asyncTime > retryTime) {
                                //TODO doRetry
                                String finalTypeString1 = typeString;
                                threadPool.execute(() -> {
                                    doRetry(key, finalTypeString1, pendingQueue, st, ishex);
                                });
                            }
                            return rs;
                        }

                        log.info("等待队列为空，未初始化，从缓存队列中获取。策略编号：{}", stNo);
                        try {
                            if (pendingQueue.isEmpty()) {
                                pendingQueue.offer(null);
                                cache.hset(key, typeString, PENDING_QUEUE_FIELD, pendingQueue);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("从算号器服务获取ID失败", e);
                        }

                        initial = true;
                        cache.hset(key, typeString, INITIAL_FIELD, initial);

                    }

                    Map dataMap = pendingQueue.poll();
                    log.info("取出第二缓存号段：{}，策略编号：{}", dataMap, stNo);
                    queue = convertMapToQueue(dataMap);
                    cache.hset(key, typeString, NOW_QUEUE_FIELD, queue);
                    cache.hset(key, typeString, THRESHOLD_FIELD, calcThresHold(queue));
                }

                rs = queue.poll();
                asyncTime = (Long) cache.hgetObj(key, typeString, THRESHOLD_FIELD);
                if (rs.equals(asyncTime)) {
                    log.info("触发阈值，异步缓存第二段队列：策略编号：{}，阈值：{}", stNo, asyncTime);
                    String finalTypeString = typeString;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                pendingQueue.offer(getPendingMapWithFeign(st, ishex));
                                log.info("异步缓存结果，pendingQueue：{},策略编号：{}", pendingQueue, stNo);
                            } catch (Exception e) {
                                log.info("异步缓存出错，纪录时间,策略编号：{}", stNo);
                                cache.hset(key, finalTypeString, ASYNC_TIME_FIELD, System.currentTimeMillis());
                            }

                        }
                    }).start();
                }

                Long asyncTime1 = (Long) Optional.ofNullable(cache.hgetObj(key, typeString, "asyncTime")).orElse(0L);
                long nowTime = System.currentTimeMillis();
                if (asyncTime != 0L && nowTime - asyncTime > retryTime) {
                    //TODO doRetry
                    String finalTypeString1 = typeString;
                    threadPool.execute(() -> {
                        doRetry(key, finalTypeString1, pendingQueue, st, ishex);
                    });
                }
                return rs;
            }
        }

    }


    private void doRetry(String key, String typeString, ConcurrentLinkedQueue<Map> pendingQueue, NumberStrategy st, boolean ishex) {
        log.info("达到重试时间，重新从服务拉取数据");
        try {
            pendingQueue.offer(getPendingMapWithFeign(st, ishex));
            log.info("异步缓存结果，pendingQueue：{}", pendingQueue);
            cache.hset(key, typeString, ASYNC_TIME_FIELD, 0);
        } catch (Exception e) {
            log.info("异步缓存出错，更新时间,策略编号：{}", st.getStNo());
            cache.hset(key, typeString, ASYNC_TIME_FIELD, System.currentTimeMillis());
        }

    }

    private LinkedList<Long> convertMapToQueue(Map dataMap) {
        Long currentId = Long.valueOf((String) dataMap.get("currentId"));
        Long maxId = Long.valueOf((String) dataMap.get("maxId"));
        Long stepInRs = Long.valueOf((String) dataMap.get("step"));
        LinkedList<Long> rsList = new LinkedList<>();

        for (Long i = currentId; i <= maxId; i = i + stepInRs) {
            rsList.offer(i);
        }
        return rsList;
    }

    private Long calcThresHold(LinkedList<Long> queue) {
        int thresHoldIndex = queue.size() / 5 - 1;
        return queue.get(thresHoldIndex);
    }

    public String transformType(String generateType) {
        byte var3 = -1;
        switch (generateType.hashCode()) {
            case 48:
                if ("0".equals(generateType)) {
                    var3 = 0;
                }
                break;
            case 49:
                if ("1".equals(generateType)) {
                    var3 = 1;
                }
                break;
            case 50:
                if ("2".equals(generateType)) {
                    var3 = 2;
                }
                break;
        }

        switch (var3) {
            case 0:
                return LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
            case 1:
                return LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
            case 2:
                return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
            default:
                return "infinite";
        }

    }

    /**
     * db获取主键值
     *
     * @return
     */
    private Map getPendingMapWithFeign(NumberStrategy st, boolean isHex) {
        Map param = JSON.parseObject(JSONObject.toJSONString(st), Map.class);
        if (isHex) {
            param.put("hex", "1");
        }
        String rsdata = (String) Optional.of(feign.batchGenerateId(param)).get();
        Map rsMap = JSON.parseObject(rsdata, Map.class);
        return rsMap;
    }


}
