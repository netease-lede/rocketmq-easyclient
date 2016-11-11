package com.netease.lottery.easymq.consumer;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.netease.lottery.easymq.common.constant.MQConstant;
import com.netease.lottery.easymq.consumer.bean.MQConsumerConfigBean;
import com.netease.lottery.easymq.consumer.enums.ConsumerTransferMode;

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

	public void loadConfigBean(MQConsumerConfigBean consumerConfigBean) throws MQClientException
	{
		consumer.setConsumerGroup(consumerConfigBean.getGroupName());
		consumer.setConsumeThreadMin(consumerConfigBean.getConsumerThreadCountMin());
		consumer.setConsumeThreadMax(consumerConfigBean.getConsumerThreadCountMax());
		for (String topic : consumerConfigBean.getTopicHandler().keySet())
		{
			consumer.subscribe(topic, MQConstant.TOPIC_DEFAULT_TAG);
		}
		if (consumerConfigBean.isBroadcast())
		{
			consumer.setMessageModel(MessageModel.BROADCASTING);
			ConsumerTransferMode.PUSH_CONCURRENTLY.regestHandlers(consumer, consumerConfigBean.getTopicHandler());
		}
		else
		{
			if (consumerConfigBean.isOrderly())
			{
				ConsumerTransferMode.PUSH_ORDERLY.regestHandlers(consumer, consumerConfigBean.getTopicHandler());
			}
			else
			{
				ConsumerTransferMode.PUSH_CONCURRENTLY.regestHandlers(consumer, consumerConfigBean.getTopicHandler());
			}
		}
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
