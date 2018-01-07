package com.sun.xiaotian.esdemo.usecase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Description   :   查询API <br/>
 * Project Name  :   esdemo<br/>
 * Author        :   FieLong Sun<br/>
 * Date          :   2018-01-07  22:02<br/>
 */

public class SearchAPI {

    private final static Logger logger = LogManager.getLogger(SearchAPI.class);

    public static void main(String[] args) {
        testSearchAll();
        testMatchQueryBuilder();
        testSearchSourceBuilder();
    }

    private static void testSearchAll() {
        try (RestHighLevelClient client = new RestHighLevelClient(RestClientBuilderFactory.getBClientBuilder())) {
            SearchRequest searchRequest = new SearchRequest("test");
            searchRequest.types("test");
            searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
            //优先搜索那些分片
            searchRequest.preference("_local");

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.timeout(new TimeValue(3, TimeUnit.SECONDS));

            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            logger.info("searchResponse: " + searchResponse);
        } catch (IOException | ElasticsearchException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void testMatchQueryBuilder() {
        try (RestHighLevelClient client = new RestHighLevelClient(RestClientBuilderFactory.getBClientBuilder())) {
            SearchRequest searchRequest = new SearchRequest("test");
            searchRequest.types("test");
            //优先搜索那些分片
            searchRequest.preference("_local");

            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name", "name 1");
            matchQueryBuilder.operator(Operator.OR);
            matchQueryBuilder.minimumShouldMatch("50%");
            matchQueryBuilder.lenient(true);
            //指定查询数据的分析器,默认和字段使用的相同
            //matchQueryBuilder.analyzer("");
            matchQueryBuilder.fuzziness(Fuzziness.AUTO);
            matchQueryBuilder.prefixLength(3);
            matchQueryBuilder.maxExpansions(10);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //结果排序
            searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
            searchSourceBuilder.query(matchQueryBuilder);
            //只显示文档部分字段信息
            //searchSourceBuilder.fetchSource(false);
            String[] includeFields = new String[] {"name", "date"};
            String[] excludeFields = new String[] {};
            searchSourceBuilder.fetchSource(includeFields, excludeFields);

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest);
            logger.info("MatchQueryBuilder: " + searchResponse);
        } catch (IOException | ElasticsearchException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void testSearchSourceBuilder() {
        try (RestHighLevelClient client = new RestHighLevelClient(RestClientBuilderFactory.getBClientBuilder())) {
            SearchRequest searchRequest = new SearchRequest("test");
            searchRequest.types("test");
            searchRequest.indicesOptions(IndicesOptions.strictExpand());
            //优先搜索那些分片
            searchRequest.preference("_local");

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("name", "name 1"));
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(5);
            searchSourceBuilder.timeout(new TimeValue(3, TimeUnit.SECONDS));
            //排序
            searchSourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.DESC));

            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            logger.info("SearchSourceBuilder: " + searchResponse);
        } catch (IOException | ElasticsearchException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
