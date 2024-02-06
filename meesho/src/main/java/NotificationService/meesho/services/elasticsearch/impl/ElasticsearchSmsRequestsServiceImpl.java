package NotificationService.meesho.services.elasticsearch.impl;

import NotificationService.meesho.controllers.blacklist.BlacklistPhoneNumberController;
import NotificationService.meesho.dao.entities.sql.SmsRequest;
import NotificationService.meesho.dao.entities.sql.SmsRequestsDocument;
import NotificationService.meesho.services.elasticsearch.ElasticsearchSmsRequestsService;
import NotificationService.meesho.services.elasticsearch.helpers.ElasticSearchSmsRequestHelper;
import NotificationService.meesho.services.elasticsearch.mappers.SmsRequestDocumentMapper;
//import org.elasticsearch.index.mapper.ObjectMapper;
//import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ElasticsearchSmsRequestsServiceImpl implements ElasticsearchSmsRequestsService {

    @Autowired
    ElasticSearchSmsRequestHelper elasticSearchSmsRequestHelper;
    private static final Logger logger = LoggerFactory.getLogger(BlacklistPhoneNumberController.class);
    @Override
    public void indexSmsRequest(SmsRequest smsRequest) {
        SmsRequestsDocument document = SmsRequestDocumentMapper.mapper.convertToDocument(smsRequest);
        if (document != null) {
            elasticSearchSmsRequestHelper.indexDocument(document);
        } else {
            logger.error("Failed to convert SMS request to document.");
        }
    }

    @Override
    public List<SmsRequestsDocument> getSmsMessagesByPhoneNumberAndTimeRange(
            String phoneNumber, LocalDateTime startTime, LocalDateTime endTime, int page, int size) throws IOException {

        return elasticSearchSmsRequestHelper.getSmsMessagesByPhoneNumberAndTimeRange(phoneNumber, startTime, endTime, page, size);

    }

    @Override
    public List<SmsRequestsDocument> getSmsMessagesContainingText(
            String searchText, int page, int size) throws IOException {

        return elasticSearchSmsRequestHelper.getSmsMessagesContainingText(searchText, page, size);

    }
}


