package com.netease.lottery.easymq.consumer;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.netease.lottery.easymq.consumer.bean.MQConsumerConfigBean;

public class MQPushConsumer
{
	private Log LOG = LogFactory.getLog(MQPushConsumer.class);

	private DefaultMQPushConsumer consumer;

	public MQPushConsumer(Properties props, MQConsumerConfigBean consumerConfigBean)
	{
		init(props, consumerConfigBean);
	}

	private void init(Properties props, MQConsumerConfigBean consumerConfigBean)
	{
		try
		{
			consumer = new DefaultMQPushConsumer("DefaultConsumerGroup");
			consumer.setNamesrvAddr(props.getProperty("easymq.consumer.name.server"));
			consumer.setInstanceName(props.getProperty("easymq.consumer.instance.name"));
			LOG.info(props.getProperty("rocket.instance.name") + " started success");
		}
		catch (Exception e)
		{
			LOG.fatal("create RocketMQ producer error!use default setting!", e);
			return;
		}
	}

	public void start(String[] topics, MessageListenerConcurrently messageListenerConcurrently) throws Exception
	{
		if (topics == null || topics.length <= 0 || messageListenerConcurrently == null)
		{
			throw new Exception("");
		}
		for (String topic : topics)
		{
			consumer.subscribe(topic, "*");
		}
		consumer.registerMessageListener(messageListenerConcurrently);
		consumer.start();
	}

	public void shutdown()
	{
		consumer.shutdown();
	}
}
