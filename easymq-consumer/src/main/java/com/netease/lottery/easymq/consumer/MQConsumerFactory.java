package com.netease.lottery.easymq.consumer;

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

	public MQPushConsumer getMQConsumer(String consumerConfigFileName)
	{
		MQPushConsumer mqPushConsumer = null;
		try
		{
			mqPushConsumer = new MQPushConsumer(consumerConfigFileName);
			LOG.info("#Load RocketMQ config:" + consumerConfigFileName);
		}
		catch (Exception e)
		{
			LOG.fatal("#Builder producer:" + consumerConfigFileName + " error.Cause:", e);
		}
		return mqPushConsumer;
	}
}
