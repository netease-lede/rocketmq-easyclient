package com.netease.lottery.easymq.consumer.bean;

import java.util.List;
import java.util.Map;

import com.netease.lottery.easymq.consumer.handler.EasyMQRecMsgHandler;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
public class ConsumerConfigBean
{
	private Map<String, List<EasyMQRecMsgHandler>> topicHandler;
	private String groupName;
	private String group;
	private boolean isOrderly;
	private boolean isBroadcast;
	private int consumerThreadCountMin;
	private int consumerThreadCountMax;
	private int consumeMessageBatchMaxSize;

	public Map<String, List<EasyMQRecMsgHandler>> getTopicHandler()
	{
		return topicHandler;
	}

	public void setTopicHandler(Map<String, List<EasyMQRecMsgHandler>> topicHandler)
	{
		this.topicHandler = topicHandler;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public boolean isOrderly()
	{
		return isOrderly;
	}

	public void setOrderly(boolean isOrderly)
	{
		this.isOrderly = isOrderly;
	}

	public boolean isBroadcast()
	{
		return isBroadcast;
	}

	public void setBroadcast(boolean isBroadcast)
	{
		this.isBroadcast = isBroadcast;
	}

	public int getConsumerThreadCountMin()
	{
		return consumerThreadCountMin;
	}

	public void setConsumerThreadCountMin(int consumerThreadCountMin)
	{
		this.consumerThreadCountMin = consumerThreadCountMin;
	}

	public int getConsumerThreadCountMax()
	{
		return consumerThreadCountMax;
	}

	public void setConsumerThreadCountMax(int consumerThreadCountMax)
	{
		this.consumerThreadCountMax = consumerThreadCountMax;
	}

	public int getConsumeMessageBatchMaxSize()
	{
		return consumeMessageBatchMaxSize;
	}

	public void setConsumeMessageBatchMaxSize(int consumeMessageBatchMaxSize)
	{
		this.consumeMessageBatchMaxSize = consumeMessageBatchMaxSize;
	}

	@Override
	public String toString()
	{
		return "ConsumerConfigBean [topicHandler=" + topicHandler + ", groupName=" + groupName + ", group=" + group
				+ ", isOrderly=" + isOrderly + ", isBroadcast=" + isBroadcast + ", consumerThreadCountMin="
				+ consumerThreadCountMin + ", consumerThreadCountMax=" + consumerThreadCountMax
				+ ", consumeMessageBatchMaxSize=" + consumeMessageBatchMaxSize + "]";
	}

}
