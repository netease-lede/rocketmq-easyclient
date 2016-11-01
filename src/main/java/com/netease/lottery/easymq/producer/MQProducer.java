package com.netease.lottery.easymq.producer;

import java.io.FileInputStream;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.netease.lottery.easymq.assist.StandardMessageQueueSelector;
import com.netease.lottery.easymq.constant.MQConstant;
import com.netease.lottery.easymq.exception.MqBussinessException;
import com.netease.lottery.easymq.exception.MqWapperException;

public class MQProducer
{
	private static final Log LOG = LogFactory.getLog(MQProducer.class);

	private DefaultMQProducer producer;

	public MQProducer(String MQConfigFileName)
	{
		init(MQConfigFileName);
	}

	private void init(String MQConfigFileName)
	{
		//从配置文件读取
		Properties props = new Properties();
		try
		{
			String filePath = this.getClass().getClassLoader().getResource(MQConstant.CONFIG_DIR).getPath() + "/"
					+ MQConfigFileName;
			props.load(new FileInputStream(filePath));
			producer = new DefaultMQProducer("ProducerTestGroup");
			producer.setNamesrvAddr(props.getProperty("rocket.name.server"));
			producer.setInstanceName(props.getProperty("rocket.instance.name"));
			producer.start();
			LOG.info(props.getProperty("rocket.instance.name") + " started success");
		}
		catch (Exception e)
		{
			LOG.fatal("create RocketMQ producer error!use default setting!", e);
			return;
		}
	}

	/**
	 * 单向发送，不等待应答,消息使用utf8编码
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @throws MqWapperException
	 */
	public void sendMsgByOneWay(String topic, String tags, String keys, String msg) throws MqWapperException
	{
		this.sendMsgByOneWay(topic, tags, keys, msg, RemotingHelper.DEFAULT_CHARSET);
	}

	/**
	 * 单向发送，不等待应答
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @param charset
	 * @throws MqWapperException
	 */
	public void sendMsgByOneWay(String topic, String tags, String keys, String msg, String charset)
			throws MqWapperException
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

	/**
	 * 单向顺序发送，不等待应答，使用utf-8编码
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @param orderTag
	 */
	public void sendMsgByOneWayOrderly(String topic, String tags, String keys, String msg, String orderTag)
			throws MqWapperException
	{
		this.sendMsgByOneWayOrderly(topic, tags, keys, msg, orderTag, RemotingHelper.DEFAULT_CHARSET);
	}

	/**
	 * 单向顺序发送，不等待应答
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @param orderTag
	 * @param charset
	 * @throws MqWapperException
	 */
	public void sendMsgByOneWayOrderly(String topic, String tags, String keys, String msg, String orderTag,
			String charset) throws MqWapperException
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

	/**
	 * 顺序消息，同类orderTag标记的消息接收有序,消息使用utf8编码
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @param orderTag
	 * @throws MqBussinessException
	 * @throws MqWapperException
	 */
	public void sendMsgBySyncOrderly(String topic, String tags, String keys, String msg, String orderTag)
			throws MqBussinessException, MqWapperException
	{
		this.sendMsgBySyncOrderly(topic, tags, keys, msg, orderTag, RemotingHelper.DEFAULT_CHARSET);
	}

	/**
	 * 顺序消息，同类orderTag标记的消息接收有序
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @param orderTag
	 * @param charset
	 * @throws MqBussinessException
	 * @throws MqWapperException
	 */
	public void sendMsgBySyncOrderly(String topic, String tags, String keys, String msg, String orderTag,
			String charset) throws MqBussinessException, MqWapperException
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

	/**
	 * 同步返回,消息使用utf8编码
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @throws MqBussinessException
	 * @throws MqWapperException
	 */
	public void sendMsgBySync(String topic, String tags, String keys, String msg)
			throws MqBussinessException, MqWapperException
	{
		this.sendMsgBySync(topic, tags, keys, msg, RemotingHelper.DEFAULT_CHARSET);
	}

	/**
	 * 同步返回
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @param charset
	 */
	public void sendMsgBySync(String topic, String tags, String keys, String msg, String charset)
			throws MqWapperException, MqBussinessException
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

	/**
	 * 异步返回,消息使用utf8编码
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @param sendCallback
	 */
	public void sendMsgByAsync(String topic, String tags, String keys, String msg, SendCallback sendCallback)
			throws MqWapperException
	{
		this.sendMsgByAsync(topic, tags, keys, msg, sendCallback, RemotingHelper.DEFAULT_CHARSET);
	}

	/**
	 * 异步返回
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @param sendCallback
	 * @param charset
	 */
	public void sendMsgByAsync(String topic, String tags, String keys, String msg, SendCallback sendCallback,
			String charset) throws MqWapperException
	{
		try
		{
			Message message = new Message(topic, tags, keys, msg.getBytes(charset));
			producer.send(message, sendCallback);
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

	/**
	 * 异步返回，顺序消息,utf-8编码
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @param orderTag
	 * @param sendCallback
	 * @throws MqWapperException
	 */
	public void sendMsgByAsyncOrderly(String topic, String tags, String keys, String msg, String orderTag,
			SendCallback sendCallback) throws MqWapperException
	{
		this.sendMsgByAsyncOrderly(topic, tags, keys, msg, orderTag, sendCallback, RemotingHelper.DEFAULT_CHARSET);
	}

	/**
	 * 异步返回，顺序消息
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @param orderTag
	 * @param sendCallback
	 * @param charset
	 */
	public void sendMsgByAsyncOrderly(String topic, String tags, String keys, String msg, String orderTag,
			SendCallback sendCallback, String charset) throws MqWapperException
	{
		try
		{
			Message message = new Message(topic, tags, keys, msg.getBytes(charset));
			producer.send(message, new StandardMessageQueueSelector(), orderTag, sendCallback);
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

	public void shutdown()
	{
		producer.shutdown();
	}

	public static void main(String[] args)
	{
		MQProducer producer = new MQProducer("common_rocketMQ.properties");
		try
		{
			producer.sendMsgBySync("TopicTest", null, null, "RocketMQ test");
			producer.shutdown();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
