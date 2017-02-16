package com.lede.tech.easymq.consumer.handler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Desc 
 * @Author bjguosong
 * @Author ykhu
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyMQConsumerMeta
{
	//订阅主题
	String topic();

	//分组，消息按组进行消费
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
