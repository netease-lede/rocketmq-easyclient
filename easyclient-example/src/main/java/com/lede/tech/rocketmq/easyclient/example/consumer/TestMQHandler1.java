package com.lede.tech.rocketmq.easyclient.example.consumer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.lede.tech.rocketmq.easyclient.consumer.bean.MessageBean;
import com.lede.tech.rocketmq.easyclient.consumer.handler.EasyMQRecMsgHandler;
import com.lede.tech.rocketmq.easyclient.consumer.handler.annotation.EasyMQConsumerMeta;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
@EasyMQConsumerMeta(topic = "topic20170119", group = "group1", consumeMessageBatchMaxSize = 10, consumerThreadCountMin = 1, consumerThreadCountMax = 1)
public class TestMQHandler1 implements EasyMQRecMsgHandler
{
	private volatile static AtomicInteger count = new AtomicInteger(0);
	private volatile static AtomicInteger total = new AtomicInteger(0);

	public void handle(List<MessageBean> msg) throws Exception
	{
		int nowCount = count.incrementAndGet();
		for (MessageBean message : msg)
		{
			int total2 = total.incrementAndGet();

			//int keyInt = Integer.parseInt(message.getKey());
			System.out.println("group1 PaySuccessMQHandler" + message);
			System.out.println("group1 key:" + message.getKey());
			System.out.println("group1 message:" + message.getMessage());
			System.out.println("group1 nowCount:" + nowCount);
			System.out.println("group1 total2:" + total2);
			//			if (keyInt % 3 == 0)
			//			{
			//				System.out.println(new Date());
			//				throw new RuntimeException();
			//			}
		}

	}
}
