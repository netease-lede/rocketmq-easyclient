package com.netease.lottery.easymq.example.consumer;

import java.util.List;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.netease.lottery.easymq.consumer.handler.EasyMQRecMsgHandler;
import com.netease.lottery.easymq.consumer.handler.annotation.EasyMQConsumerMeta;

@EasyMQConsumerMeta(topic = "topic20161114", group = "group1")
public class PaySuccessMQHandler implements EasyMQRecMsgHandler
{

	public void handle(List<MessageExt> msg) throws Exception
	{

		for (MessageExt message : msg)
		{
			System.out.println("PaySuccessMQHandler" + message);
			String body = new String(message.getBody());
			System.out.println("body:" + body);
		}

	}
}
