package NotificationService.meesho.services.redis;

import java.util.List;

public interface BlacklistService {
    void addBlacklistNumber(String phoneNumber);

    void removeBlacklistNumber(String phoneNumber);

    boolean checkBlacklistNumber(String phoneNumber);

    void checkAndAddBlacklistedPhoneNumber(String phoneNumber);

    List<String> getBlacklistedPhoneNumbers();
}
