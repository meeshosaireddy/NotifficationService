package NotificationService.meesho.constants;

public class RedisConstants {
    public static String KEY="send_sms";
    public static String INTERNAL_SERVER_ERROR="INTERNAL_SERVER_ERROR";
    public static String ERROR_MESSAGE = "PhoneNumber couldn't be blacklisted";
    public static String SUCCESS_WHITELISTED="Successfully Whitelisted";
    public static String SUCCESS_BLACKLISTED="Successfully Blacklisted";
    public final  static String VALUE="blacklistedPhoneNumbers";
    public final static String HOST="localhost";
    public final static int PORT=6379;
}
