package NotificationService.meesho.validators.sendsms;

import NotificationService.meesho.dao.entities.models.UserMessage;
import org.springframework.stereotype.Component;

@Component
public class SendSmsValidator {

    public boolean validateUserMessage(UserMessage userMessage){
        return userMessage.getPhoneNumber() != null && userMessage.getMessage() != null;
    }
}
