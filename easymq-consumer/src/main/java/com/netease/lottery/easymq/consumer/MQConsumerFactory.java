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
		super();
	}

	public static MQConsumerFactory getFactory()
	{
		if (factory == null)
		{
			synchronized (MQConsumerFactory.class)
			{
				if (factory == null)
				{
					factory = new MQConsumerFactory();
				}
			}
		}
		return factory;
	}

	public MQPushConsumer getMQConsumer(Properties prop)
	{
		MQPushConsumer mqPushConsumer = null;
		try
		{
			mqPushConsumer = new MQPushConsumer(prop);
			LOG.info("easymq load config:" + prop);
		}
		catch (Exception e)
		{
			LOG.fatal("easymq wrong. Builder consumer:" + prop + " error.Cause:", e);
			throw e;
		}
		return mqPushConsumer;
	}
}
