package com.lede.tech.easymq.example.consumer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.lede.tech.easymq.consumer.bean.MessageBean;
import com.lede.tech.easymq.consumer.handler.EasyMQRecMsgHandler;
import com.lede.tech.easymq.consumer.handler.annotation.EasyMQConsumerMeta;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
@EasyMQConsumerMeta(topic = "topic20170119", group = "group1", isOrderly = true)
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
			//			if (keyInt % 3 == 0)
			//			{
			//				System.out.println(new Date());
			//				throw new RuntimeException();
			//			}
		}

	}
}
