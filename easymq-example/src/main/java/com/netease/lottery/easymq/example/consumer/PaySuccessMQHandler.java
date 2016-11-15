package com.netease.lottery.easymq.example.consumer;

import java.util.List;

import com.netease.lottery.easymq.consumer.bean.MessageBean;
import com.netease.lottery.easymq.consumer.handler.EasyMQRecMsgHandler;
import com.netease.lottery.easymq.consumer.handler.annotation.EasyMQConsumerMeta;

@EasyMQConsumerMeta(topic = "topic20161114", group = "group1")
public class PaySuccessMQHandler implements EasyMQRecMsgHandler
{

	public void handle(List<MessageBean> msg) throws Exception
	{

		for (MessageBean message : msg)
		{
			System.out.println("PaySuccessMQHandler" + message);
			System.out.println("key:" + message.getKey());
			System.out.println("message:" + message.getMessage());
		}

	}
}
