package NotificationService.meesho.services.blacklist.impl;

import NotificationService.meesho.dao.entities.sql.BlackListPhoneNumber;
import NotificationService.meesho.services.blacklist.BlackListedService;
import NotificationService.meesho.services.blacklist.helpers.BlackListedHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BlackListedServiceImpl implements BlackListedService {

    @Autowired
    private BlackListedHelper blackListedHelper;

    @Override
    public boolean isPhoneNumberBlacklisted(String phone_number) {
       return blackListedHelper.isPhoneNumberBlacklisted(phone_number);
    }

    @Override
    public void addBlackListPhoneNumber(BlackListPhoneNumber blackListPhoneNumber) {
        blackListedHelper.addBlackListPhoneNumber(blackListPhoneNumber);
    }

    @Override
    public void removePhoneNumberFromBlackList(String phone_number) {
      blackListedHelper.removePhoneNumberFromBlackList(phone_number);
    }
    @Override
    public List<BlackListPhoneNumber> getAllBlacklistedPhoneNumbers(){
        return blackListedHelper.getAllBlacklistedPhoneNumbers();
    }
}
