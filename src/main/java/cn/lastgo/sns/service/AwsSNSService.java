package cn.lastgo.sns.service;

import com.amazonaws.ResponseMetadata;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

/**
 * Sends SMS using the AWS Simple Notification Service.
 *
 * @author zhangxiaobai
 */
@Service
public class AwsSNSService implements SMSService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsSNSService.class);

    @Value(value = "${cloud.aws.region.static}")
    private String region;
    @Value(value = "${cloud.aws.endpoint.static}")
    private String endPoint;
    @Value(value = "${cloud.aws.credentials.accessKey}")
    private String awsAccessKey;
    @Value(value = "${cloud.aws.credentials.secretKey}")
    private String awsSecretKey;


    /**
     * Create instance of AmazonSNS using AWS Default credentials.
     *
     * @return AmazonSNS Client for accessing Amazon SNS.
     */
    public AmazonSNS getSNSClient() {
        return AmazonSNSClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region)).build();
    }

    /**
     * Uses the publish method of the AmazonSNSClient class to send a message
     *
     * @param topicArn The recipient topic arn.
     * @param message  The message to send.
     * @return message Id The message identification
     */
    @Override
    public String sendMessage(final String topicArn, final String message) {
        final AmazonSNS snsClient = getSNSClient();

        LOGGER.info("Sending SMS... MESSAGE: {} TOPIC ARN: {}", message, topicArn);

        try {
            //publish request message
            final PublishRequest publishRequest = new PublishRequest(topicArn, message);

            final PublishResult result = snsClient.publish(publishRequest);
            //get message id
            final String messageId = result.getMessageId();

            LOGGER.info("Message successfully sent to: {} with messageId: {}, request id: {} ",
                    topicArn, messageId, result.getSdkResponseMetadata().getRequestId());
            return messageId;
        } catch (InvalidParameterException | InvalidParameterValueException
                | AuthorizationErrorException e) {
            System.out.println(e);
            LOGGER.error("Failed to send SMS to: " + topicArn + " with message: " + message);
            LOGGER.error("Error encountered while sending SMS: " + e.getMessage());
        }

        return null;
    }

    /**
     * create a topic
     * @param topicName can not be null
     * @return topic arn
     */
    @Override
    public String createTopic(String topicName) {
        final AmazonSNS snsClient = getSNSClient();

        LOGGER.info("Create a topic use springboot ... TOPIC NAME: {}", topicName);
        try {
            // Create an Amazon SNS topic.
            final CreateTopicRequest createTopicRequest = new CreateTopicRequest(topicName);
            final CreateTopicResult result = snsClient.createTopic(createTopicRequest);
            //get topic arn
            String topicArn = result.getTopicArn();
            LOGGER.info("Create Topic successfully, topic name: {}, topic arn: {}, request id: {}",topicName, topicArn, result.getSdkResponseMetadata().getRequestId());
            return topicArn;
        } catch (InvalidParameterException | InvalidParameterValueException
                | AuthorizationErrorException e) {
            LOGGER.error("Failed to create topic: {} ",topicName);
            LOGGER.error("Error encountered while creating topic: " + e.getMessage());
        }

        return null;
    }


    /**
     * create subscription for a topic
     * this subscription will send message to a phone number while topic receive message
     * protocol
     *        http
     *        https
     *        email
     *        email-json
     *        sqs
     *        sms
     *        ...
     *
     * @param topicArn target topic
     * @param phoneNumber  The recipient number. Should have country code as prefix.
     * @return
     */
    @Override
    public String createSubscriptionSMS(String topicArn, String phoneNumber) {
        final AmazonSNS snsClient = getSNSClient();

        LOGGER.info("Create a subscription use protocol SMS for a topic ... TOPIC ARN: {}", topicArn);
        try {
            // Create an Amazon SNS topic subscription.
            SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "sms", phoneNumber);
            SubscribeResult result = snsClient.subscribe(subscribeRequest);
            //get topic arn
            String subscriptionArn = result.getSubscriptionArn();
            ResponseMetadata cachedResponseMetadata = snsClient.getCachedResponseMetadata(subscribeRequest);
            LOGGER.info("Create Topic successfully, topic arn: {}, subscription arn: {}, request id: {}",topicArn, subscriptionArn, cachedResponseMetadata.getRequestId());
            return topicArn;
        } catch (InvalidParameterException | InvalidParameterValueException
                | AuthorizationErrorException e) {
            LOGGER.error("Failed to create subscription for topic arn: {} ",topicArn);
            LOGGER.error("Error encountered while creating subscription: " + e.getMessage());
        }

        return null;
    }

    @Override
    public String createSubscriptionEMAIL(String topicArn, String email) {

        final AmazonSNS snsClient = getSNSClient();

        LOGGER.info("Create a subscription use protocol EMAIL for a topic ... TOPIC ARN: {}", topicArn);
        try {
            // Create an Amazon SNS topic subscription.
            SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "email", email);
            SubscribeResult result = snsClient.subscribe(subscribeRequest);
            //get topic arn
            String subscriptionArn = result.getSubscriptionArn();
            ResponseMetadata cachedResponseMetadata = snsClient.getCachedResponseMetadata(subscribeRequest);
            LOGGER.info("Create Topic successfully, topic arn: {}, subscription arn: {}, request id: {}",topicArn, subscriptionArn, cachedResponseMetadata.getRequestId());
            return topicArn;
        } catch (InvalidParameterException | InvalidParameterValueException
                | AuthorizationErrorException e) {
            LOGGER.error("Failed to create subscription for topic arn: {} ",topicArn);
            LOGGER.error("Error encountered while creating subscription: " + e.getMessage());
        }

        return null;
    }

    /**
     * 通用方法，用来创建topic subscription
     * @param topicArn 指定topic arn
     * @param protocol 指定 protocol 类型字符串
     * @param content 指定类型的 内容 例如 protocol ： email 则 content : example@xx.com
     * @return 返回创建的 subscription ID
     */
    @Override
    public String createSubscription(String topicArn, String protocol, String content) {

        final AmazonSNS snsClient = getSNSClient();

        LOGGER.info("Create a subscription use protocol {} for a topic ... TOPIC ARN: {}",protocol, topicArn);
        try {
            // Create an Amazon topic subscription.
            SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, protocol, content);
            SubscribeResult result = snsClient.subscribe(subscribeRequest);
            //get topic arn
            String subscriptionArn = result.getSubscriptionArn();
            ResponseMetadata cachedResponseMetadata = snsClient.getCachedResponseMetadata(subscribeRequest);
            LOGGER.info("Create Topic successfully, topic arn: {}, subscription arn: {}, request id: {}",topicArn, subscriptionArn, cachedResponseMetadata.getRequestId());
            return cachedResponseMetadata.getRequestId();
        } catch (InvalidParameterException | InvalidParameterValueException
                | AuthorizationErrorException e) {
            LOGGER.error("Failed to create subscription for topic arn: {} ",topicArn);
            LOGGER.error("Error encountered while creating subscription: " + e.getMessage());
        }

        return null;
    }
}
