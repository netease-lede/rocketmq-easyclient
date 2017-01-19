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
	/*
	 * 发送模式 同步、异步、oneway
	 */
	private ProducerTransferMode transferMode;
	/**
	 * topic 主题
	 */
	private String topic;
	/**
	 * tags 主题下的标签，暂统一为default
	 */
	private String tags;
	/**
	 * keys 消息对应的key，建议每个消息有唯一的key
	 */
	private String keys;
	/**
	 * message 消息具体的文本形式
	 */
	private String message;
	/**
	 * charSet 编码
	 */
	private String charSet;
	/**
	 * callback 异步调用时，使用的回调函数
	 */
	private SendCallback callback;
	/**
	 * orderTag 顺序消息需要的参数，该参数相同值下的消息保证有序
	 */
	private String orderTag;
	/**
	 * delayLevel 延迟发送级别
	 */
	private EasyMQMessageDelayLevel delayLevel;

	public EasyMQMessageConfig(String topic, String keys, String message)
	{
		this.topic = topic;
		this.keys = keys;
		this.message = message;
		this.transferMode = ProducerTransferMode.SYNC;
		this.tags = MQConstant.TOPIC_DEFAULT_TAG;
		this.charSet = RemotingHelper.DEFAULT_CHARSET;
		this.callback = null;
		this.orderTag = "";
		this.delayLevel = null;
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

	public EasyMQMessageDelayLevel getDelayLevel()
	{
		return delayLevel;
	}

	public void setDelayLevel(EasyMQMessageDelayLevel delayLevel)
	{
		this.delayLevel = delayLevel;
	}

	@Override
	public String toString()
	{
		return "EasyMQMessageConfig [transferMode=" + transferMode + ", topic=" + topic + ", tags=" + tags + ", keys="
				+ keys + ", message=" + message + ", charSet=" + charSet + ", callback=" + callback + ", orderTag="
				+ orderTag + ", delayLevel=" + delayLevel + "]";
	}

}
