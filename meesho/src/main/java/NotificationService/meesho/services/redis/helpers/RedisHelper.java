package NotificationService.meesho.services.redis.helpers;

import NotificationService.meesho.constants.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RedisHelper {
    @Autowired
    private RedisTemplate<String, List<String>> redisTemplate;
    String key = RedisConstants.KEY;

    public List<String> getBlacklistedPhoneNumbers() {
        List<String> blacklistedNumbers = null;
        blacklistedNumbers = redisTemplate.opsForValue().get(key);
        return blacklistedNumbers != null ? blacklistedNumbers : List.of();
    }

    public boolean checkPhoneNumberBlacklisted(String phoneNumber) {
        List<String> blacklistedNumbers = getBlacklistedPhoneNumbers();
        return blacklistedNumbers.contains(phoneNumber);
    }

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

    public void removeBlacklistedPhoneNumber(String phoneNumber) {
        List<String> existingNumbers = getBlacklistedPhoneNumbers();
        System.out.println(existingNumbers);
        List<String> changed = new ArrayList<>();
        for (String i : existingNumbers) {
            if (!(i.equals(phoneNumber)))
                changed.add(i);
        }
//        System.out.println(changed);
        redisTemplate.opsForValue().set(key, changed);

    }
}
