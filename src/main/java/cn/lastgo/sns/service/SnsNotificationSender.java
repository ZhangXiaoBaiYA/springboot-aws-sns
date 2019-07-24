package cn.lastgo.sns.service;

import com.amazonaws.services.sns.AmazonSNS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SnsNotificationSender {

    private final NotificationMessagingTemplate notificationMessagingTemplate;

    @Autowired
    public SnsNotificationSender(AmazonSNS amazonSns) {
        this.notificationMessagingTemplate = new NotificationMessagingTemplate(amazonSns);
    }

    public void send(String subject, String message) {
        this.notificationMessagingTemplate.sendNotification("test-transfer-fee", message, subject);
    }
}