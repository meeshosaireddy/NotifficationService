package NotificationService.meesho.services.redis.impl;

import NotificationService.meesho.constants.RedisConstants;
import NotificationService.meesho.dao.entities.sql.BlackListPhoneNumber;
import NotificationService.meesho.services.blacklist.helpers.BlackListedHelper;
import NotificationService.meesho.services.redis.BlacklistService;
import NotificationService.meesho.services.redis.helpers.BlackListPhoneNumberCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    private BlackListedHelper blackListedHelper;

    @Override
    public void addBlacklistNumber(String phoneNumber) {
        BlackListPhoneNumber blackListPhoneNumber = new BlackListPhoneNumber();
        blackListPhoneNumber.setPhoneNumber(phoneNumber);
        blackListedHelper.addBlackListPhoneNumber(blackListPhoneNumber);
    }

    @Override
    public void removeBlacklistNumber(String phoneNumber) {
        blackListedHelper.removePhoneNumberFromBlackList(phoneNumber);
    }

    @Override
    public boolean checkBlacklistNumber(String phoneNumber) {
        //just check in redis whether its present or not, if not present then check in db
        //while saving : save in both db and redis
        return blackListedHelper.checkIfPhoneNumberBlacklisted(phoneNumber);
    }

    @Override
    @Cacheable(value = RedisConstants.VALUE)
    public List<String> getBlacklistedPhoneNumbers() {
        List<BlackListPhoneNumber> blackListPhoneNumbers = blackListedHelper.getAllBlacklistedPhoneNumbers();
        //TODO : return string
        return null;
    }

    @Override
    public void checkAndAddBlacklistedPhoneNumber(String phoneNumber) {
        if (!checkBlacklistNumber(phoneNumber))
            addBlacklistNumber(phoneNumber);
    }
}
