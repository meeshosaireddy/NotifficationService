package NotificationService.meesho.services.blacklist;

import NotificationService.meesho.dao.entities.sql.BlackListPhoneNumber;

import java.util.List;

public interface BlackListedService {
    boolean isPhoneNumberBlacklisted(String phoneNumber);

    void addBlackListPhoneNumber(BlackListPhoneNumber blackListPhoneNumber);

    void removePhoneNumberFromBlackList(String phoneNumber);

     List<BlackListPhoneNumber> getAllBlacklistedPhoneNumbers();

}
