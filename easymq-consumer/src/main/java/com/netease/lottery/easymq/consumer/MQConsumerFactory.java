package com.netease.lottery.easymq.consumer;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MQConsumerFactory
{
	private Log LOG = LogFactory.getLog(MQConsumerFactory.class);

	private static MQConsumerFactory factory;

	private MQConsumerFactory()
	{
	}

	public static MQConsumerFactory getFactory()
	{
		return factory;
	}

	public MQPushConsumer getMQConsumer(Properties consumerConfig)
	{
		MQPushConsumer mqPushConsumer = null;
		try
		{
			mqPushConsumer = new MQPushConsumer(consumerConfig);
			LOG.info("#Load RocketMQ config:" + consumerConfig);
		}
		catch (Exception e)
		{
			LOG.fatal("#Builder producer:" + consumerConfig + " error.Cause:", e);
		}
		return mqPushConsumer;
	}
}
