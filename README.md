# RocketMQ-EasyClient

## 简介

&emsp;&emsp;rocketmq-easyclient是乐得技术团队针对rocketmq开发的一套易用java客户端。easyclient分别对producer和consumer做出了一些包装，可在实际项目中直接引入使用，使得开发人员更加集中精力在具体业务逻辑，而不是对rocketmq-client的二次包装上。

## 客户端封装概要

&emsp;&emsp;rocketmq服务器端功能强大，具体可参考链接：https://github.com/apache/incubator-rocketmq 。在客户端功能方面，我们的改进主要在易用性和与应用的整合上，目前封装的消息队列客户端基本上在rocketmq的基础上进行了以下扩展方便使用：

-  配置封装 : 使用properties配置文件即可完成`producer`或者`consumer`的设置，并且固定了配置文件路径，使得配置项不会随意散落在项目的各个配置文件中，查找确认方便。
-  `producer`封装 : 使用`producer`工厂可获得`producer`实例，提供了三种`producer`发送方式，同步、异步和不确认发送；分别又支持无序和有序消息，有序消息中的队列分配算法进行了封装，用户不用自己实现。默认为同步无序，api提供`config`类支持其他发送方式，该`config`类为后续消息重发功能基础。
-  `consumer`封装 : 消费者进行了抽象，将需要的各种类型consumer的实例化在封装中完成，用户只需要实现`handler`接口并添加类注解就可以使用，`consumer`的实现细节和`consumer`根据需要的实例化数量和类型等都已封装，用户无需关心，同样的`consumer`也支持了无序消费和有序消费，用户也无需处理为了接受这两种消息而需要进行特殊设置的细节问题。
- 概念简化，rocketmq将消息分为两层结构`topic`和`tag`，为了便于理解和易于使用，封装了`tag`层，只暴露`topic`给用户，让大家使用和其他常见mq概念一致。该调整会再沟通确认后续版本是否放开。

## 后续规划

&emsp;&emsp;目前版本主要封装集中在consumer端，后续计划从以下几个方面完善easyclient功能：
- `producer`发送增加消息重放机制，当连不上rocketmq服务器端或者由于其他原因而发送失败时，将消息保存，当连接恢复后重试消息发送。
- 增加与spring兼容的扫包方式，在`EasyMQRecMsgHandler`接口实现类中增加spring标签可以生效，即可以和spring注解配合使用，不需要再用ApplicationContext来获取bean了。
- 功能增强，增加连接多组MQ服务器端配置，增加精确延迟消息以及定时消息等功能。

## 使用说明

### producer

#### 如何引入`producer`

配置文件增加：

```
#一般在src/main/resources下增加：
easymqconfig/producers/easymq_producer.properties
easymq.producer.nameserver=192.168.1.1:9876;192.168.1.2:9876
easymq.producer.groupname=api_producer
easymq.producer.topicqueuenums=8
easymq.producer.sendmsgtimeoutmillis=3000
```

注：`easymq.producer.groupname`不能包含.号
pom.xml引入：

```xml
<dependency>
	<groupId>com.lede.tech.rocketmq</groupId>
	<artifactId>easyclient-producer</artifactId>
	<version>0.0.1-SNAPSHOT</version> <!-- 注意版本号请使用最新版 -->
</dependency>
```
工具静态类中可调用：

```java
EasyMQProducer producer = EasyMQProducerFactory.getProducer();
```

后续消息发送使用该producer实例即可。

#### 如何使用producer

`producer`目前暴露了3个api供大家调用

```java
//方法一：
public void sendMsg(String topic, String keys, String msg) throws MqWapperException, MqBussinessException

//方法二：
public void sendMsg(String topic, String keys, String msg, SendCallback callback)
			throws MqWapperException, MqBussinessException

//方法三：
public void sendMsg(EasyMQMessageConfig config) throws MqWapperException, MqBussinessException
```
以上方法中的异常`MqWapperException`为发生内部错误包装的异常，`MqBussinessException`为服务器端消息状态返回不正确包装的异常。
方法一为同步调用，参数为`topic`、`keys`、`msg`；`topic`即为主题，`msg`为具体发送的消息。`keys`为该消息对应的键，`keys`的取值尽量和消息唯一对应（允许不唯一，但不推荐不唯一），根据`keys`值，可以在后台查询具体消息
方法二为异步调用，额外参数`SendCallback`为回调接口：

```java
new SendCallback() {
	@Override
	public void onSuccess(SendResult sendResult)
	{
		System.out.println("success");
	}

	@Override
	public void onException(Throwable e)
	{
		System.out.println("failed");
	}
};
```
该回调函数处理消息发送成功或者失败后的处理方式。需要注意的一点是，这里指的异步是相对于消息队列服务器端消息处理而言，对于这次调用还是阻塞的。
方法三为使用`EasyMQMessageConfig`的定制化发送，当需要发送高级模式消息时，使用该方法，其中`EasyMQMessageConfig`为：

```java
public class EasyMQMessageConfig
{
	/*
	 * 发送模式 同步、异步、oneway
	 */
	private ProducerTransferMode transferMode;
	/**
	 * topic 主题
	 */
	private String topic;
	/**
	 * tags 主题下的标签，暂统一为default
	 */
	private String tags;
	/**
	 * keys 消息对应的key，建议每个消息有唯一的key
	 */
	private String keys;
	/**
	 * message 消息具体的文本形式
	 */
	private String message;
	/**
	 * charSet 编码
	 */
	private String charSet;
	/**
	 * callback 异步调用时，使用的回调函数
	 */
	private SendCallback callback;
	/**
	 * orderTag 顺序消息需要的参数，该参数相同值下的消息保证有序
	 */
	private String orderTag;
	/**
	 * delayLevel 延迟发送级别
	 */
	private EasyMQMessageDelayLevel delayLevel;

	public EasyMQMessageConfig(String topic, String keys, String message)
	{
		this.topic = topic;
		this.keys = keys;
		this.message = message;
		this.transferMode = ProducerTransferMode.SYNC;
		this.tags = MQConstant.TOPIC_DEFAULT_TAG;
		this.charSet = RemotingHelper.DEFAULT_CHARSET;
		this.callback = null;
		this.orderTag = "";
		this.delayLevel = null;
	}
        //忽略setter/getter和toString
}
```
该类设计主要为以后的消息重发服务。

#### producer发送高级模式

##### 顺序消息

以同步发送方法为例：

```java
		for (int i = 0; i < 5; i++)
		{
			EasyMQMessageConfig config = new EasyMQMessageConfig("topic20170118", "id" + i, "onlyU" + i);
			config.setOrderTag("order1");
			producer.sendMsg(config);
		}
```
增加调用`setOrderTag`为需要保持顺序的消息设置统一的`orderTag`则可以保证相同`orderTag`下的消息有序。
使用顺序消息需要注意：当一条消息最终消费失败前，相同`orderTag`的后续消息为了保证消息有序性都都不会被发送到`consumer`端。

##### 延迟消息

延迟消息只能按照级别进行延迟发送，目前的级别如下：
“1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h”
为了使用方便，这些级别进行了枚举包装：

```java
	EasyMQMessageConfig config = new EasyMQMessageConfig("topic20170119-delay", "id1", "onlyU1");
	config.setDelayLevel(EasyMQMessageDelayLevel.FIVE_MINUTES);
	producer.sendMsg(config);
```
`EasyMQMessageDelayLevel`中有所有支持18个级别的枚举

##### 其他设置

`EasyMQMessageConfig`中同样也可以设置消息编码、调整发送方式等。

### consumer

#### 如何引入`consumer`

配置文件增加：

```
#src/main/resources下增加：
easymqconfig/consumers/easymq_consumer.properties
easymq.consumer.nameserver=192.168.1.1:9876;192.168.1.2:9876
easymq.consumer.consumertimeoutminutes=15
easymq.consumer.scanpackage=com.lede.tech.rocketmq.easyclient.example.consumer
```
其中`easymq.consumer.scanpackage`为扫包路径，相关`consumer`接口实现类需要放到该包中

pom.xml引入：

```xml
<dependency>
	<groupId>com.lede.tech.rocketmq</groupId>
	<artifactId>easyclient-consumer</artifactId>
	<version>0.0.1-SNAPSHOT</version> <!-- 注意版本号请使用最新版 -->
</dependency>
```
项目启动需要调用：

```java
EasyMQConsumerManager.init();

```
例如在spring配置文件中中可以这么配置:

```xml
<bean id="easyMQConsumerManager" class="com.lede.tech.rocketmq.easyclient.consumer.EasyMQConsumerManager" init-method="init" />
```

#### 如何使用consumer


```java
@EasyMQConsumerMeta(topic = "topic20170119-con", group = "group2")
public class TestMQHandler2 implements EasyMQRecMsgHandler
{

	public void handle(List<MessageBean> msg) throws Exception
	{

		try
		{
			for (MessageBean message : msg)
			{
				System.out.println("group2 PaySuccessMQHandler" + message);
				System.out.println("group2 key:" + message.getKey());
				System.out.println("group2 message:" + message.getMessage());
			}
		}
		catch (Exception e)
		{
			//自定义操作，记录日志等
			throw e;
		}

	}
}
}
```

`MessageBean`中包括rocketmq原始消息`MessageExt`，在需要查看消息高级属性时可从中获取，例如消息产生时间等（比较该字段，可以在客户端实现消息过期）。
扫包路径中的添加注解`@EasyMQConsumerMeta`并实现了接口`EasyMQRecMsgHandler`中的类都会被加载。注解中指明接受的`topic`等信息。
注解类：

```java
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyMQConsumerMeta
{
	//订阅主题
	String topic();

	//分组，消息按组进行消费，一般来讲一组应用可以为一组，该group可以按照项目域名命名api_com（不能有.）
	String group();

	//消息是否为有序消息
	boolean isOrderly() default false;

	//消息是否广播接收
	boolean isBroadcast() default false;

	//消费者最小线程
	int consumerThreadCountMin() default 10;

	//消费者最大线程
	int consumerThreadCountMax() default 30;

	//每次推送消息数量
	int consumeMessageBatchMaxSize() default 1;

}
```

#### 消费者注意事项

消费者消费消息需要确保幂等性。消息队列为保证可靠性，在极端情况下（例如网络不稳）可能会造成消息重复发送。
消费逻辑中可以按照示例所示捕获异常记录日志后重新抛出，对于抛出异常的情况，消费者封装逻辑中不会返回消费成功，消息会在稍后重新进行推送。不希望重新推送，可以捕获异常不再抛出即可。










