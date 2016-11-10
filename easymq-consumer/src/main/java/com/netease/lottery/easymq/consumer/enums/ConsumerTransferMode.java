package com.netease.lottery.easymq.consumer.enums;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.netease.lottery.easymq.consumer.handler.MQRecMsgHandler;

public enum ConsumerTransferMode
{
	PUSH_CONCURRENTLY
	{
		@Override
		public void regestHandlers(DefaultMQPushConsumer consumer, Map<String, List<MQRecMsgHandler>> topicHandlers)
		{
			MessageListenerConcurrently listener = new MessageListenerConcurrently() {
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
						ConsumeConcurrentlyContext context)
				{
					Map<String, List<MessageExt>> topicMessage = msgs.stream()
							.collect(Collectors.groupingBy(MessageExt::getTopic));
					for (Map.Entry<String, List<MessageExt>> entry : topicMessage.entrySet())
					{
						String topic = entry.getKey();
						List<MessageExt> messages = entry.getValue();

					}
					return null;
				}

			};
		}
	},
	PUSH_ORDERLY
	{
		@Override
		public void regestHandlers(DefaultMQPushConsumer consumer, Map<String, List<MQRecMsgHandler>> topicHandlers)
		{

		}
	};

	private ConsumerTransferMode()
	{

	}

	public void regestHandlers(DefaultMQPushConsumer consumer, Map<String, List<MQRecMsgHandler>> topicHandlers)
	{

	}
}
