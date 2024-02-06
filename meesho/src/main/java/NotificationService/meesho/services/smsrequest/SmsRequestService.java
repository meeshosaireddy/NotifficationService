package NotificationService.meesho.services.smsrequest;

import NotificationService.meesho.dao.entities.sql.SmsRequest;

import java.util.NoSuchElementException;

public interface SmsRequestService {
    void sendSmsRequest(SmsRequest smsRequest);

    SmsRequest getSmsRequestById(int id) throws NoSuchElementException;

    SmsRequest saveSmsRequest(SmsRequest smsRequest, boolean isBlacklisted, String failure_code, String failure_comments);

    void sendSmsRequestToKafka(SmsRequest smsRequest);
}
