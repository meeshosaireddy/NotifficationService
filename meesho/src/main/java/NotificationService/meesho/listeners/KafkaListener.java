package NotificationService.meesho.listeners;

import NotificationService.meesho.constants.KafkaConstants;
import NotificationService.meesho.dao.entities.sql.SmsRequest;
import NotificationService.meesho.services.elasticsearch.impl.ElasticsearchSmsRequestsServiceImpl;
import NotificationService.meesho.services.redis.transformers.BlackListedTransformer;
import NotificationService.meesho.components.SendSmsComponent;
import NotificationService.meesho.services.smsrequest.SmsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaListener {
    @Autowired
    private SmsRequestService smsRequestService;
    @Autowired
    private BlackListedTransformer blackListedTransformer;
    @Autowired
    private ElasticsearchSmsRequestsServiceImpl elasticsearchSmsRequestsServiceImpl;

    @org.springframework.kafka.annotation.KafkaListener(topics = KafkaConstants.TOPICS, groupId = KafkaConstants.GROUP_ID)
    public void checkForBlacklisted(int requestId) {
        SmsRequest smsRequest = smsRequestService.getSmsRequestById(requestId);
        boolean isBlacklisted = blackListedTransformer.checkIfBlacklisted(smsRequest);
        if (!isBlacklisted) {
            try {
                smsRequest = saveSmsRequest(smsRequest, isBlacklisted, "", "");
                SendSmsComponent.sendSms(smsRequest);
            } catch (Exception e) {
                smsRequest = saveSmsRequest(smsRequest, isBlacklisted, KafkaConstants.INTERNAL_SERVER_ERROR, KafkaConstants.MESSAGE_SENDING_FAILED);
                KafkaConstants.ERROR = false;
            }
        } else {
            smsRequest = saveSmsRequest(smsRequest, isBlacklisted, KafkaConstants.INTERNAL_SERVER_ERROR, KafkaConstants.BLACKLISTED_PHONE_NUMBER);
        }
        elasticsearchSmsRequestsServiceImpl.indexSmsRequest(smsRequest);
        KafkaConstants.BLACKLISTED = isBlacklisted ? 2 : 1;

    }

    public SmsRequest saveSmsRequest(SmsRequest smsRequest, boolean isBlacklisted, String failureCode, String failureComments) {
        return smsRequestService.saveSmsRequest(smsRequest, isBlacklisted, failureCode, failureComments);
    }

}
