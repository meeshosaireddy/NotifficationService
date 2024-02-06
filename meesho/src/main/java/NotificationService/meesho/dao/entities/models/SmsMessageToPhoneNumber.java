package NotificationService.meesho.dao.entities.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsMessageToPhoneNumber {
    private String deliveryChannel;
    private Map<String, Channel> channels;
    private List<Destination> destination;
}



