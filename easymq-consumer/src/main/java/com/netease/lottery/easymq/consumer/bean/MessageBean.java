package com.netease.lottery.easymq.consumer.bean;

import org.apache.rocketmq.common.message.MessageExt;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
public class MessageBean
{
	private MessageExt messageExt;
	private String key;
	private String message;

	public MessageExt getMessageExt()
	{
		return messageExt;
	}

	public void setMessageExt(MessageExt messageExt)
	{
		this.messageExt = messageExt;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	@Override
	public String toString()
	{
		return "MessageBean [messageExt=" + messageExt + ", key=" + key + ", message=" + message + "]";
	}
}
