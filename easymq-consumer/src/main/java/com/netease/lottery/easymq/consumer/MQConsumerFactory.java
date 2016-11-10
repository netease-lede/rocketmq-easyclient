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

	public MQPushConsumer getMQConsumer(Properties prop)
	{
		MQPushConsumer mqPushConsumer = null;
		try
		{
			mqPushConsumer = new MQPushConsumer(prop);
			LOG.info("#Load RocketMQ config:" + prop);
		}
		catch (Exception e)
		{
			LOG.fatal("#Builder producer:" + prop + " error.Cause:", e);
			throw e;
		}
		return mqPushConsumer;
	}
}
