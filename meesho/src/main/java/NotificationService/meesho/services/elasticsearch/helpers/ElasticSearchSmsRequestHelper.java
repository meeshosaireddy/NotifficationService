package NotificationService.meesho.services.elasticsearch.helpers;

import NotificationService.meesho.constants.ElasticSearchSmsRequestsConstants;
import NotificationService.meesho.controllers.redis.RedisController;
import NotificationService.meesho.dao.entities.sql.SmsRequestsDocument;
import NotificationService.meesho.services.elasticsearch.transformers.ElasticSearchSmsRequestTransformer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ElasticSearchSmsRequestHelper {

    @Autowired
    @Qualifier(ElasticSearchSmsRequestsConstants.ELASTIC_SEARCH_CLIENT)
    private RestHighLevelClient elasticsearchClient;
    @Autowired
    private ElasticSearchSmsRequestTransformer elasticSearchSmsRequestTransformer;
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchSmsRequestHelper.class);

    public void indexDocument(SmsRequestsDocument document) {
        IndexRequest indexRequest = new IndexRequest(ElasticSearchSmsRequestsConstants.INDEX, ElasticSearchSmsRequestsConstants.TYPE, String.valueOf(document.getId()));
        try {
            String jsonDocument = elasticSearchSmsRequestTransformer.convertDocumentToJson(document);
            indexRequest.source(jsonDocument, XContentType.JSON);
            elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public List<SmsRequestsDocument> getSmsMessagesByPhoneNumberAndTimeRange(
            String phoneNumber, LocalDateTime startTime, LocalDateTime endTime, int page, int size) {
        try {
            int from = page * size;
            SearchSourceBuilder sourceBuilder = buildSmsMessagesQuery(phoneNumber, startTime, endTime);
            sourceBuilder.from(from);
            sourceBuilder.size(size);
            SearchResponse searchResponse = executeSmsMessagesSearch(sourceBuilder);

            return processSmsMessagesSearchResponse(searchResponse);
        } catch (IOException e) {
            logger.error("Error retrieving SMS messages by phone number and time range: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private SearchSourceBuilder buildSmsMessagesQuery(String phoneNumber, LocalDateTime startTime, LocalDateTime endTime) {
        return new SearchSourceBuilder()
                .query(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("phoneNumber.keyword", phoneNumber))
                        .must(QueryBuilders.rangeQuery(ElasticSearchSmsRequestsConstants.CREATED_AT)
                                .gte(startTime)
                                .lte(endTime)));

    }

    private SearchResponse executeSmsMessagesSearch(SearchSourceBuilder sourceBuilder) throws IOException {
        SearchRequest searchRequest = new SearchRequest(ElasticSearchSmsRequestsConstants.INDEX)
                .source(sourceBuilder);
        return elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    private List<SmsRequestsDocument> processSmsMessagesSearchResponse(SearchResponse searchResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<SmsRequestsDocument> smsRequestsDocuments = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            String sourceAsString = hit.getSourceAsString();
            SmsRequestsDocument document = objectMapper.readValue(sourceAsString, SmsRequestsDocument.class);
            smsRequestsDocuments.add(document);
        }
        return smsRequestsDocuments;
    }

    public List<SmsRequestsDocument> getSmsMessagesContainingText(
            String searchText, int page, int size) {
        try {
            SearchSourceBuilder sourceBuilder = buildTextSearchQuery(searchText, page, size);
            SearchResponse searchResponse = executeTextSearch(sourceBuilder);
            return processTextSearchResponse(searchResponse);
        } catch (IOException e) {
            logger.error("Error retrieving SMS messages containing text: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private SearchSourceBuilder buildTextSearchQuery(String searchText, int page, int size) {
        return new SearchSourceBuilder()
                .query(QueryBuilders.matchQuery("message", searchText))
                .sort(SortBuilders.fieldSort(ElasticSearchSmsRequestsConstants.CREATED_AT).order(SortOrder.DESC))
                .from(page * size)
                .size(size);
    }

    private SearchResponse executeTextSearch(SearchSourceBuilder sourceBuilder) throws IOException {
        SearchRequest searchRequest = new SearchRequest(ElasticSearchSmsRequestsConstants.INDEX)
                .source(sourceBuilder);
        return elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    private List<SmsRequestsDocument> processTextSearchResponse(SearchResponse searchResponse) {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

        List<SmsRequestsDocument> documents = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            try {
                SmsRequestsDocument document = objectMapper.readValue(hit.getSourceAsString(), SmsRequestsDocument.class);
                documents.add(document);
            } catch (IOException e) {
                logger.error("Error converting JSON to object: " + e.getMessage());
                logger.error("Failed JSON data: " + hit.getSourceAsString());
            }
        }
        return documents;
    }


}
