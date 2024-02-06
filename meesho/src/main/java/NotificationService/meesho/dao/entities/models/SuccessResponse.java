package NotificationService.meesho.dao.entities.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse {
    private Data data;


    @NoArgsConstructor
    @AllArgsConstructor
    @lombok.Data
    public static class Data {
        private String requestId;
        private String comments;
    }

}
