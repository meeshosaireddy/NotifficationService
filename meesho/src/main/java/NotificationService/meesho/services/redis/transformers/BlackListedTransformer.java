package NotificationService.meesho.services.redis.transformers;

import NotificationService.meesho.dao.entities.sql.SmsRequest;
import NotificationService.meesho.services.blacklist.impl.BlackListedServiceImpl;
import NotificationService.meesho.services.elasticsearch.impl.ElasticsearchSmsRequestsServiceImpl;
import NotificationService.meesho.services.redis.impl.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlackListedTransformer {
    @Autowired
    private RedisServiceImpl redisServiceImpl;
    @Autowired
    private BlackListedServiceImpl blackListedServiceImpl;
    public boolean checkIfBlacklisted(SmsRequest smsRequest) {
        boolean isBlacklisted = redisServiceImpl.checkBlacklistNumber(smsRequest.getPhoneNumber());
        if (!isBlacklisted && !blackListedServiceImpl.isPhoneNumberBlacklisted(smsRequest.getPhoneNumber())) {
            redisServiceImpl.addBlacklistNumber(smsRequest.getPhoneNumber());
            isBlacklisted = true;
        }
        return isBlacklisted;
    }
}
