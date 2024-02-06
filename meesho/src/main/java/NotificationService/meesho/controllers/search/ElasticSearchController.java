package NotificationService.meesho.controllers.search;

import NotificationService.meesho.dao.entities.models.ErrorResponse;
import NotificationService.meesho.constants.ErrorConstants;
import NotificationService.meesho.constants.api.ElasticSearchAPIConstants;
import NotificationService.meesho.dao.entities.sql.SmsRequestsDocument;
import NotificationService.meesho.services.elasticsearch.ElasticsearchSmsRequestsService;
import NotificationService.meesho.services.elasticsearch.impl.ElasticsearchSmsRequestsServiceImpl;
import NotificationService.meesho.transformers.ErrorResponseTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ElasticSearchController {
    @Autowired
    private ElasticsearchSmsRequestsService elasticsearchSmsRequestsService;

    @GetMapping(ElasticSearchAPIConstants.FIND_BY_PHONE_NUMBER_AND_TIME_RANGE)
    public ResponseEntity<?> findByPhoneNumberAndTimeRange(@RequestParam String phoneNumber, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<SmsRequestsDocument> result = null;
        try {
            result = elasticsearchSmsRequestsService.getSmsMessagesByPhoneNumberAndTimeRange(phoneNumber, startTime, endTime, page, size);
            if (result.isEmpty()) {
                ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(ErrorConstants.INVALID_REQUEST, ErrorConstants.DOCUMENT_NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(ErrorConstants.INVALID_REQUEST, ErrorConstants.DOCUMENT_NOT_FOUND);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(ElasticSearchAPIConstants.SEARCH)
    public ResponseEntity<?> searchSmsMessages(@RequestParam String searchText, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "1") int size) {
        try {
            List<SmsRequestsDocument> smsMessages = elasticsearchSmsRequestsService.getSmsMessagesContainingText(searchText, page, size);
            if (smsMessages.isEmpty()) {
                ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(ErrorConstants.INVALID_REQUEST, ErrorConstants.DOCUMENT_NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.ok(smsMessages);
        } catch (IOException e) {
            ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(ErrorConstants.INVALID_REQUEST, ErrorConstants.DOCUMENT_NOT_FOUND);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
