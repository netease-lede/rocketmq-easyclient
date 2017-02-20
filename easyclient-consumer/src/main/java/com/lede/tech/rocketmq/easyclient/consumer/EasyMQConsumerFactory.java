package com.lede.tech.rocketmq.easyclient.consumer;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @Desc 
 * @Author bjguosong
 */
public class EasyMQConsumerFactory
{
	private Log LOG = LogFactory.getLog(EasyMQConsumerFactory.class);

	private static EasyMQConsumerFactory factory;

	private EasyMQConsumerFactory()
	{
		super();
	}

	public static EasyMQConsumerFactory getFactory()
	{
		if (factory == null)
		{
			synchronized (EasyMQConsumerFactory.class)
			{
				if (factory == null)
				{
					factory = new EasyMQConsumerFactory();
				}
			}
		}
		return factory;
	}

	public EasyMQPushConsumer getMQConsumer(Properties prop)
	{
		EasyMQPushConsumer mqPushConsumer = null;
		try
		{
			mqPushConsumer = new EasyMQPushConsumer(prop);
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
