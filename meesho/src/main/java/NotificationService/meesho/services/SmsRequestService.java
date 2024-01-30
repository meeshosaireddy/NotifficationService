package NotificationService.meesho.services;

import NotificationService.meesho.models.SmsRequests;
import NotificationService.meesho.repositories.SmsRequestDBRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SmsRequestService {

    private final SmsRequestDBRepository smsRequestsRepository;

    public SmsRequestService(SmsRequestDBRepository smsRequestsRepository) {
        this.smsRequestsRepository = smsRequestsRepository;
    }

    public void insertSmsRequest(SmsRequests smsRequests){
        this.smsRequestsRepository.save(smsRequests);
    }
    public SmsRequests getSmsRequestById(int id) {
        Optional<SmsRequests> optionalSmsRequest = smsRequestsRepository.findById(id);
        return optionalSmsRequest.orElse(null);
    }
    public void updateStatus(int requestId, boolean isBlacklisted) {
        Optional<SmsRequests> SmsRequest = smsRequestsRepository.findById(requestId);

        if (SmsRequest.isPresent()) {
            SmsRequests smsRequest = SmsRequest.get();
            smsRequest.setStatus("Blacklisted");
            smsRequestsRepository.save(smsRequest);
        } else {
            // Handle the case when the SMS request with the given ID is not found
            throw new NoSuchElementException("SMS request with ID " + requestId + " not found.");
        }
    }

}
