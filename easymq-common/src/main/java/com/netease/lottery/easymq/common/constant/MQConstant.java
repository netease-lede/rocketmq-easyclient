package com.netease.lottery.easymq.common.constant;

public class MQConstant
{
	//rocketMQ配置文件夹
	public static final String CONFIG_DIR = "easymqconfig";
	//rocketMQ生产者默认配置文件名称
	public static final String DEFAULT_FILENAME = "easymq_producer.properties";
	//rocketMQ消费者默认配置文件名称
	public static final String DEFAULT_CONSUMER_FILENAME = "easymq_consumer.properties";
	//异常sleep时间
	public static final int EXCEPTION_SLEEP_TIME = 1000;
	//topic默认的tag
	public static final String TOPIC_DEFAULT_TAG = "default";
	public static final String CONSUMER_INTERFACE_CLASSNAME = "com.netease.lottery.easymq.consumer.handler.MQRecMsgHandler";
	public static final String CONSUMER_GROUPNAME_SEP = "_";
	public static final String CONSUMER_ORDERLY_TRUE = "orderly";
	public static final String CONSUMER_BROADCAST_TRUE = "broadcast";

	//配置文件变量
	public static final String CONFIG_CONSUMER_SCANPACKAGE = "easymq.consumer.scanpackage";
	public static final String CONFIG_CONSUMER_SCANPACKAGE_DEFAULT = "com.netease";
}
