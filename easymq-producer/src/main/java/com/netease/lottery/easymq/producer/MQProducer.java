package com.netease.lottery.easymq.producer;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.netease.lottery.easymq.common.constant.MQConstant;
import com.netease.lottery.easymq.common.exception.MqBussinessException;
import com.netease.lottery.easymq.common.exception.MqWapperException;
import com.netease.lottery.easymq.producer.enums.TransferModeEnum;

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
			producer = new DefaultMQProducer(props.getProperty("easymq.producer.groupname"));
			producer.setNamesrvAddr(props.getProperty("easymq.name.server"));
			producer.setInstanceName(props.getProperty("easymq.instance.name"));
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
	 * 发送方式同步，编码UTF-8。一般使用该方法即可
	 * @param topic
	 * @param keys
	 * @param msg
	 * @throws MqWapperException
	 * @throws MqBussinessException
	 */
	public void sendMsg(String topic, String keys, String msg) throws MqWapperException, MqBussinessException
	{
		TransferModeEnum.SYNC.sendMsg(producer, topic, MQConstant.TOPIC_DEFAULT_TAG, keys, msg,
				RemotingHelper.DEFAULT_CHARSET, null);
	}

	/**
	 * 发送方式同步，支持自定义编码
	 * @param topic
	 * @param keys
	 * @param msg
	 * @param charset
	 * @throws MqWapperException
	 * @throws MqBussinessException
	 */
	public void sendMsg(String topic, String keys, String msg, String charset)
			throws MqWapperException, MqBussinessException
	{
		TransferModeEnum.SYNC.sendMsg(producer, topic, MQConstant.TOPIC_DEFAULT_TAG, keys, msg, charset, null);
	}

	/**
	 * 发送方式可选，transferMode参数支持同步、异步和不确认
	 * @param topic
	 * @param keys
	 * @param msg
	 * @param charset
	 * @param transferMode
	 * @param callback
	 * @throws MqWapperException
	 * @throws MqBussinessException
	 */
	public void sendMsg(String topic, String keys, String msg, String charset, TransferModeEnum transferMode,
			SendCallback callback) throws MqWapperException, MqBussinessException
	{
		transferMode.sendMsg(producer, topic, MQConstant.TOPIC_DEFAULT_TAG, keys, msg, charset, callback);
	}

	/**
	 * 发送方式同步，编码UTF-8，相同orderTag消息有序
	 * @param topic
	 * @param keys
	 * @param msg
	 * @param orderTag
	 * @throws MqBussinessException
	 * @throws MqWapperException
	 */
	public void sendMsgOrderly(String topic, String keys, String msg, String orderTag)
			throws MqBussinessException, MqWapperException
	{
		TransferModeEnum.SYNC.sendMsgOrderly(producer, topic, MQConstant.TOPIC_DEFAULT_TAG, keys, msg, orderTag,
				RemotingHelper.DEFAULT_CHARSET, null);
	}

	/**
	 * 发送方式同步，自定义编码，相同orderTag消息有序
	 * @param topic
	 * @param keys
	 * @param msg
	 * @param orderTag
	 * @param charset
	 * @throws MqBussinessException
	 * @throws MqWapperException
	 */
	public void sendMsgOrderly(String topic, String keys, String msg, String orderTag, String charset)
			throws MqBussinessException, MqWapperException
	{
		TransferModeEnum.SYNC.sendMsgOrderly(producer, topic, MQConstant.TOPIC_DEFAULT_TAG, keys, msg, orderTag,
				charset, null);
	}

	/**
	 * 发送方式可选，相同orderTag消息有序，transferMode参数支持同步、异步和不确认
	 * @param topic
	 * @param keys
	 * @param msg
	 * @param orderTag
	 * @param charset
	 * @param transferMode
	 * @param callback
	 * @throws MqWapperException
	 * @throws MqBussinessException
	 */
	public void sendMsgOrderly(String topic, String keys, String msg, String orderTag, String charset,
			TransferModeEnum transferMode, SendCallback callback) throws MqWapperException, MqBussinessException
	{
		transferMode.sendMsgOrderly(producer, topic, MQConstant.TOPIC_DEFAULT_TAG, keys, msg, orderTag, charset,
				callback);
	}

	public void shutdown()
	{
		producer.shutdown();
	}
}
