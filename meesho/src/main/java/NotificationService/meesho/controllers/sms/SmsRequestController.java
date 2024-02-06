package NotificationService.meesho.controllers.sms;

import NotificationService.meesho.dao.entities.models.ErrorResponse;
import NotificationService.meesho.dao.entities.models.SuccessResponse;
import NotificationService.meesho.dao.entities.models.UserMessage;
import NotificationService.meesho.constants.ErrorConstants;
import NotificationService.meesho.constants.KafkaConstants;
import NotificationService.meesho.constants.SmsConstants;
import NotificationService.meesho.constants.api.SmsAPIConstants;
import NotificationService.meesho.dao.entities.sql.SmsRequest;
import NotificationService.meesho.services.smsrequest.SmsRequestService;
import NotificationService.meesho.transformers.ErrorResponseTransformer;
import NotificationService.meesho.transformers.SmsRequestTransformer;
import NotificationService.meesho.transformers.SuccessResponseTransformer;
import NotificationService.meesho.validators.sendsms.SendSmsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class SmsRequestController {


    @Autowired
    private SmsRequestService smsRequestService;
    @Autowired
    private SendSmsValidator sendSmsValidator;
    @Autowired
    private SmsRequestTransformer smsRequestTransformer;

    @PostMapping(SmsAPIConstants.SEND_SMS)
    public ResponseEntity<?> sendSms(@RequestBody UserMessage userMessage) {
        try {
            if (!sendSmsValidator.validateUserMessage(userMessage)) {
                ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(ErrorConstants.INVALID_REQUEST, ErrorConstants.ALL_FIELDS_ARE_MANDATORY);
                return ResponseEntity.status(400).body(errorResponse);
            }
            SmsRequest smsRequest = smsRequestTransformer.getSmsRequest(userMessage, "0");
            smsRequestService.sendSms(smsRequest);
            while (KafkaConstants.BLACKLISTED == 0) ;
            if (KafkaConstants.ERROR && (KafkaConstants.BLACKLISTED == 1)) {
                SuccessResponse successResponse = new SuccessResponse(new SuccessResponse.Data(String.valueOf(smsRequest.getId()), SmsConstants.SMS_SUCCESS));
                KafkaConstants.BLACKLISTED = 0;
                return ResponseEntity.ok(successResponse);
            } else {
                ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(SmsConstants.INTERNAL_SERVER_ERROR, SmsConstants.SENDING_SMS_FAILED);
                KafkaConstants.BLACKLISTED = 0;
                return ResponseEntity.status(500).body(errorResponse);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(SmsConstants.INTERNAL_SERVER_ERROR, SmsConstants.SENDING_SMS_FAILED);
            KafkaConstants.BLACKLISTED = 0;
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping(SmsAPIConstants.GET_SMS)
    public ResponseEntity<?> getSmsRequestDetails(@PathVariable int requestId) {
        SmsRequest smsRequest;
        try {
            smsRequest = smsRequestService.getSmsRequestById(requestId);
            if (smsRequest == null) {
                ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(ErrorConstants.INVALID_REQUEST, ErrorConstants.SMS_NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(ErrorConstants.INVALID_REQUEST, ErrorConstants.SMS_NOT_FOUND);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        return ResponseEntity.ok(SuccessResponseTransformer.smsSuccessResponse(smsRequest));
    }
}
