package NotificationService.meesho.transformers;

import NotificationService.meesho.constants.KafkaConstants;
import NotificationService.meesho.controllers.redis.RedisController;
import NotificationService.meesho.dao.entities.models.SmsMessageToPhoneNumber;
import NotificationService.meesho.dao.entities.sql.SmsRequest;
import NotificationService.meesho.transformers.SmsRequestsToMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SmsTransformer {
    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);
    public static void sendSms(SmsRequest smsRequest) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String API_URL = KafkaConstants.API_URL;
            String HEADERS = KafkaConstants.HEADERS;
            SmsMessageToPhoneNumber smsMessageToPhoneNumber= SmsRequestsToMessageTransformer.convertSmsRequestToMessage(smsRequest);
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, smsMessageToPhoneNumber, String.class, HEADERS);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Successfully Sent");
            } else {
                logger.error("Failed to send SMS");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
