package NotificationService.meesho.transformers;

import NotificationService.meesho.dao.entities.models.BlackListedSuccessResponse;
import NotificationService.meesho.dao.entities.models.RedisSuccessResponse;
import NotificationService.meesho.dao.entities.models.SmsSuccessResponse;
import NotificationService.meesho.dao.entities.models.SuccessResponse;
import NotificationService.meesho.dao.entities.sql.SmsRequest;

import java.util.List;

public class SuccessResponseTransformer {
    public static SuccessResponse successResponse(String code, String message) {
        SuccessResponse.Data successdetails = new SuccessResponse.Data(code, message);
        return new SuccessResponse(successdetails);
    }

    public static RedisSuccessResponse redisSuccessResponse(String data) {
        return new RedisSuccessResponse(data);
    }

    public static BlackListedSuccessResponse blackListedSuccessResponse(List<String> data) {
        return new BlackListedSuccessResponse(data);
    }

    public static SmsSuccessResponse smsSuccessResponse(SmsRequest smsRequest) {
        SmsSuccessResponse.Data smssuccessdetails = new SmsSuccessResponse.Data(smsRequest);
        return new SmsSuccessResponse(smssuccessdetails);
    }
}
