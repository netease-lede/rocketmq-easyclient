package com.netease.lottery.easymq.consumer.handler;

import java.util.List;

@MQConsumerAnnotation(topic = "paySuccessTopic", tags = "*")
public class PaySuccessMQHandler implements MQRecMsgHandler
{
	public void handle(List<String> msg) throws Exception
	{
		System.out.println(msg);
	}
}
