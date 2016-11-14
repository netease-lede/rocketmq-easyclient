package com.netease.lottery.easymq.consumer.enums;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.netease.lottery.easymq.consumer.handler.EasyMQRecMsgHandler;

public enum ConsumerTransferMode
{

	PUSH_CONCURRENTLY
	{
		@Override
		public void regestHandlers(DefaultMQPushConsumer consumer, Map<String, List<EasyMQRecMsgHandler>> topicHandlers)
		{
			final Log log = LogFactory.getLog(ConsumerTransferMode.class);
			MessageListenerConcurrently listener = new MessageListenerConcurrently() {
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
						ConsumeConcurrentlyContext context)
				{
					StandarddealMessage(topicHandlers, msgs, log);
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}

			};
			consumer.setMessageListener(listener);
		}

	},
	PUSH_ORDERLY
	{
		@Override
		public void regestHandlers(DefaultMQPushConsumer consumer, Map<String, List<EasyMQRecMsgHandler>> topicHandlers)
		{
			final Log log = LogFactory.getLog(ConsumerTransferMode.class);
			MessageListenerOrderly listener = new MessageListenerOrderly() {

				@Override
				public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context)
				{
					StandarddealMessage(topicHandlers, msgs, log);
					return ConsumeOrderlyStatus.SUCCESS;
				}
			};
			consumer.setMessageListener(listener);
		}

	};
	private ConsumerTransferMode()
	{

	}

	public void regestHandlers(DefaultMQPushConsumer consumer, Map<String, List<EasyMQRecMsgHandler>> topicHandlers)
	{

	}

	private static void StandarddealMessage(Map<String, List<EasyMQRecMsgHandler>> topicHandlers, List<MessageExt> msgs,
			final Log log)
	{
		//该map的key为topic，value为该topic下的所有message
		Map<String, List<MessageExt>> topicMessage = msgs.stream().collect(Collectors.groupingBy(MessageExt::getTopic));
		for (Map.Entry<String, List<MessageExt>> entry : topicMessage.entrySet())
		{
			String topic = entry.getKey();
			//			List<String> messages = entry.getValue().stream().map(msg -> msg.getBody().toString())
			//					.collect(Collectors.toList());
			List<MessageExt> messages = entry.getValue().stream().map(msg -> msg).collect(Collectors.toList());
			List<EasyMQRecMsgHandler> handlers = topicHandlers.get(topic);
			for (EasyMQRecMsgHandler handler : handlers)
			{
				try
				{
					handler.handle(messages);
				}
				catch (Exception e)
				{
					log.fatal("easymq wrong. topic:" + topic + ",handler:" + handler.getClass().getName(), e);
				}
			}
		}
	}
}
