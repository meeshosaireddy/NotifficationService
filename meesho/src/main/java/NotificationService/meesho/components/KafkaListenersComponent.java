package NotificationService.meesho.components;

import NotificationService.meesho.models.SmsRequests;
import NotificationService.meesho.services.ElasticsearchSmsRequestsService;
import NotificationService.meesho.services.RedisService;
import NotificationService.meesho.services.SmsRequestService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenersComponent {
    private final SmsRequestService smsRequestService;
    private final RedisService redisService;
    private final  ElasticsearchSmsRequestsService elasticsearchService;

    public KafkaListenersComponent(SmsRequestService smsRequestService, RedisService redisService, ElasticsearchSmsRequestsService elasticsearchService) {
        this.smsRequestService = smsRequestService;

        this.redisService = redisService;
        this.elasticsearchService = elasticsearchService;
    }

    @KafkaListener(topics = "notification.send_sms",groupId = "send_sms")
    public boolean checkForBlacklisted(int requestId) {
        // Retrieve SMS request by ID from the service
        SmsRequests smsRequests = smsRequestService.getSmsRequestById(requestId);

        // Log the phone number for debugging purposes
        System.out.println("Phone number: " + smsRequests.getPhone_number());

        // Check if the phone number is blacklisted using the Redis service
        if (redisService.checkBlacklistNumber(smsRequests.getPhone_number())) {
            // Phone number is blacklisted

            // Update the database (You may need to implement this method in your service)
            smsRequestService.updateStatus(requestId, true);
            smsRequests.setStatus("Blacklisted");
            return true;
        }
       // elasticsearchService.indexSmsRequest(smsRequests);
        // Return false if the number is not blacklisted
        return false;
    }


}
