package com.netease.lottery.easymq.consumer.bean;

import java.util.List;
import java.util.Map;

import com.netease.lottery.easymq.consumer.handler.MQRecMsgHandler;

public class MQConsumerConfigBean
{
	private Map<String, List<MQRecMsgHandler>> topicHandler;
	private String groupName;
	private String group;
	private boolean isOrderly;
	private boolean isBroadcast;
	private int consumerThreadCount;

	public Map<String, List<MQRecMsgHandler>> getTopicHandler()
	{
		return topicHandler;
	}

	public void setTopicHandler(Map<String, List<MQRecMsgHandler>> topicHandler)
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

	public int getConsumerThreadCount()
	{
		return consumerThreadCount;
	}

	public void setConsumerThreadCount(int consumerThreadCount)
	{
		this.consumerThreadCount = consumerThreadCount;
	}

	@Override
	public String toString()
	{
		return "MQConsumerConfigBean [topicHandler=" + topicHandler + ", groupName=" + groupName + ", group=" + group
				+ ", isOrderly=" + isOrderly + ", isBroadcast=" + isBroadcast + ", consumerThreadCount="
				+ consumerThreadCount + "]";
	}

}
