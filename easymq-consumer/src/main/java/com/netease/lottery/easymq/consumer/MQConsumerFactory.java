package com.netease.lottery.easymq.consumer;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netease.lottery.easymq.consumer.bean.MQConsumerConfigBean;

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

	public MQPushConsumer getMQConsumer(Properties prop, MQConsumerConfigBean consumerConfigBean)
	{
		MQPushConsumer mqPushConsumer = null;
		try
		{
			mqPushConsumer = new MQPushConsumer(prop, consumerConfigBean);
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
