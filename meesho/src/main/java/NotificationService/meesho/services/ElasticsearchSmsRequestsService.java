package NotificationService.meesho.services;

import NotificationService.meesho.models.SmsRequests;
import NotificationService.meesho.models.SmsRequestsDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.entity.ContentType;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.xcontent.XContentType;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import com.fasterxml.jackson.datatype.jsr310.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticsearchSmsRequestsService {

    @Autowired
    @Qualifier("elastic-search-client")
    private RestHighLevelClient elasticsearchClient;



    public void indexSmsRequest(SmsRequests smsRequest) {
        SmsRequestsDocument document = convertToDocument(smsRequest);

        // Configure ObjectMapper to handle Java 8 date/time types
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Serialize dates as ISO-8601 strings

        IndexRequest indexRequest = new IndexRequest("notification-service", "_doc", String.valueOf(document.getId()));

        try {
            // Convert document to JSON string using the configured ObjectMapper
            String jsonDocument = objectMapper.writeValueAsString(document);
            System.out.println(jsonDocument);

            // Set the JSON string as the source of the index request with content type XContentType.JSON
            indexRequest.source(jsonDocument, XContentType.JSON);

            // Index the document
            elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    private String convertToJsonString(SmsRequestsDocument document) {
        // Convert the document to JSON representation using your preferred method
        // For example, you can use Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(document);
        } catch (JsonProcessingException e) {
            System.out.println("Error converting document to JSON: " + e.getMessage());
            return "{}"; // Provide a default value or handle the error appropriately
        }
    }

    public List<SmsRequestsDocument> getSmsMessagesByPhoneNumberAndTimeRange(
            String phoneNumber, LocalDateTime startTime, LocalDateTime endTime, int page, int size) throws IOException {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("phone_number.keyword", phoneNumber))
                        .must(QueryBuilders.rangeQuery("created_at")
                                .gte(startTime)
                                .lte(endTime)));

        SearchRequest searchRequest = new SearchRequest("notification-service")
                .source(sourceBuilder);

        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

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


    private SmsRequestsDocument convertToDocument(SmsRequests smsRequest) {
        SmsRequestsDocument document = new SmsRequestsDocument();
        document.setId(smsRequest.getId());
        document.setPhone_number(smsRequest.getPhone_number());
        document.setMessage(smsRequest.getMessage());
        document.setStatus(smsRequest.getStatus());
        document.setFailure_code(smsRequest.getFailure_code());
        document.setFailure_comments(smsRequest.getFailure_comments());
        document.setCreated_at(smsRequest.getCreated_at());
        document.setUpdated_at(smsRequest.getUpdated_at());
        return document;
    }
    public List<SmsRequestsDocument> getSmsMessagesContainingText(
            String searchText, int page, int size) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchQuery("message", searchText))
                .sort(SortBuilders.fieldSort("created_at").order(SortOrder.DESC))
                .from(page * size)
                .size(size);

        SearchRequest searchRequest = new SearchRequest("notification-service")
                .source(sourceBuilder);

        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

        return Arrays.stream(searchResponse.getHits().getHits())
                .map(hit -> {
                    try {
                        return objectMapper.readValue(hit.getSourceAsString(), SmsRequestsDocument.class);
                    } catch (IOException e) {
                        throw new RuntimeException("Error converting JSON to object", e);
                    }
                })
                .collect(Collectors.toList());
    }
}


