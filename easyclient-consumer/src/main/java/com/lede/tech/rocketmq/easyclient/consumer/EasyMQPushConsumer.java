package com.lede.tech.rocketmq.easyclient.consumer;

import java.util.Properties;

import com.lede.tech.rocketmq.easyclient.consumer.enums.ConsumerTransferMode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.util.StringUtils;

import com.lede.tech.rocketmq.easyclient.common.constant.MQConstant;
import com.lede.tech.rocketmq.easyclient.common.exception.MqConsumerConfigException;
import com.lede.tech.rocketmq.easyclient.consumer.bean.ConsumerConfigBean;

/**
 * 
 * @Desc 
 * @Author bjguosong
 * @Author ykhu
 */
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
		String timeout = props.getProperty(MQConstant.CONFIG_CONSUMER_TIMEOUT);
		if (!StringUtils.isEmpty(timeout))
		{
			consumer.setConsumeTimeout(Long.parseLong(timeout));
		}
	}

	private boolean checkVaild(Properties props)
	{
		String nameserver = props.getProperty(MQConstant.CONFIG_CONSUMER_NAMESERVER);
		if (!nameserver.matches("[0-9.:;]+"))
		{
			return false;
		}
		String timeout = props.getProperty(MQConstant.CONFIG_CONSUMER_TIMEOUT);
		if (!StringUtils.isEmpty(timeout) && !timeout.matches("[0-9]+"))
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
		consumer.setConsumeMessageBatchMaxSize(consumerConfigBean.getConsumeMessageBatchMaxSize());
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
