package com.netease.lottery.easymq.producer.enums;

import java.util.Objects;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.netease.lottery.easymq.constant.MQConstant;
import com.netease.lottery.easymq.exception.MqBussinessException;
import com.netease.lottery.easymq.exception.MqWapperException;
import com.netease.lottery.easymq.producer.assist.StandardMessageQueueSelector;

public enum TransferModeEnum
{
	SYNC
	{
		@Override
		public void sendMsg(DefaultMQProducer producer, String topic, String tags, String keys, String msg,
				String charset, SendCallback callback) throws MqWapperException, MqBussinessException
		{
			try
			{
				Message message = new Message(topic, tags, keys, msg.getBytes(charset));
				SendResult sendResult = producer.send(message);
				SendStatus sendStatus = sendResult.getSendStatus();
				if (!Objects.equals(sendStatus, SendStatus.SEND_OK))
				{
					throw new MqBussinessException(sendStatus.name());
				}
			}
			catch (Exception e)
			{
				try
				{
					Thread.sleep(MQConstant.EXCEPTION_SLEEP_TIME);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
				throw new MqWapperException(e);
			}
		}

		@Override
		public void sendMsgOrderly(DefaultMQProducer producer, String topic, String tags, String keys, String msg,
				String orderTag, String charset, SendCallback callback) throws MqBussinessException, MqWapperException
		{
			try
			{
				Message message = new Message(topic, tags, keys, msg.getBytes(charset));
				SendResult sendResult = producer.send(message, new StandardMessageQueueSelector(), orderTag);
				SendStatus sendStatus = sendResult.getSendStatus();
				if (!Objects.equals(sendStatus, SendStatus.SEND_OK))
				{
					throw new MqBussinessException(sendStatus.name());
				}
			}
			catch (Exception e)
			{
				try
				{
					Thread.sleep(MQConstant.EXCEPTION_SLEEP_TIME);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
				throw new MqWapperException(e);
			}
		}
	},
	ASYNC
	{
		@Override
		public void sendMsg(DefaultMQProducer producer, String topic, String tags, String keys, String msg,
				String charset, SendCallback callback) throws MqWapperException, MqBussinessException
		{
			try
			{
				Message message = new Message(topic, tags, keys, msg.getBytes(charset));
				producer.send(message, callback);
			}
			catch (Exception e)
			{
				try
				{
					Thread.sleep(MQConstant.EXCEPTION_SLEEP_TIME);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
				throw new MqWapperException(e);
			}
		}

		@Override
		public void sendMsgOrderly(DefaultMQProducer producer, String topic, String tags, String keys, String msg,
				String orderTag, String charset, SendCallback callback) throws MqBussinessException, MqWapperException
		{
			try
			{
				Message message = new Message(topic, tags, keys, msg.getBytes(charset));
				producer.send(message, new StandardMessageQueueSelector(), orderTag, callback);
			}
			catch (Exception e)
			{
				try
				{
					Thread.sleep(MQConstant.EXCEPTION_SLEEP_TIME);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
				throw new MqWapperException(e);
			}
		}
	},
	ONEWAY

	{

		@Override
		public void sendMsg(DefaultMQProducer producer, String topic, String tags, String keys, String msg,
				String charset, SendCallback callback) throws MqWapperException, MqBussinessException
		{
			try
			{
				Message message = new Message(topic, tags, keys, msg.getBytes(charset));
				producer.sendOneway(message);
			}
			catch (Exception e)
			{
				try
				{
					Thread.sleep(MQConstant.EXCEPTION_SLEEP_TIME);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
				throw new MqWapperException(e);
			}
		}

		@Override
		public void sendMsgOrderly(DefaultMQProducer producer, String topic, String tags, String keys, String msg,
				String orderTag, String charset, SendCallback callback) throws MqBussinessException, MqWapperException
		{
			try
			{
				Message message = new Message(topic, tags, keys, msg.getBytes(charset));
				producer.sendOneway(message, new StandardMessageQueueSelector(), orderTag);
			}
			catch (Exception e)
			{
				try
				{
					Thread.sleep(MQConstant.EXCEPTION_SLEEP_TIME);
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
				throw new MqWapperException(e);
			}
		}
	};

	private TransferModeEnum()
	{
	}

	public void sendMsg(DefaultMQProducer producer, String topic, String tags, String keys, String msg, String charset,
			SendCallback callback) throws MqWapperException, MqBussinessException
	{

	}

	public void sendMsgOrderly(DefaultMQProducer producer, String topic, String tags, String keys, String msg,
			String orderTag, String charset, SendCallback callback) throws MqBussinessException, MqWapperException
	{

	}
}