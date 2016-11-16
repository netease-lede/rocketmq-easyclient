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
			producer.sendMsgOrderly("topic20161116", "order8", "order10detail", "onlyyou");
			System.out.println("send");
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
