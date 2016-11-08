package com.netease.lottery.easymq.consumer.handler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MQConsumerMeta
{
	String topic();

	String group();

	boolean isOrderly() default false;

	boolean isBroadcast() default false;

	int consumerThreadCount() default 20;

}
