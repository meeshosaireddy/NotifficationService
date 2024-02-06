package NotificationService.meesho.services.redis.impl;

import NotificationService.meesho.constants.RedisConstants;
import NotificationService.meesho.dao.entities.sql.BlackListPhoneNumber;
import NotificationService.meesho.services.blacklist.impl.BlackListedServiceImpl;
import NotificationService.meesho.services.redis.RedisService;
import NotificationService.meesho.services.redis.helpers.RedisHelper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private BlackListedServiceImpl blackListedServiceImpl;
    String key = RedisConstants.KEY;

    @Override
    @CacheEvict(value = RedisConstants.VALUE, allEntries = true)
    public void addBlacklistNumber(String phoneNumber) {
       redisHelper.addBlacklistedPhoneNumber(phoneNumber);
       BlackListPhoneNumber blackListPhoneNumber = new BlackListPhoneNumber();
       blackListPhoneNumber.setPhoneNumber(phoneNumber);
       blackListedServiceImpl.addBlackListPhoneNumber(blackListPhoneNumber);

    }
    @Override
    @CacheEvict(value = RedisConstants.VALUE, allEntries = true)
    public void removeBlacklistNumber(String phoneNumber) {
        redisHelper.removeBlacklistedPhoneNumber(phoneNumber);
        blackListedServiceImpl.removePhoneNumberFromBlackList(phoneNumber);
    }


    @Override
    @Cacheable(value = RedisConstants.VALUE)
    public boolean checkBlacklistNumber(String phoneNumber) {
       if (!redisHelper.checkPhoneNumberBlacklisted(phoneNumber)){

           if (!blackListedServiceImpl.isPhoneNumberBlacklisted(phoneNumber)){
               addBlacklistNumber(phoneNumber);
               return true;
           }
           return false;
       }
       return true;
    }
    @Override
    @Cacheable(value = RedisConstants.VALUE)
    public List<String> getBlacklistedPhoneNumbers() {
        return redisHelper.getBlacklistedPhoneNumbers();
    }

    @Override
    public void checkAndAddBlacklistedPhoneNumber(String phoneNumber){
        if (!checkBlacklistNumber(phoneNumber))
             addBlacklistNumber(phoneNumber);
    }
}
