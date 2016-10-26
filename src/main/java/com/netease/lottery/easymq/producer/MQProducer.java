package com.netease.lottery.easymq.producer;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.netease.lottery.easymq.constant.MQConstant;

public class MQProducer
{
	private final Log LOG = LogFactory.getLog(MQProducer.class);

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

	public void sendOneWay(String topic, String tags, String keys, String msg) throws InterruptedException
	{
		try
		{
			Message message = new Message(topic, tags, keys, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
			producer.sendOneway(message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Thread.sleep(1000);
		}
	}

	public void sendByOrder(String topic, String tags, String keys, String msg)
	{
		try
		{
			Message message = new Message(topic, tags, keys, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
			SendResult sendResult = producer.send(message);
			System.out.println(sendResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void sendMsg(String topic, String tags, String keys, String msg) throws InterruptedException
	{
		try
		{
			Message message = new Message(topic, tags, keys, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
			SendResult sendResult = producer.send(message);
			System.out.println(sendResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Thread.sleep(1000);
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
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
