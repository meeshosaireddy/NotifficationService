package NotificationService.meesho.dao.entities.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(indexName = "notification-service")
public class SmsRequestsDocument {

    @Id
    private Integer id;

    private String phoneNumber;
    private String message;
    private String status;
    private String failureCode;
    private String failureComments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
