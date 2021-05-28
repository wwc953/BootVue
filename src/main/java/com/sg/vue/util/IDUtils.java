package com.sg.vue.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class IDUtils {

    public String generateid(NumberStrategy st, boolean fullWithZero, boolean isBatch, boolean isHex) {
        if (isBatch) {
            return "{0}";
        } else {
            // db获取id
            long id = generateId(st, isHex);
            String idstr = String.valueOf(id);
            if (isHex) idstr = Long.toHexString(id);
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

    public Long generateId(NumberStrategy st, boolean ishex) {
        Long initLong = 0L;

        String generateType = st.getGenType();
        Long noLength = st.getNoLength();
        String stNo = st.getStNo();
        String typeString = null;
        if (noLength < 5L) {
            return initLong;
        } else {
            typeString = transformType(generateType);
            String key = "NUMBER_ID_" + stNo;
            synchronized (st) {
                Object check = cache.hget(key, typeString);
                if (check == null) cache.del(key);

                Boolean initial = (Boolean) Optional.ofNullable(cache.hgetObj(key, typeString, "initialFlag")).orElse(false);
                LinkedList<Long> queue = cache.hgetObj(key, typeString, "now") == null ? new LinkedList<>() : (LinkedList) cache.hgetObj(key, typeString, "now");
                ConcurrentLinkedQueue<Map> pendingQueue = cache.hgetObj(key, typeString, "pending") == null ? new ConcurrentLinkedQueue<>() : (ConcurrentLinkedQueue) cache.hgetObj(key, typeString, "pending");
                Long rs = null;
                Long asyncTime;
                if (queue.isEmpty()) {
                    if (pendingQueue.isEmpty()) {

                        log.info("等待队列为空，未初始化，从缓存队列中获取。策略编号：{}", stNo);
                        try {
                            if (pendingQueue.isEmpty()) {
                                pendingQueue.offer(null);
                                cache.hset(key, typeString, "pending", pendingQueue);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("从算号器服务获取ID失败", e);
                        }


                    }
                }
            }
        }

        return initLong;
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

}
