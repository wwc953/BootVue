package com.sg.vue.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sg.vue.bean.EsObj;
import com.sg.vue.bean.MyUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/es")
public class ESController {

    @Autowired
    RestHighLevelClient restClient;

    @GetMapping("/query")
    public void query() throws IOException {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //分析查询和聚合
        sourceBuilder.profile(true);

        String ES_INDEX = "wwctest";
        //设置ES索引和类型
        SearchRequest request = new SearchRequest(ES_INDEX);
        request.source(sourceBuilder);

        SearchResponse response = restClient.search(request, RequestOptions.DEFAULT);
        System.out.println("response:" + JSONObject.toJSONString(response));

        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            System.out.println(hit.getSourceAsString());
        }

    }


    @GetMapping("/queryuser")
    @ResponseBody
    public List<MyUser> querymyuser() throws IOException {
        String ES_INDEX = "user";
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询所有
//        sourceBuilder.query(QueryBuilders.matchAllQuery());
        // termQuery 精确查询
//        sourceBuilder.query(QueryBuilders.termQuery("id", "258"));

//        // BoolQuery搜索方式
//        // 首先构造多关键字查询条件
//        MultiMatchQueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery("5", "id", "name");
//        // 然后构造精确匹配查询条件
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("id", "258");
//        // 组合两个条件，组合方式为 must 全满足
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        // 通过布尔查询来构造过滤查询
//        boolQueryBuilder.filter(QueryBuilders.termQuery("flag", "02"));
//        boolQueryBuilder.must(matchQueryBuilder);
//        boolQueryBuilder.must(termQueryBuilder);
//        sourceBuilder.query(boolQueryBuilder);

        WildcardQueryBuilder wildcardQueryName = QueryBuilders.wildcardQuery("name", "*才*");
//        WildcardQueryBuilder wildcardQueryid = QueryBuilders.wildcardQuery("address", "*维*");

        //在使用matchQuery等时，在执行查询时，搜索的词会被分词器分词，而使用matchPhraseQuery时，不会被分词器分词，而是直接以一个短语的形式查询
        MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("address", "职倒");
//        MatchQueryBuilder matchPhraseQuery = QueryBuilders.matchQuery("address", "职倒");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 相当于and
//        boolQueryBuilder.must(wildcardQueryName);
//        boolQueryBuilder.must(wildcardQueryid);
//        boolQueryBuilder.must(matchPhraseQuery);

        // 相当于or
        boolQueryBuilder.should(wildcardQueryName);
//        boolQueryBuilder.should(wildcardQueryid);
        boolQueryBuilder.should(matchPhraseQuery);

//        boolQueryBuilder.filter(QueryBuilders.termQuery("flag", "02"));

        sourceBuilder.query(boolQueryBuilder);

        sourceBuilder.from(0);
        sourceBuilder.size(10);
        //分析查询和聚合 ,,, 作用？？？
//        sourceBuilder.profile(true);

        //设置ES索引和类型
        SearchRequest request = new SearchRequest(ES_INDEX);
        request.source(sourceBuilder);

        System.out.println(request);

        SearchResponse response = restClient.search(request, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        // 匹配到的总记录数
        TotalHits totalHits = hits.getTotalHits();

        SearchHit[] searchHits = hits.getHits();

        List<MyUser> arrayList = new ArrayList<MyUser>();
        for (SearchHit hit : searchHits) {
            MyUser user = JSON.parseObject(hit.getSourceAsString(), MyUser.class);
            System.out.println(JSONObject.toJSONString(user));
            arrayList.add(user);
        }
        return arrayList;
    }

    @GetMapping("/queryuser2")
    @ResponseBody
    public List<MyUser> querymyuser2() throws IOException {
        String ES_INDEX = "user";
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder mastQuery = QueryBuilders.boolQuery();

        WildcardQueryBuilder wildcardQueryName = QueryBuilders.wildcardQuery("name", "*才*");
        MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("age", "690");
        BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
        subQuery.should(wildcardQueryName);
        subQuery.should(matchPhraseQuery);
        mastQuery.must(subQuery);

        mastQuery.filter(QueryBuilders.termsQuery("flag", "02"));

        sourceBuilder.query(mastQuery);

        sourceBuilder.from(0);
        sourceBuilder.size(10);

        System.out.println(sourceBuilder);

        //设置ES索引和类型
        SearchRequest request = new SearchRequest(ES_INDEX);
        request.source(sourceBuilder);

        SearchResponse response = restClient.search(request, RequestOptions.DEFAULT);

        System.out.println(response);

        SearchHits hits = response.getHits();
        System.out.println("total:" + hits.getTotalHits().value);

        SearchHit[] searchHits = hits.getHits();
        List<MyUser> arrayList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            MyUser user = JSON.parseObject(hit.getSourceAsString(), MyUser.class);
            System.out.println(JSONObject.toJSONString(user));
            arrayList.add(user);
        }

        return arrayList;
    }

    @GetMapping("/queryesobj")
    @ResponseBody
    public List<EsObj> queryesobj() throws IOException {
        String ES_INDEX = "esobj";
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder mastQuery = QueryBuilders.boolQuery();

        WildcardQueryBuilder wildcardQueryName = QueryBuilders.wildcardQuery("name", "*0*");
        MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("numbers", "162106951307420");
        BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
        subQuery.should(wildcardQueryName);
        subQuery.should(matchPhraseQuery);
        mastQuery.must(subQuery);

//        mastQuery.filter(QueryBuilders.termsQuery("flag", "10"));
//        mastQuery.filter(QueryBuilders.termsQuery("numbers", "1621069513074201"));

        sourceBuilder.query(mastQuery);
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        log.info("source:{}", sourceBuilder);

        //设置ES索引和类型
        SearchRequest request = new SearchRequest(ES_INDEX);
        request.source(sourceBuilder);
        log.info("SearchRequest:{}", request);

        SearchResponse response = restClient.search(request, RequestOptions.DEFAULT);
        log.info("SearchResponse:{}", response);


        SearchHits hits = response.getHits();
        log.info("total:{}", hits.getTotalHits().value);

        SearchHit[] searchHits = hits.getHits();
        List<EsObj> arrayList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            EsObj user = JSON.parseObject(hit.getSourceAsString(), EsObj.class);
            System.out.println(JSONObject.toJSONString(user));
            arrayList.add(user);
        }

        return arrayList;
    }

    @GetMapping("/createIndex")
    public Boolean createIndex() throws IOException {
        //创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("wwctest");

        //设置分片和副本数
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
        );

        //索引映射
        request.mapping("{\n" +
                "  \"properties\": {\n" +
                "    \"name\": {\n" +
                "      \"type\": \"keyword\"\n" +
                "    },\n" +
                "    \"age\": {\n" +
                "      \"type\": \"long\"\n" +
                "    }\n" +
                "  }\n" +
                "}", XContentType.JSON);

        CreateIndexResponse createIndexResponse = restClient.indices().create(request, RequestOptions.DEFAULT);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println("创建索引结果：" + acknowledged);
        return acknowledged;
    }

    @GetMapping("/del/{index}")
    public Boolean del(@PathVariable(value = "index") String index) throws IOException {

        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse response = restClient.indices().delete(request, RequestOptions.DEFAULT);

        return response.isAcknowledged();
    }

    @GetMapping("/add")
    public String add() throws IOException {
        String indexName = "wwctest";
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("age", System.currentTimeMillis());
        jsonMap.put("name", System.currentTimeMillis() + "wang王");
        IndexRequest indexRequest = new IndexRequest(indexName).source(jsonMap);

//        // 手动指定路由的key，文档查询时可提高性能
//        indexRequest.routing("userInfo");
//        // 刷新策略，WAIT_UNTIL设置则表示刷新使此请求的内容对搜索可见为止
//        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);

        IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);
        return JSONObject.toJSONString(indexResponse);
    }

    @GetMapping("/addmysql")
    public String addmysql() throws IOException {
        String indexName = "peopledemo";
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("age", 10);
        jsonMap.put("name", "wang王");
        jsonMap.put("address", "address");
        jsonMap.put("update_time", new Date());
        jsonMap.put("create_time", new Date());
        IndexRequest indexRequest = new IndexRequest(indexName).source(jsonMap);

//        // 手动指定路由的key，文档查询时可提高性能
//        indexRequest.routing("userInfo");
//        // 刷新策略，WAIT_UNTIL设置则表示刷新使此请求的内容对搜索可见为止
//        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);

        IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);
        return JSONObject.toJSONString(indexResponse);
    }

    @GetMapping("/createEsObj/{size}")
    public void createEsObj(@PathVariable(value = "size") Integer size) {
        if (size == null || size < 0) {
            size = 10;
        }
        String indesName = "esobj";
        for (int i = 0; i < size; i++) {
            EsObj esObj = new EsObj();
            esObj.setName(i + "name");
            esObj.setNumbers(Arrays.asList(System.currentTimeMillis() + "" + i, System.currentTimeMillis() + "" + i * 2));
            esObj.setAddrs(Arrays.asList("南京" + i, "北京" + i, "上海" + i));
            esObj.setFlag(i + "");
            List<MyUser> objects = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                MyUser user = new MyUser();
                user.setFlag("01");
                user.setName(i + "subname");
                user.setUpdateTime(new Date());
                user.setCreateTime(new Date());
                user.setAge(j);
                user.setId(j);
                objects.add(user);
            }
            esObj.setUserlist(objects);
            addEsObj(indesName, esObj);
        }

    }

    public void addEsObj(String indexName, EsObj obj) {
        if (indexName == null || indexName.length() == 0) {
            throw new RuntimeException("indexName is null");
        }
        IndexRequest indexRequest = new IndexRequest(indexName).source(JSONObject.toJSONString(obj), XContentType.JSON);
        try {
            restClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
