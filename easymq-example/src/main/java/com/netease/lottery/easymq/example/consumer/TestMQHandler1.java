package com.netease.lottery.easymq.example.consumer;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netease.lottery.easymq.consumer.bean.MessageBean;
import com.netease.lottery.easymq.consumer.handler.EasyMQRecMsgHandler;
import com.netease.lottery.easymq.consumer.handler.annotation.EasyMQConsumerMeta;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
@EasyMQConsumerMeta(topic = "topic20161119", group = "group1")
public class TestMQHandler1 implements EasyMQRecMsgHandler
{
	private volatile static AtomicInteger count = new AtomicInteger(0);

	public void handle(List<MessageBean> msg) throws Exception
	{

		for (MessageBean message : msg)
		{
			int nowCount = count.incrementAndGet();
			int keyInt = Integer.parseInt(message.getKey());
			System.out.println("group1 PaySuccessMQHandler" + message);
			System.out.println("group1 key:" + message.getKey());
			System.out.println("group1 message:" + message.getMessage());
			if (keyInt % 3 == 0)
			{
				System.out.println(new Date());
				throw new RuntimeException();
			}
		}

	}
}
