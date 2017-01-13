package com.netease.lottery.easymq.example.consumer;

import java.util.List;

import com.netease.lottery.easymq.consumer.bean.MessageBean;
import com.netease.lottery.easymq.consumer.handler.EasyMQRecMsgHandler;
import com.netease.lottery.easymq.consumer.handler.annotation.EasyMQConsumerMeta;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
@EasyMQConsumerMeta(topic = "topic20161116", group = "group2")
public class TestMQHandler2 implements EasyMQRecMsgHandler
{

	public void handle(List<MessageBean> msg) throws Exception
	{

		for (MessageBean message : msg)
		{
			System.out.println("group2 PaySuccessMQHandler" + message);
			System.out.println("group2 key:" + message.getKey());
			System.out.println("group2 message:" + message.getMessage());
		}

	}
}
