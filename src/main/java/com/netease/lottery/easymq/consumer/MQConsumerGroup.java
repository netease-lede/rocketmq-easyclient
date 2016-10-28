package com.netease.lottery.easymq.consumer;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netease.lottery.easymq.handler.MQRecMsgHandler;

public class MQConsumerGroup
{
	private Log LOG = LogFactory.getLog(MQConsumerGroup.class);
	/**
	 * 消费组名称
	 */
	private String consumerGroupName;
	/**
	 * 消费者数量
	 */
	private Integer consumerNumber;
	/**
	 * 消费端处理handler map
	 */
	private Map<String, List<MQRecMsgHandler>> handlerMap = Maps.newHashMap();
	/**
	 * 消息监听器
	 */
	private MessageListenerConcurrently messageListenerConcurrently;
	/**
	 * 消费组配置文件名
	 */
	private String consumerConfigFileName;
	/**
	 * 消费组订阅的Topic列表
	 */
	private String[] topics;
	/**
	 * 消费者列表
	 */
	private List<MQPushConsumer> consumerList = Lists.newArrayList();

	public void initConsumerGroup() throws Exception
	{
		if (consumerNumber == null || consumerNumber <= 0)
		{
			throw new Exception("");
		}
		for (int index = 1; index <= consumerNumber; index++)
		{
			MQPushConsumer consumer = MQConsumerFactory.getFactory().getMQConsumer(consumerConfigFileName);
			consumer.start(topics, messageListenerConcurrently);
			consumerList.add(consumer);
		}
	}

	public void registerTopicHandler(String topic, MQRecMsgHandler handler)
	{
		LOG.info("easymq registerTopicHandler for topic:" + topic + ",handler:" + handler.getClass());
		List<MQRecMsgHandler> handlerList = handlerMap.get(topic);
		if (CollectionUtils.isEmpty(handlerList))
		{
			handlerList = Lists.newArrayList();
			handlerList.add(handler);
			handlerMap.put(topic, handlerList);
		}
		else
		{
			handlerList.add(handler);
		}
	}

	public String getConsumerGroupName()
	{
		return consumerGroupName;
	}

	public void setConsumerGroupName(String consumerGroupName)
	{
		this.consumerGroupName = consumerGroupName;
	}

	public Integer getConsumerNumber()
	{
		return consumerNumber;
	}

	public void setConsumerNumber(Integer consumerNumber)
	{
		this.consumerNumber = consumerNumber;
	}

	public String getConsumerConfigFileName()
	{
		return consumerConfigFileName;
	}

	public void setConsumerConfigFileName(String consumerConfigFileName)
	{
		this.consumerConfigFileName = consumerConfigFileName;
	}

	public Map<String, List<MQRecMsgHandler>> getHandlerMap()
	{
		return handlerMap;
	}

	public void setHandlerMap(Map<String, List<MQRecMsgHandler>> handlerMap)
	{
		this.handlerMap = handlerMap;
	}

	public MessageListenerConcurrently getMessageListenerConcurrently()
	{
		return messageListenerConcurrently;
	}

	public void setMessageListenerConcurrently()
	{
		this.messageListenerConcurrently = new MessageListenerConcurrently() {

			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context)
			{
				try
				{
					if (CollectionUtils.isEmpty(msgs))
					{
						LOG.info("推送的消息为空消息");
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					}
					String topic = msgs.get(0).getTopic();
					List<MQRecMsgHandler> handlerList = handlerMap.get(topic);
					if (CollectionUtils.isEmpty(handlerList))
					{
						throw new Exception("topic 不存在");
					}
					List<String> messageList = Lists.newArrayList();
					for (MessageExt msg : msgs)
					{
						messageList.add(msg.getBody().toString());
					}
					for (MQRecMsgHandler handler : handlerList)
					{
						handler.handle(messageList);
					}
				}
				catch (Exception e)
				{
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		};
	}

	public String[] getTopics()
	{
		return topics;
	}

	public void setTopics(String[] topics)
	{
		this.topics = topics;
	}
}
