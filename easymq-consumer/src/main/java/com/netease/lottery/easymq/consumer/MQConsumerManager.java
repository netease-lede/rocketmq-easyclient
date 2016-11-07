package com.netease.lottery.easymq.consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netease.lottery.easymq.common.constant.MQConstant;

public class MQConsumerManager
{
	private Log LOG = LogFactory.getLog(MQConsumerManager.class);

	public void init()
	{
		MQConsumerGroup consumerGroup = new MQConsumerGroup();
		consumerGroup.setConsumerConfigFileName(MQConstant.DEFAULT_CONSUMER_FILENAME);
		consumerGroup.setConsumerGroupName("defaultConsumerGroup");
		consumerGroup.setConsumerNumber(1);
		try
		{
			consumerGroup.initConsumerGroup();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
