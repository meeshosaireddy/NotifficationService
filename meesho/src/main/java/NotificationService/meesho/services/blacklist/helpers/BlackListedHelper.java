package NotificationService.meesho.services.blacklist.helpers;

import NotificationService.meesho.dao.entities.sql.BlackListPhoneNumber;
import NotificationService.meesho.repositories.BlackListedPhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlackListedHelper {
    @Autowired
    private BlackListedPhoneNumberRepository blackListedPhoneNumberRepository;


    public boolean isPhoneNumberBlacklisted(String phone_number) {
        return blackListedPhoneNumberRepository.isPhoneNumberBlacklisted(phone_number);
    }


    public void addBlackListPhoneNumber(BlackListPhoneNumber blackListPhoneNumber) {
        blackListedPhoneNumberRepository.addBlackListPhoneNumber(blackListPhoneNumber);
    }


    public void removePhoneNumberFromBlackList(String phone_number) {
        blackListedPhoneNumberRepository.removePhoneNumberFromBlackList(phone_number);
    }

    public List<BlackListPhoneNumber> getAllBlacklistedPhoneNumbers(){
        return blackListedPhoneNumberRepository.getAllBlacklistedPhoneNumbers();
    }
}
