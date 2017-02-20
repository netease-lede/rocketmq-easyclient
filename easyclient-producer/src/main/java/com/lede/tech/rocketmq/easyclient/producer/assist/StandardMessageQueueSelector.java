package com.lede.tech.rocketmq.easyclient.producer.assist;

import java.util.List;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
public class StandardMessageQueueSelector implements MessageQueueSelector
{

	public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg)
	{
		Integer id = Math.abs(((String) arg).hashCode());
		int index = id % mqs.size();
		return mqs.get(index);
	}

}
