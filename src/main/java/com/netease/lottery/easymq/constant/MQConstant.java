package com.netease.lottery.easymq.constant;

public class MQConstant
{
	//rocketMQ配置文件夹
	public static final String CONFIG_DIR = "MQConfig";
	//rocketMQ生产者默认配置文件名称
	public static final String DEFAULT_FILENAME = "mq_producer.properties";
	//rocketMQ消费者默认配置文件名称
	public static final String DEFAULT_CONSUMER_FILENAME = "mq_consumer.properties";
	//异常sleep时间
	public static final int EXCEPTION_SLEEP_TIME = 1000;
}
