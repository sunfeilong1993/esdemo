package com.sun.xiaotian.esdemo.usecase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * Description   :   删除数据<br/>
 * Project Name  :   esdemo<br/>
 * Author        :   FieLong Sun<br/>
 * Date          :   2018-01-07  12:39<br/>
 */

public class DeleteAPI {

    private final static Logger logger = LogManager.getLogger(DeleteAPI.class);

    public static void main(String[] args) {

        UpdateRequest updateRequest = new UpdateRequest("test", "test", "1");
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", "xiaotian");
        data.put("age", 24);
        data.put("birthday", new Date());
        updateRequest
                .upsert()
                .doc(data);


        DeleteRequest deleteRequest = new DeleteRequest("test", "test", "1");
        deleteRequest
                .timeout(TimeValue.timeValueSeconds(1))
                .setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);

        try (RestHighLevelClient client = new RestHighLevelClient(RestClientBuilderFactory.getBClientBuilder())) {
            UpdateResponse updateResponse = client.update(updateRequest);

            deleteRequest.version(updateResponse.getVersion());
            DeleteResponse response = client.delete(deleteRequest);
            System.out.println(response.status());
        } catch (IOException | RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
