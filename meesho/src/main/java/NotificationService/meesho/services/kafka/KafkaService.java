package NotificationService.meesho.services.kafka;

import NotificationService.meesho.constants.KafkaConstants;
import NotificationService.meesho.services.elasticsearch.impl.ElasticsearchSmsRequestsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    @Autowired
    private KafkaTemplate<String, Integer> kafkaTemplate;
    @Autowired
    private ElasticsearchSmsRequestsServiceImpl elasticsearchSmsRequestsServiceImpl;
    public void sendTopic(int requestId){
        kafkaTemplate.send(KafkaConstants.TOPICS, requestId);
    }
}
