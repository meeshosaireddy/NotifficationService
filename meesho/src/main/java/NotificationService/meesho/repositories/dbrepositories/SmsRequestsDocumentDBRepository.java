package NotificationService.meesho.repositories.dbrepositories;

import NotificationService.meesho.dao.entities.sql.SmsRequestsDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SmsRequestsDocumentDBRepository extends ElasticsearchRepository<SmsRequestsDocument, Integer> {
}
