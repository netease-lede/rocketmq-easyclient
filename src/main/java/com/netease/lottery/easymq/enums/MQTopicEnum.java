package com.netease.lottery.easymq.enums;

public enum MQTopicEnum
{
	PAY_SUCCESS_TOPIC("paySuccessTopic", true);

	private MQTopicEnum(String topic, boolean isRecMsg)
	{
		this.topic = topic;
		this.isRecMsg = isRecMsg;
	}

	private String topic;
	private boolean isRecMsg;

	public String getTopic()
	{
		return topic;
	}

	public void setTopic(String topic)
	{
		this.topic = topic;
	}

	public boolean isRecMsg()
	{
		return isRecMsg;
	}

	public void setRecMsg(boolean isRecMsg)
	{
		this.isRecMsg = isRecMsg;
	}
}
