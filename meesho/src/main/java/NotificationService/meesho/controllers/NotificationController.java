package NotificationService.meesho.controllers;

import NotificationService.meesho.clientmodels.ErrorResponse;
import NotificationService.meesho.clientmodels.SuccessResponse;
import NotificationService.meesho.clientmodels.UserMessage;
import NotificationService.meesho.models.SmsRequests;
import NotificationService.meesho.models.SmsRequestsDocument;
//import NotificationService.meesho.services.ElasticsearchSmsRequestsService;
import NotificationService.meesho.services.ElasticsearchSmsRequestsService;
import NotificationService.meesho.services.RedisService;
import NotificationService.meesho.services.SmsRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class NotificationController {

    private final RedisService redisService;
    private final KafkaTemplate<String, Integer> kafkaTemplate;
    private final SmsRequestService smsRequestService;



    @Autowired
    private ElasticsearchSmsRequestsService elasticsearchSmsRequestsService;

    public NotificationController(RedisService redisService, KafkaTemplate<String, Integer> kafkaTemplate, SmsRequestService smsRequestService) {
        this.redisService = redisService;
        this.kafkaTemplate = kafkaTemplate;
        this.smsRequestService = smsRequestService;
    }

    //    @Transactional
    @PostMapping("/v1/sms/send")
    public ResponseEntity<Object> SendSms(@RequestBody UserMessage message_from_user) {
        try {
            // Validate request parameters
            if (message_from_user.getPhone_number() == null || message_from_user.getMessage() == null) {
                // Return an error response for missing parameters
                ErrorResponse errorResponse = new ErrorResponse("INVALID_REQUEST", "phone_number and message are mandatory");
                return ResponseEntity.status(400).body(errorResponse);
            }
            System.out.println("hello!");

            SmsRequests smsRequest = new SmsRequests();
            smsRequest.setPhone_number(message_from_user.getPhone_number());
            smsRequest.setMessage(message_from_user.getMessage());
            smsRequest.setStatus("NOT BLACKliSTED"); // Assuming an initial status
            smsRequest.setCreated_at(LocalDateTime.now()); // Set created_at to the current date and time
            smsRequest.setUpdated_at(LocalDateTime.now());

            // Save the SmsRequests entity to the database
            smsRequestService.insertSmsRequest(smsRequest);
            System.out.println("hello!");
            // Publish request_id to Kafka topic
            int requestId = (smsRequest.getId());
            kafkaTemplate.send("notification.send_sms", requestId);
            try {
                elasticsearchSmsRequestsService.indexSmsRequest(smsRequest);
            }catch (Exception e){
                System.out.println(e);
            }
            // Return a success response
            SuccessResponse successResponse = new SuccessResponse(requestId, "Successfully Sent");
            try {
                RestTemplate restTemplate = new RestTemplate();


                String API_URL="";
                String HEADERS="";
                ResponseEntity<String> response = restTemplate.postForEntity(API_URL, smsRequest, String.class, HEADERS);


                if (response.getStatusCode() == HttpStatus.OK) {
                    return ResponseEntity.ok(successResponse);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send SMS");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
            }

        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("INTERNAL_ERROR", "Error processing SMS request");
            return ResponseEntity.status(500).body(errorResponse);
        }

    }
//    @Transactional
    @DeleteMapping("/v1/blacklist")
    public ResponseEntity<String> DeletePhoneNumbers(@RequestBody String phone_number){
        try{
            redisService.removeBlacklistNumber(phone_number);
            //update database
        }catch (Exception e){
            String error_message="PhoneNumber couldn't be whitelisted";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error_message);
        }
        return null;
    }

    @PostMapping("/v1/blacklist")
    public ResponseEntity<String> PostPhoneNumbers(@RequestBody List<String> phone_numbers) {
        try {
            for (String phoneNumber : phone_numbers) {
                System.out.println("Hello!");
                redisService.addBlacklistNumber(phoneNumber);
                System.out.println("Hello!");
            }
        } catch (Exception e) {
            String error_message = "PhoneNumber couldn't be blacklisted";
            System.out.println(error_message);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error_message);
        }
        String success_message = "Successfully Blacklisted";
        return ResponseEntity.ok(success_message);
    }
    @GetMapping("/findByPhoneNumberAndTimeRange")
    public ResponseEntity<List<SmsRequestsDocument>> findByPhoneNumberAndTimeRange(
            @RequestParam String phoneNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {

        PageRequest pageRequest = PageRequest.of(page, size);
        List<SmsRequestsDocument> result = elasticsearchSmsRequestsService.getSmsMessagesByPhoneNumberAndTimeRange(
                phoneNumber, startTime, endTime,page,size);

        return ResponseEntity.ok(result);
    }
    @GetMapping("/v1/blacklist")
    public ResponseEntity<List<String>> getblacklistnumbers(){
        try{

        }catch (Exception e){
            String error_message="PhoneNumber couldn't be blacklisted";
            //error message
        }
        return ResponseEntity.ok(redisService.getBlacklistedPhoneNumbers());
    }
    @GetMapping("/v1/sms/{request_id}")
    public ResponseEntity<SmsRequests> getsmsdetails(@PathVariable int request_id) {
        try {

        } catch (Exception e) {
            String error_message = "Couldn't fetch";
            //error message
        }
        return ResponseEntity.ok(smsRequestService.getSmsRequestById(request_id));
    }
    @GetMapping("/sms/search")
    public List<SmsRequestsDocument> searchSmsMessages(
            @RequestParam String searchText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            return elasticsearchSmsRequestsService.getSmsMessagesContainingText(searchText, page, size);
        } catch (IOException e) {
            // Handle the IOException appropriately, such as logging the error or returning an error response
            e.printStackTrace();
            return null;
        }
    }

}
