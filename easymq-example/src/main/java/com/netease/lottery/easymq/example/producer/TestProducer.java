package com.netease.lottery.easymq.example.producer;

import com.netease.lottery.easymq.common.exception.MqBussinessException;
import com.netease.lottery.easymq.common.exception.MqWapperException;
import com.netease.lottery.easymq.producer.EasyMQProducer;
import com.netease.lottery.easymq.producer.EasyMQProducerFactory;

public class TestProducer
{
	public static void main(String[] args)
	{
		EasyMQProducer producer = EasyMQProducerFactory.getProducer();
		try
		{
			//producer.sendMsg("topic20161116", "order10", "order10detail");
			System.out.println("begin");
			long begin = System.currentTimeMillis();
			for (int i = 10; i <= 14; i++)
			{
				producer.sendMsgOrderly("topic20161119", i + "", i + "detail", "onlyyou");
			}
			System.out.println("send,taking" + (System.currentTimeMillis() - begin) + "ms");
		}
		catch (MqWapperException e)
		{
			e.printStackTrace();
		}
		catch (MqBussinessException e)
		{
			e.printStackTrace();
		}
	}
}
