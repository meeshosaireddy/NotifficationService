package NotificationService.meesho.repositories.dbrepositories;

import NotificationService.meesho.dao.entities.sql.SmsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRequestDBRepository extends JpaRepository<SmsRequest,Integer> {

}
