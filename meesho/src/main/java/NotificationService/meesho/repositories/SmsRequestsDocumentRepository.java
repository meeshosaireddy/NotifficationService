package NotificationService.meesho.repositories;

import NotificationService.meesho.models.SmsRequests;

import NotificationService.meesho.models.SmsRequestsDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;

public interface SmsRequestsDocumentRepository extends ElasticsearchRepository<SmsRequestsDocument, Integer> {
    // Additional custom query methods can be added here
    @Query("{\"bool\": {\"must\": [{\"match\": {\"phone_number.keyword\": \"?0\"}}, {\"range\": {\"created_at\": {\"gte\": \"?1\", \"lte\": \"?2\"}}}]}}")
    Page<SmsRequestsDocument> findByPhoneNumberAndTimestampBetween(
            String phoneNumber, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
