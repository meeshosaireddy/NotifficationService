package NotificationService.meesho.services.elasticsearch;

import NotificationService.meesho.dao.entities.sql.SmsRequest;
import NotificationService.meesho.dao.entities.sql.SmsRequestsDocument;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ElasticsearchSmsRequestsService {

    void indexSmsRequest(SmsRequest smsRequest);

    List<SmsRequestsDocument> getSmsMessagesByPhoneNumberAndTimeRange(
            String phoneNumber, LocalDateTime startTime, LocalDateTime endTime, int page, int size) throws IOException;

    List<SmsRequestsDocument> getSmsMessagesContainingText(String searchText, int page, int size) throws IOException;
}

