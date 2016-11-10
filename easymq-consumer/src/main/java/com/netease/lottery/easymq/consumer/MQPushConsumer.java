package com.netease.lottery.easymq.consumer;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.netease.lottery.easymq.consumer.bean.MQConsumerConfigBean;

public class MQPushConsumer
{
	private Log LOG = LogFactory.getLog(MQPushConsumer.class);

	private DefaultMQPushConsumer consumer;

	public MQPushConsumer(Properties props)
	{
		init(props);
	}

	private void init(Properties props)
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

	public void loadConfigBean(MQConsumerConfigBean consumerConfigBean)
	{
		//TODO
		consumer.setConsumerGroup(consumerConfigBean.getGroupName());
		consumer.setConsumeThreadMin(consumerConfigBean.getConsumerThreadCountMin());
		consumer.setConsumeThreadMax(consumerConfigBean.getConsumerThreadCountMax());
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

	public void start() throws MQClientException
	{
		consumer.start();
	}

	public void shutdown()
	{
		consumer.shutdown();
	}
}
