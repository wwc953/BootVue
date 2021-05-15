package com.sg.vue.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
    @Value("${cloud.elasticsearch.host:127.0.0.1}")
    private String esHost;

    @Value("${cloud.elasticsearch.port:9200}")
    private int esPort;

    @Bean
    public RestHighLevelClient restClient() {
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(new HttpHost(esHost, esPort)));
        return restClient;
    }
}
