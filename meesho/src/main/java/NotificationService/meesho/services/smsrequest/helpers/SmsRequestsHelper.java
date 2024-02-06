package NotificationService.meesho.services.smsrequest.helpers;

import NotificationService.meesho.dao.entities.sql.SmsRequest;
import NotificationService.meesho.repositories.SmsRequestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@RequiredArgsConstructor(onConstructor_ = @Autowired )
@Component
public class SmsRequestsHelper {
    @Autowired
    private SmsRequestsRepository smsRequestsRepository;

    public void saveSmsRequest(SmsRequest smsRequest) {
        smsRequestsRepository.saveSmsRequest(smsRequest);
    }

    public SmsRequest getSmsRequestById(int id) {
        Optional<SmsRequest> optionalSmsRequest = smsRequestsRepository.findSmsRequestById(id);
        return optionalSmsRequest.orElse(null);
    }

    public SmsRequest saveSmsRequest(SmsRequest smsRequest, boolean isBlacklisted, String failureCode, String failureComments) {
        return smsRequestsRepository.updatedSmsRequest(smsRequest, isBlacklisted, failureCode, failureComments);
    }

}
