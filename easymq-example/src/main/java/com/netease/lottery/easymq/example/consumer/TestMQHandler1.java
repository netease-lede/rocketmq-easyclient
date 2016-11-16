package com.netease.lottery.easymq.example.consumer;

import java.util.List;

import com.netease.lottery.easymq.consumer.bean.MessageBean;
import com.netease.lottery.easymq.consumer.handler.EasyMQRecMsgHandler;
import com.netease.lottery.easymq.consumer.handler.annotation.EasyMQConsumerMeta;

@EasyMQConsumerMeta(topic = "topic20161116", group = "group1")
public class TestMQHandler1 implements EasyMQRecMsgHandler
{

	public void handle(List<MessageBean> msg) throws Exception
	{

		for (MessageBean message : msg)
		{
			System.out.println("group1 PaySuccessMQHandler" + message);
			System.out.println("group1 key:" + message.getKey());
			System.out.println("group1 message:" + message.getMessage());
		}

	}
}
