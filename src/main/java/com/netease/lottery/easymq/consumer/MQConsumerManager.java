package com.netease.lottery.easymq.consumer;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netease.lottery.easymq.handler.MQRecMsgHandler;

public class MQConsumerManager
{
	private Log LOG = LogFactory.getLog(MQConsumerManager.class);
	private MQConsumerFactory mqConsumerFactory;

	private Map<String, MQRecMsgHandler> handlerMap = Maps.newHashMap();

	public void init() throws MQClientException
	{
		consumer.setNamesrvAddr("127.0.0.1:9876");
		consumer.setInstanceName("consumer");
		consumer.subscribe("TestTopic", "*");
		consumer.registerMessageListener(new MessageListenerConcurrently() {

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
					MQRecMsgHandler handler = handlerMap.get(topic);
					if (handler == null)
					{
						throw new Exception("topic 不存在");
					}
					List<String> messageList = Lists.newArrayList();
					for (MessageExt msg : msgs)
					{
						messageList.add(msg.getBody().toString());
					}
					handler.handle(messageList);
				}
				catch (Exception e)
				{
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
	}

	public void registerTopicHandler(String topic, MQRecMsgHandler handler)
	{
		LOG.info("easymq registerTopicHandler for topic:" + topic + ",handler:" + handler.getClass());
		handlerMap.put(topic, handler);
	}

}
