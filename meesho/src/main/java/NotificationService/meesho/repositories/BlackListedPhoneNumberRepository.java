package NotificationService.meesho.repositories;

import NotificationService.meesho.repositories.dbrepositories.BlackListedPhoneNumberDBRepository;
import NotificationService.meesho.dao.entities.sql.BlackListPhoneNumber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlackListedPhoneNumberRepository {
    @Autowired
    BlackListedPhoneNumberDBRepository blackListedPhoneNumberDBRepository;

    public BlackListPhoneNumber findByPhoneNumber(String phoneNumber) {
        return blackListedPhoneNumberDBRepository.findByPhoneNumber(phoneNumber);
    }

    public void addBlackListPhoneNumber(BlackListPhoneNumber blackListPhoneNumber) {
        if (isPhoneNumberBlacklisted(blackListPhoneNumber.getPhoneNumber()))
            blackListedPhoneNumberDBRepository.save(blackListPhoneNumber);
    }

    public void removePhoneNumberFromBlackList(String phoneNumber) {
        BlackListPhoneNumber blackListPhoneNumber = blackListedPhoneNumberDBRepository.findByPhoneNumber(phoneNumber);
        if (blackListPhoneNumber != null)
            blackListedPhoneNumberDBRepository.delete(blackListPhoneNumber);
    }

    public boolean isPhoneNumberBlacklisted(String phoneNumber) {
        BlackListPhoneNumber blackListPhoneNumber = blackListedPhoneNumberDBRepository.findByPhoneNumber(phoneNumber);
        if (blackListPhoneNumber == null)
            return true;
        return false;
    }

    public List<BlackListPhoneNumber> getAllBlacklistedPhoneNumbers() {
        return blackListedPhoneNumberDBRepository.findAll();
    }


}
