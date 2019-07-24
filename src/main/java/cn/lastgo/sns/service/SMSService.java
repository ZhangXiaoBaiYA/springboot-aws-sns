package cn.lastgo.sns.service;

public interface SMSService
{
    String sendMessage(final String topicArn, final String message );
    String createTopic(final String topicName);

    String createSubscriptionSMS(final String topicArn, final String phoneNumber);

    String createSubscriptionEMAIL(final String topicArn, final String email);

    String createSubscription(final String topicArn,final String protocol, final String content);
}
