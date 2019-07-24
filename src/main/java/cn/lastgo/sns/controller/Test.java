package cn.lastgo.sns.controller;

import cn.lastgo.sns.service.AwsSNSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Test {
    @Autowired
    AwsSNSService awsSNSService;


    @Value( value = "${aws.sns.topic}" )
    private String              topicArn;

    @GetMapping("/index")
    public void index(String topic){
        String arn = this.topicArn;
        if(topic != null && !topic.equals("")){
            arn = topic;
        }
        System.out.println(arn);
        awsSNSService.sendMessage(arn,"springboot test for amazon sns");
    }


    @GetMapping("/create-topic")
    public void createTopic(String topicName){
        System.out.println(topicName);
        if (topicName == null || topicName.equals("")) {
            System.out.println("topic name can not null");
            return;
        }
        awsSNSService.createTopic(topicName);
    }

    @GetMapping("/create-subscription-email")
    public void createSubscriptionsEmail(String email){
        String topArn = "arn:aws:sns:ap-northeast-1:547236205914:test-topic-1";
        System.out.println(email);
        if (email == null || email.equals("")) {
            System.out.println("email name can not null");
            return;
        }
        awsSNSService.createSubscriptionEMAIL(topArn,email);
    }

    @GetMapping("/create-subscription-sms")
    public void createSubscriptionsSMS(String phoneNumber){
        System.out.println(phoneNumber);
        if (phoneNumber == null || phoneNumber.equals("")) {
            System.out.println("phone number can not null");
            return;
        }
        awsSNSService.createSubscriptionSMS(topicArn,phoneNumber);
    }


    //http://localhost:8080/create-subscription?topicArn=arn:aws:sns:ap-northeast-1:547236205914:test-sns-sqs&protocol=sqs&content=arn:aws:sqs:ap-northeast-1:547236205914:test-queue
    @GetMapping("/create-subscription")
    public void createSubscription(String topicArn,String protocol,String content){
        //这里先使用已将创建好的topicArn,也可以前端传过来
        System.out.println(topicArn);
        System.out.println(protocol);
        System.out.println(content);
        //如果是sqs  则必须传递 arn  例如：arn:aws:sqs:ap-northeast-1:547236205914:test-queue

        if (topicArn == null || topicArn.equals("")) {
            System.out.println("topicArn can not null");
            return;
        }
        if (protocol == null || protocol.equals("")) {
            System.out.println("protocol can not null");
            return;
        }
        if (content == null || content.equals("")) {
            System.out.println("content can not null");
            return;
        }
        awsSNSService.createSubscription(topicArn,protocol,content);
    }
}
