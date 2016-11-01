package com.netease.lottery.easymq.assist;

import java.util.List;

import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;

public class StandardMessageQueueSelector implements MessageQueueSelector
{

	public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg)
	{
		Integer id = ((String) arg).hashCode();
		int index = id % mqs.size();
		return mqs.get(index);
	}

}
