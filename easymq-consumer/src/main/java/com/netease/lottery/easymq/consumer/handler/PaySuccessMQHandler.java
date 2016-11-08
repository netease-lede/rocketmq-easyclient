package com.netease.lottery.easymq.consumer.handler;

import java.util.List;

import com.netease.lottery.easymq.consumer.handler.annotation.MQConsumerMeta;

@MQConsumerMeta(topic = "paySuccessTopic", group = "group1")
public class PaySuccessMQHandler implements MQRecMsgHandler
{
	public void handle(List<String> msg) throws Exception
	{
		System.out.println(msg);
	}
}
