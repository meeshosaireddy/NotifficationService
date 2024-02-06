package NotificationService.meesho.dao.entities.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Destination {
    private List<String> msisdn;
    private String correlationId;
}

