package com.netease.lottery.easymq.consumer;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.netease.lottery.easymq.common.constant.MQConstant;
import com.netease.lottery.easymq.common.exception.MqConsumerConfigException;
import com.netease.lottery.easymq.consumer.bean.ConsumerConfigBean;
import com.netease.lottery.easymq.consumer.enums.ConsumerTransferMode;

public class EasyMQPushConsumer
{
	private Log LOG = LogFactory.getLog(EasyMQPushConsumer.class);

	private DefaultMQPushConsumer consumer;

	public EasyMQPushConsumer(Properties props)
	{
		init(props);
	}

	private void init(Properties props)
	{
		if (!checkVaild(props))
		{
			throw new MqConsumerConfigException("easymq wrong. consumer config properties error.");
		}
		consumer = new DefaultMQPushConsumer();
		consumer.setNamesrvAddr(props.getProperty(MQConstant.CONFIG_CONSUMER_NAMESERVER));
		if (!StringUtils.isEmpty(props.getProperty(MQConstant.CONFIG_CONSUMER_INSTANCENAME)))
		{
			consumer.setInstanceName(props.getProperty(MQConstant.CONFIG_CONSUMER_INSTANCENAME));
		}
	}

	private boolean checkVaild(Properties props)
	{
		String nameserver = props.getProperty(MQConstant.CONFIG_CONSUMER_NAMESERVER);
		if (!nameserver.matches("[0-9.:;]+"))
		{
			return false;
		}
		return true;
	}

	public void loadConfigBean(ConsumerConfigBean consumerConfigBean) throws MQClientException
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
