package com.netease.lottery.easymq.producer;

import java.io.FileInputStream;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
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
	 * 单向发送，不等待应答
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @throws MqWapperException
	 */
	public void sendOneWay(String topic, String tags, String keys, String msg) throws MqWapperException
	{
		try
		{
			Message message = new Message(topic, tags, keys, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
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
	 * 顺序消息，同类orderTag标记的消息接收有序
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @param orderTag
	 * @throws MqBussinessException
	 * @throws MqWapperException
	 */
	public void sendByOrder(String topic, String tags, String keys, String msg, String orderTag)
			throws MqBussinessException, MqWapperException
	{
		try
		{
			Message message = new Message(topic, tags, keys, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
			SendResult sendResult = producer.send(message, new MessageQueueSelector() {
				public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg)
				{
					Integer id = ((String) arg).hashCode();
					int index = id % mqs.size();
					return mqs.get(index);
				}
			}, orderTag);
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
	 * 同步返回
	 * @param topic
	 * @param tags
	 * @param keys
	 * @param msg
	 * @throws MqBussinessException
	 * @throws MqWapperException
	 */
	public void sendMsg(String topic, String tags, String keys, String msg)
			throws MqBussinessException, MqWapperException
	{
		try
		{
			Message message = new Message(topic, tags, keys, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
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

	public void shutdown()
	{
		producer.shutdown();
	}

	public static void main(String[] args)
	{
		MQProducer producer = new MQProducer("common_rocketMQ.properties");
		try
		{
			producer.sendMsg("TopicTest", null, null, "RocketMQ test");
			producer.shutdown();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
