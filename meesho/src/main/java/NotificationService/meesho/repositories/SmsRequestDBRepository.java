package NotificationService.meesho.repositories;

import NotificationService.meesho.models.SmsRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRequestDBRepository extends JpaRepository<SmsRequests,Integer> {

}
