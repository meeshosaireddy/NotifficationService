package NotificationService.meesho.services.smsrequest.impl;

import NotificationService.meesho.dao.entities.sql.SmsRequest;
import NotificationService.meesho.services.kafka.KafkaService;
import NotificationService.meesho.services.smsrequest.SmsRequestService;
import NotificationService.meesho.services.smsrequest.helpers.SmsRequestsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SmsRequestServiceImpl implements SmsRequestService {

    @Autowired
    private SmsRequestsHelper smsRequestsHelper;
    @Autowired
    private KafkaService kafkaService;

    @Override
    public void saveSmsRequest(SmsRequest smsRequest) {
        smsRequestsHelper.saveSmsRequest(smsRequest);
    }

    @Override
    public SmsRequest getSmsRequestById(int id) {
        Optional<SmsRequest> optionalSmsRequest = Optional.ofNullable(smsRequestsHelper.getSmsRequestById(id));
        return optionalSmsRequest.orElse(null);
    }

    @Override
    public SmsRequest saveSmsRequest(SmsRequest smsRequest, boolean isBlacklisted, String failure_code, String failure_comments) {
        return smsRequestsHelper.saveSmsRequest(smsRequest, isBlacklisted, failure_code, failure_comments);
    }

    @Override
    public void sendSms(SmsRequest smsRequest) {
        saveSmsRequest(smsRequest);
        int requestId = (smsRequest.getId());
        kafkaService.sendTopic(requestId);
    }
}
