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
import com.google.common.collect.Lists;
import com.netease.lottery.easymq.consumer.bean.MessageBean;
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
					boolean ok = StandarddealMessage(topicHandlers, msgs, log);
					if (ok)
					{
						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					}
					else
					{
						return ConsumeConcurrentlyStatus.RECONSUME_LATER;
					}
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
					boolean ok = StandarddealMessage(topicHandlers, msgs, log);
					if (ok)
					{
						return ConsumeOrderlyStatus.SUCCESS;
					}
					else
					{
						return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
					}

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

	private static boolean StandarddealMessage(Map<String, List<EasyMQRecMsgHandler>> topicHandlers,
			List<MessageExt> msgs, final Log log)
	{
		boolean ok = true;
		//该map的key为topic，value为该topic下的所有message
		Map<String, List<MessageExt>> topicMessage = msgs.stream().collect(Collectors.groupingBy(MessageExt::getTopic));
		for (Map.Entry<String, List<MessageExt>> entry : topicMessage.entrySet())
		{
			String topic = entry.getKey();
			//			List<String> messages = entry.getValue().stream().map(msg -> msg.getBody().toString())
			//					.collect(Collectors.toList());
			List<MessageBean> messages = Lists.newArrayList();
			for (MessageExt messageExt : entry.getValue())
			{
				MessageBean message = new MessageBean();
				message.setMessageExt(messageExt);
				message.setKey(messageExt.getKeys());
				message.setMessage(new String(messageExt.getBody()));
				messages.add(message);
			}
			List<EasyMQRecMsgHandler> handlers = topicHandlers.get(topic);
			for (EasyMQRecMsgHandler handler : handlers)
			{
				try
				{
					handler.handle(messages);
				}
				catch (Exception e)
				{
					log.fatal("easymq wrong. topic:" + topic + ",handler:" + handler.getClass().getName()
							+ "messageSize:" + messages.size() + ",messages:" + messages, e);
					ok = false;
				}
			}
		}
		return ok;
	}
}
