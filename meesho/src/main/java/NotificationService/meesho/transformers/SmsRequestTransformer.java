package NotificationService.meesho.transformers;

import NotificationService.meesho.dao.entities.models.UserMessage;
import NotificationService.meesho.dao.entities.sql.SmsRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SmsRequestTransformer {
    public SmsRequest getSmsRequest(UserMessage userMessage, String status) {
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setPhoneNumber(userMessage.getPhoneNumber());
        smsRequest.setMessage(userMessage.getMessage());
        smsRequest.setStatus(status); // Assuming an initial status
        smsRequest.setCreatedAt(LocalDateTime.now()); // Set created_at to the current date and time
        smsRequest.setUpdatedAt(LocalDateTime.now());
        return smsRequest;
    }
}
