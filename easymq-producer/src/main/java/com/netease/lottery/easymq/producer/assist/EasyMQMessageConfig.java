package com.netease.lottery.easymq.producer.assist;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import com.netease.lottery.easymq.common.constant.MQConstant;
import com.netease.lottery.easymq.producer.enums.ProducerTransferMode;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
public class EasyMQMessageConfig
{
	private ProducerTransferMode transferMode;
	private String topic;
	//该参数暂不使用，统一为default
	private String tags;
	private String keys;
	private String message;
	private String charSet;
	private SendCallback callback;
	private String orderTag;

	public EasyMQMessageConfig(String topic, String keys, String message)
	{
		this.transferMode = ProducerTransferMode.SYNC;
		this.tags = MQConstant.TOPIC_DEFAULT_TAG;
		this.charSet = RemotingHelper.DEFAULT_CHARSET;
		this.callback = null;
		this.orderTag = "";
	}

	public ProducerTransferMode getTransferMode()
	{
		return transferMode;
	}

	public void setTransferMode(ProducerTransferMode transferMode)
	{
		this.transferMode = transferMode;
	}

	public String getTopic()
	{
		return topic;
	}

	public void setTopic(String topic)
	{
		this.topic = topic;
	}

	public String getKeys()
	{
		return keys;
	}

	public void setKeys(String keys)
	{
		this.keys = keys;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getTags()
	{
		return tags;
	}

	public String getCharSet()
	{
		return charSet;
	}

	public void setCharSet(String charSet)
	{
		this.charSet = charSet;
	}

	public SendCallback getCallback()
	{
		return callback;
	}

	public void setCallback(SendCallback callback)
	{
		this.callback = callback;
	}

	public String getOrderTag()
	{
		return orderTag;
	}

	public void setOrderTag(String orderTag)
	{
		this.orderTag = orderTag;
	}

	@Override
	public String toString()
	{
		return "EasyMQMessageConfig [transferMode=" + transferMode + ", topic=" + topic + ", tags=" + tags + ", keys="
				+ keys + ", message=" + message + ", charSet=" + charSet + ", callback=" + callback + ", orderTag="
				+ orderTag + "]";
	}
}
