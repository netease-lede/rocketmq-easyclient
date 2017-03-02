package com.lede.tech.rocketmq.easyclient.common.constant;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
public class MQConstant
{
	public static final boolean IS_WINDOWS = System.getProperty("os.name").contains("indow");

	//rocketMQ配置文件夹
	public static final String CONFIG_DIR = "easymqconfig";
	public static final String CONFIG_DIR_PRODUCERS = "producers";
	public static final String CONFIG_DIR_CONSUMERS = "consumers";
	//rocketMQ生产者默认配置文件名称
	public static final String DEFAULT_PRODUCER_FILENAME = "easymq_producer.properties";
	//rocketMQ消费者默认配置文件名称
	public static final String DEFAULT_CONSUMER_FILENAME = "easymq_consumer.properties";
	//异常sleep时间
	public static final int EXCEPTION_SLEEP_TIME = 1000;
	//topic默认的tag
	public static final String TOPIC_DEFAULT_TAG = "default";
	public static final String CONSUMER_INTERFACE_CLASSNAME = "com.lede.tech.easymq.consumer.handler.EasyMQRecMsgHandler";
	public static final String CONSUMER_GROUPNAME_SEP = "_";
	public static final String CONSUMER_ORDERLY_TRUE = "orderly";
	public static final String CONSUMER_BROADCAST_TRUE = "broadcast";

	//配置文件变量
	public static final String CONFIG_CONSUMER_SCANPACKAGE = "easymq.consumer.scanpackage";
	public static final String CONFIG_CONSUMER_SCANPACKAGE_DEFAULT = "com.lede";
	public static final String CONFIG_CONSUMER_NAMESERVER = "easymq.consumer.nameserver";
	public static final String CONFIG_CONSUMER_INSTANCENAME = "easymq.consumer.instancename";
	public static final String CONFIG_CONSUMER_TIMEOUT = "easymq.consumer.consumertimeoutminutes";
	public static final String CONFIG_PRODUCER_NAMESERVER = "easymq.producer.nameserver";
	public static final String CONFIG_PRODUCER_INSTANCENAME = "easymq.producer.instancename";
	public static final String CONFIG_PRODUCER_TOPICQUEUENUMS = "easymq.producer.topicqueuenums";
	public static final int CONFIG_PRODUCER_TOPICQUEUENUMS_DEFAULT = 8;
	public static final String CONFIG_PRODUCER_TIMEOUT = "easymq.producer.sendmsgtimeoutmillis";
	public static final String CONFIG_PRODUCER_GROUPNAME = "easymq.producer.groupname";

}
