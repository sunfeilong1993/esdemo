package com.sun.xiaotian.esdemo.usecase;

import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;


/**
 * Description   :   索引API使用<br/>
 * Project Name  :   esdemo<br/>
 * Author        :   FieLong Sun<br/>
 * Date          :   2018-01-07  01:00<br/>
 */
public class IndexAPI {

    private final static Logger logger = LogManager.getLogger(IndexAPI.class);


    public static void main(String[] args) {

        IndexRequest request = new IndexRequest("human", "person", "2");
        request.source(
                "name", "xiaotian",
                "age", "24",
                "sex", "man"
        );

        try (RestHighLevelClient client = new RestHighLevelClient(RestClientBuilderFactory.getBClientBuilder())) {
            IndexResponse index = client.index(request);
            System.out.println(index.status());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
