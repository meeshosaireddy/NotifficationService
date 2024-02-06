package NotificationService.meesho.controllers.redis;

import NotificationService.meesho.services.blacklist.BlackListedService;
import NotificationService.meesho.services.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import NotificationService.meesho.dao.entities.models.ErrorResponse;
import NotificationService.meesho.constants.ErrorConstants;
import NotificationService.meesho.constants.RedisConstants;
import NotificationService.meesho.constants.api.RedisAPIConstants;
import NotificationService.meesho.services.blacklist.impl.BlackListedServiceImpl;
import NotificationService.meesho.services.redis.helpers.RedisHelper;
import NotificationService.meesho.transformers.ErrorResponseTransformer;
import NotificationService.meesho.transformers.SuccessResponseTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class RedisController {

    @Autowired
    RedisService redisService;
    @Autowired
    BlackListedService blackListedService;
    @Autowired
    RedisHelper redisHelper;
    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @GetMapping(RedisAPIConstants.GET_BLACKLISTED_PHONENUMBERS)
    public ResponseEntity<?> getBlacklistNumbers() {
        try {
            List<String> result = (redisService.getBlacklistedPhoneNumbers());
            if (result.isEmpty()) {
                ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(ErrorConstants.INVALID_REQUEST, ErrorConstants.DOCUMENT_NOT_FOUND);
                return ResponseEntity.ok(errorResponse);
            }
            return ResponseEntity.ok(SuccessResponseTransformer.blackListedSuccessResponse(result));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @DeleteMapping(RedisAPIConstants.REMOVE_BLACKLISTED_PHONENUMBERS)
    public ResponseEntity<?> removePhoneNumbersFromBlacklist(@RequestBody List<String> phoneNumbers) {
        try {
            for (String phoneNumber : phoneNumbers) {
                redisService.removeBlacklistNumber(phoneNumber);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(ErrorConstants.INVALID_REQUEST, ErrorConstants.WHITELISTED_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        return ResponseEntity.ok(SuccessResponseTransformer.redisSuccessResponse(RedisConstants.SUCCESS_WHITELISTED));
    }

    @PostMapping(RedisAPIConstants.ADD_BLACKLISTED_PHONENUMBERS)
    public ResponseEntity<?> addBlacklistedPhoneNumbers(@RequestBody List<String> phoneNumbers) {
        try {
            for (String phoneNumber : phoneNumbers) {
                redisService.checkAndAddBlacklistedPhoneNumber(phoneNumber);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponseTransformer.errorResponse(RedisConstants.INTERNAL_SERVER_ERROR, RedisConstants.ERROR_MESSAGE);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        return ResponseEntity.ok(SuccessResponseTransformer.redisSuccessResponse(RedisConstants.SUCCESS_BLACKLISTED));

    }
    @GetMapping(RedisAPIConstants.IS_BLACKLISTED)
    public ResponseEntity<?> isPhoneNumberBlacklisted(@RequestBody String phoneNumber) {
        return ResponseEntity.ok(redisService.checkBlacklistNumber(phoneNumber));
    }

}
