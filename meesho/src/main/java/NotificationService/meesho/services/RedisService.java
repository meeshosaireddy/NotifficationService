package NotificationService.meesho.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisService {

    private final RedisTemplate<String, List<String>> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, List<String>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @CacheEvict(value = "blacklistedPhoneNumbers", allEntries = true)
    public void addBlacklistNumber(String phoneNumber) {
        String key = "send_sms";
        List<String> existingNumbers = getBlacklistedPhoneNumbers();
        if (existingNumbers==null) {
            existingNumbers=new ArrayList<>();

        }
        existingNumbers.add(phoneNumber);
        redisTemplate.opsForValue().set(key, existingNumbers);
    }

    @CacheEvict(value = "blacklistedPhoneNumbers", allEntries = true)
    public void removeBlacklistNumber(String phoneNumber) {
        String key = "send_sms";
        try {
            System.out.println(phoneNumber);
            List<String> existingNumbers = getBlacklistedPhoneNumbers();
            System.out.println(existingNumbers);
            boolean r=existingNumbers.remove(phoneNumber);
            System.out.println(r);

            redisTemplate.opsForValue().set(key, existingNumbers);
        }catch (Exception e){
            System.out.println(e);
        }
    }


    @Cacheable(value = "blacklistedPhoneNumbers")
    public boolean checkBlacklistNumber(String phoneNumber) {
        List<String> blacklistedNumbers = getBlacklistedPhoneNumbers();
        return blacklistedNumbers.contains(phoneNumber);
    }

    @Cacheable(value = "blacklistedPhoneNumbers")
    public List<String> getBlacklistedPhoneNumbers() {
        String key = "send_sms";
        System.out.println("fef");
        List<String> blacklistedNumbers = null;
        try{
            System.out.println(redisTemplate.opsForValue().get(key));
            blacklistedNumbers=redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println(blacklistedNumbers);
        return blacklistedNumbers != null ? blacklistedNumbers : List.of();
    }
}
