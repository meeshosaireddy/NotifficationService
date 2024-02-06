package NotificationService.meesho.repositories;

import NotificationService.meesho.dao.entities.sql.SmsRequest;
import NotificationService.meesho.repositories.dbrepositories.SmsRequestDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class SmsRequestsRepository {
    @Autowired
    private SmsRequestDBRepository smsRequestDBRepository;

    public void saveSmsRequest(SmsRequest smsRequest) {
        smsRequestDBRepository.save(smsRequest);
    }

    public Optional<SmsRequest> findSmsRequestById(int id) {
        return smsRequestDBRepository.findById(id);
    }

    public SmsRequest updatedSmsRequest(SmsRequest smsRequest, boolean isBlacklisted, String failureCode, String failureComments) {
        smsRequest.setStatus(isBlacklisted ? "0" : "1");
        smsRequest.setFailureCode(failureCode);
        smsRequest.setFailureComments(failureComments);
        smsRequest.setUpdatedAt(LocalDateTime.now());
        saveSmsRequest(smsRequest);
        return smsRequest;
    }
}
