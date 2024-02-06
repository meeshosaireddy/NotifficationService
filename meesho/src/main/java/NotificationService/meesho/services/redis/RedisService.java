package NotificationService.meesho.services.redis;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public interface RedisService {
    void addBlacklistNumber(String phoneNumber);

    void removeBlacklistNumber(String phoneNumber);

    boolean checkBlacklistNumber(String phoneNumber);

    void checkAndAddBlacklistedPhoneNumber(String phoneNumber);

    List<String> getBlacklistedPhoneNumbers();
}
