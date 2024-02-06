package NotificationService.meesho.repositories.dbrepositories;

import NotificationService.meesho.dao.entities.sql.BlackListPhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListedPhoneNumberDBRepository extends JpaRepository<BlackListPhoneNumber,Integer> {

    @Query("SELECT b FROM BlackListPhoneNumber b WHERE b.phoneNumber = :phoneNumber")
    BlackListPhoneNumber findByPhoneNumber(String phoneNumber);
}
