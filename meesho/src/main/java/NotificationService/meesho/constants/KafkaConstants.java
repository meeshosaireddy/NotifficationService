package NotificationService.meesho.constants;

public class KafkaConstants {
    public static boolean ERROR=true;
    public static int BLACKLISTED=0;
    public final static String TOPICS="notification.send_sms";
    public final static String GROUP_ID="send_sms";
    public  static String MESSAGE_SENDING_FAILED="Message Sending Failed";
    public  static String BLACKLISTED_PHONE_NUMBER = "BlackListed PhoneNumber";
    public static String INTERNAL_SERVER_ERROR="500";
    public final static String API_URL = "https://api.imiconnect.in/resources/v1/messaging";
    public final static String HEADERS = "c0c49ebf-ca44-11e9-9e4e-025282c394f2";
    public final static String SERVER="localhost:9092";
}
