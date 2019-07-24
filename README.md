# Springboot + Amazon + SNS
使用SNS 协调和管理向订阅终端节点或客户端交付或发送消息的过程.

本示例项目，包含
+ 消息发送
+ 创建topic
+ 创建订阅(短信)
+ 创建订阅(邮箱)
+ 创建订阅-通用
+ SQS 订阅 SNS

### 说明



### 配置文件 application.properties
```
# request key
cloud.aws.credentials.accessKey=请输入您的凭证KEY
cloud.aws.credentials.secretKey=请输入您的凭证密钥
cloud.aws.region.static=ap-northeast-1
cloud.aws.endpoint.static=sqs.ap-northeast-1.amazonaws.com