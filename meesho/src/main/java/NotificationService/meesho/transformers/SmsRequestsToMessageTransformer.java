package NotificationService.meesho.transformers;

import NotificationService.meesho.constants.SmsConstants;
import NotificationService.meesho.dao.entities.models.Channel;
import NotificationService.meesho.dao.entities.models.Destination;
import NotificationService.meesho.dao.entities.models.SmsMessageToPhoneNumber;
import NotificationService.meesho.dao.entities.sql.SmsRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsRequestsToMessageTransformer {

    static String text = SmsConstants.TEXT;

    public static SmsMessageToPhoneNumber convertSmsRequestToMessage(SmsRequest smsRequest) {
        SmsMessageToPhoneNumber message = new SmsMessageToPhoneNumber();
        message.setDeliveryChannel(SmsConstants.SMS);
        Channel channel = new Channel();
        channel.setText(text);
        Map<String, Channel> channels = new HashMap<>();
        channels.put(SmsConstants.SMS, channel);
        message.setChannels(channels);
        Destination destination = new Destination();
        List<String> msisdnList = new ArrayList<>();
        msisdnList.add(smsRequest.getPhoneNumber());
        destination.setMsisdn(msisdnList);
        destination.setCorrelationId(String.valueOf(smsRequest.getId()));
        List<Destination> destinations = new ArrayList<>();
        destinations.add(destination);
        message.setDestination(destinations);
        return message;
    }
}