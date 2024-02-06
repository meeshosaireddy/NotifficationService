package NotificationService.meesho.services.redis.helpers;

import NotificationService.meesho.constants.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BlackListPhoneNumberCacheRepository {

    @Autowired
    private RedisTemplate<String, List<String>> redisTemplate;
    //TODO: no need to create a constant for key
    String key = RedisConstants.KEY;

    public List<String> getBlacklistedPhoneNumbers() {
        List<String> blacklistedNumbers = redisTemplate.opsForValue().get(key);
        return blacklistedNumbers != null ? blacklistedNumbers : new ArrayList<>();
    }

    public boolean checkPhoneNumberBlacklisted(String phoneNumber) {
        List<String> blacklistedNumbers = getBlacklistedPhoneNumbers();
        return blacklistedNumbers.contains(phoneNumber);
    }

    //TODO : dont fetch and add, figure out a way to directly add to set of phone number
    public void addBlacklistedPhoneNumber(String phoneNumber) {
        List<String> existingNumbers = getBlacklistedPhoneNumbers();
        if (!existingNumbers.contains(phoneNumber))
            existingNumbers.add(phoneNumber);
        System.out.println(existingNumbers);
        redisTemplate.opsForValue().set(key, existingNumbers);
    }

    public void deleteAllBlacklistedPhoneNumbers() {
        redisTemplate.opsForValue().set(key, new ArrayList<>());
    }

    //TODO : directly remove phonenumber from set of phone number
    public void removeBlacklistedPhoneNumber(String phoneNumber) {
        List<String> existingNumbers = getBlacklistedPhoneNumbers();
        List<String> changed = new ArrayList<>();
        for (String existingPhoneNumber : existingNumbers) {
            if (!(existingPhoneNumber.equals(phoneNumber)))
                changed.add(existingPhoneNumber);
        }
//        System.out.println(changed);
        redisTemplate.opsForValue().set(key, changed);

    }
}
