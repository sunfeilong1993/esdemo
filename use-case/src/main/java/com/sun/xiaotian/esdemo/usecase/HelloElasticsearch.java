package com.sun.xiaotian.esdemo.usecase;

import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Description   :   First Elasticsearch Class<br/>
 * Project Name  :   esdemo<br/>
 * Author        :   FieLong Sun<br/>
 * Date          :   2018-01-06  23:47<br/>
 */

public class HelloElasticsearch {

    private final static Logger logger = LoggerFactory.getLogger(HelloElasticsearch.class);

    public static void main(String[] args) throws IOException {

        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("localhost", 9200, "http"));

        try (RestHighLevelClient client = new RestHighLevelClient(restClientBuilder);) {
            boolean ping = client.ping(new BasicHeader("name", "value"));
            logger.info("ping 通了吗? " + (ping ? "通了" : "没有"));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
