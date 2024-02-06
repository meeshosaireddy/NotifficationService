package NotificationService.meesho.services.blacklist.helpers;

import NotificationService.meesho.dao.entities.sql.BlackListPhoneNumber;
import NotificationService.meesho.repositories.BlackListedPhoneNumberRepository;
import NotificationService.meesho.services.redis.helpers.BlackListPhoneNumberCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlackListedHelper {

    @Autowired
    private BlackListedPhoneNumberRepository blackListedPhoneNumberRepository;
    @Autowired
    private BlackListPhoneNumberCacheRepository blackListPhoneNumberCacheRepository;


    public boolean isPhoneNumberBlacklisted(String phone_number) {
        return blackListedPhoneNumberRepository.isPhoneNumberBlacklisted(phone_number);
    }


    public void addBlackListPhoneNumber(BlackListPhoneNumber blackListPhoneNumber) {
        blackListedPhoneNumberRepository.addBlackListPhoneNumber(blackListPhoneNumber);
        blackListPhoneNumberCacheRepository.addBlacklistedPhoneNumber(blackListPhoneNumber.getPhoneNumber());
    }


    public void removePhoneNumberFromBlackList(String phoneNumber) {
        blackListedPhoneNumberRepository.removePhoneNumberFromBlackList(phoneNumber);
        blackListPhoneNumberCacheRepository.removeBlacklistedPhoneNumber(phoneNumber);
    }

    public List<BlackListPhoneNumber> getAllBlacklistedPhoneNumbers() {
        blackListPhoneNumberCacheRepository.getBlacklistedPhoneNumbers();
        return blackListedPhoneNumberRepository.getAllBlacklistedPhoneNumbers();
    }

    public boolean checkIfPhoneNumberBlacklisted(String phoneNumber) {
        if (!blackListPhoneNumberCacheRepository.checkPhoneNumberBlacklisted(phoneNumber)) {
            if (!isPhoneNumberBlacklisted(phoneNumber)) {
                BlackListPhoneNumber blackListPhoneNumber = new BlackListPhoneNumber();
                blackListPhoneNumber.setPhoneNumber(phoneNumber);
                addBlackListPhoneNumber(blackListPhoneNumber);
                return true;
            }
            return false;
        }
        return true;
    }
}
