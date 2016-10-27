package com.netease.lottery.easymq.handler;

import java.util.List;

import com.netease.lottery.easymq.enums.MQTopicEnum;

public class PaySuccessMQHandler implements MQRecMsgHandler
{
	private String topic;
	private MQRecMsgHandler handler;

	public void handle(List<String> msg) throws Exception
	{
		System.out.println(msg);
	}

	public void registerHandler(String topic, MQRecMsgHandler handler)
	{
		this.topic = MQTopicEnum.PAY_SUCCESS_TOPIC.getTopic();
		this.handler = this;
	}

	public String getTopic()
	{
		return topic;
	}

	public void setTopic(String topic)
	{
		this.topic = topic;
	}

	public MQRecMsgHandler getHandler()
	{
		return handler;
	}

	public void setHandler(MQRecMsgHandler handler)
	{
		this.handler = handler;
	}
}
