package com.lede.tech.easymq.producer.enums;

import java.util.Objects;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.util.StringUtils;

import com.lede.tech.easymq.common.exception.MqBussinessException;
import com.lede.tech.easymq.common.exception.MqWapperException;
import com.lede.tech.easymq.producer.assist.EasyMQMessageConfig;
import com.lede.tech.easymq.producer.assist.StandardMessageQueueSelector;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
public enum ProducerTransferMode
{
	SYNC
	{
		@Override
		protected void sendMsg(DefaultMQProducer producer, Message message, EasyMQMessageConfig config)
				throws MqWapperException, MqBussinessException
		{
			try
			{
				SendResult sendResult = producer.send(message);
				SendStatus sendStatus = sendResult.getSendStatus();
				if (!Objects.equals(sendStatus, SendStatus.SEND_OK))
				{
					throw new MqBussinessException("send message return not ok. sendStatus:" + sendStatus.name());
				}
			}
			catch (Exception e)
			{
				throw new MqWapperException(e);
			}
		}

		@Override
		protected void sendMsgOrderly(DefaultMQProducer producer, Message message, EasyMQMessageConfig config)
				throws MqBussinessException, MqWapperException
		{
			try
			{
				SendResult sendResult = producer.send(message, new StandardMessageQueueSelector(),
						config.getOrderTag());
				SendStatus sendStatus = sendResult.getSendStatus();
				if (!Objects.equals(sendStatus, SendStatus.SEND_OK))
				{
					throw new MqBussinessException("send message return not ok. sendStatus:" + sendStatus.name());
				}
			}
			catch (Exception e)
			{
				throw new MqWapperException(e);
			}
		}
	},
	ASYNC
	{
		@Override
		public void sendMsg(DefaultMQProducer producer, EasyMQMessageConfig config)
				throws MqWapperException, MqBussinessException
		{
			if (Objects.isNull(config.getCallback()))
			{
				throw new MqBussinessException("ASYNC mode need callback. plz set up EasyMQMessageConfig.callback");
			}
			super.sendMsg(producer, config);
		}

		@Override
		protected void sendMsg(DefaultMQProducer producer, Message message, EasyMQMessageConfig config)
				throws MqWapperException, MqBussinessException
		{
			try
			{
				producer.send(message, config.getCallback());
			}
			catch (Exception e)
			{
				throw new MqWapperException(e);
			}
		}

		@Override
		protected void sendMsgOrderly(DefaultMQProducer producer, Message message, EasyMQMessageConfig config)
				throws MqBussinessException, MqWapperException
		{
			try
			{
				producer.send(message, new StandardMessageQueueSelector(), config.getOrderTag(), config.getCallback());
			}
			catch (Exception e)
			{
				throw new MqWapperException(e);
			}
		}
	},

	ONEWAY
	{

		@Override
		protected void sendMsg(DefaultMQProducer producer, Message message, EasyMQMessageConfig config)
				throws MqWapperException, MqBussinessException
		{
			try
			{
				producer.sendOneway(message);
			}
			catch (Exception e)
			{
				throw new MqWapperException(e);
			}
		}

		@Override
		protected void sendMsgOrderly(DefaultMQProducer producer, Message message, EasyMQMessageConfig config)
				throws MqBussinessException, MqWapperException
		{
			try
			{
				producer.sendOneway(message, new StandardMessageQueueSelector(), config.getOrderTag());
			}
			catch (Exception e)
			{
				throw new MqWapperException(e);
			}
		}
	};

	private ProducerTransferMode()
	{
	}

	private static Message genMessage(EasyMQMessageConfig config) throws MqWapperException
	{
		try
		{
			Message message = new Message(config.getTopic(), config.getTags(), config.getKeys(),
					config.getMessage().getBytes(config.getCharSet()));
			if (Objects.nonNull(config.getDelayLevel()))
			{
				message.setDelayTimeLevel(config.getDelayLevel().getDelayLevel());
			}
			return message;
		}
		catch (Exception e)
		{
			throw new MqWapperException(e);
		}

	}

	public void sendMsg(DefaultMQProducer producer, EasyMQMessageConfig config)
			throws MqWapperException, MqBussinessException
	{
		Message message = ProducerTransferMode.genMessage(config);
		if (!Objects.isNull(message))
		{
			if (StringUtils.isEmpty(config.getOrderTag()))
			{
				this.sendMsg(producer, message, config);
				return;
			}
			this.sendMsgOrderly(producer, message, config);
		}
	}

	protected abstract void sendMsg(DefaultMQProducer producer, Message message, EasyMQMessageConfig config)
			throws MqBussinessException, MqWapperException;

	protected abstract void sendMsgOrderly(DefaultMQProducer producer, Message message, EasyMQMessageConfig config)
			throws MqBussinessException, MqWapperException;

}
