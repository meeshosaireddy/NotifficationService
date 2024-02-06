package NotificationService.meesho.dao.entities.models;

import NotificationService.meesho.dao.entities.sql.SmsRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsSuccessResponse {
    private Data data;


    @NoArgsConstructor
    @AllArgsConstructor
    @lombok.Data
    public static class Data {
        private SmsRequest smsRequest;
    }

}
