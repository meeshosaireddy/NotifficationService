package NotificationService.meesho.services.redis.transformers;

import NotificationService.meesho.dao.entities.sql.SmsRequest;
import NotificationService.meesho.services.blacklist.helpers.BlackListedHelper;
import NotificationService.meesho.services.redis.impl.BlacklistServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlackListedTransformer {
    @Autowired
    private BlacklistServiceImpl blacklistService;
    @Autowired
    private BlackListedHelper blackListedHelper;

    public boolean checkIfBlacklisted(SmsRequest smsRequest) {
        boolean isBlacklisted = blacklistService.checkBlacklistNumber(smsRequest.getPhoneNumber());
        if (!isBlacklisted && !blackListedHelper.isPhoneNumberBlacklisted(smsRequest.getPhoneNumber())) {
            blacklistService.addBlacklistNumber(smsRequest.getPhoneNumber());
            isBlacklisted = true;
        }
        return isBlacklisted;
    }
}
