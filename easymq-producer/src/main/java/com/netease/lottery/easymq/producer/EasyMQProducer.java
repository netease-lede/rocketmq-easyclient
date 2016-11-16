package com.netease.lottery.easymq.producer;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.netease.lottery.easymq.common.constant.MQConstant;
import com.netease.lottery.easymq.common.exception.MqBussinessException;
import com.netease.lottery.easymq.common.exception.MqConsumerConfigException;
import com.netease.lottery.easymq.common.exception.MqProducerConfigException;
import com.netease.lottery.easymq.common.exception.MqWapperException;
import com.netease.lottery.easymq.producer.enums.ProducerTransferMode;

public class EasyMQProducer
{
	private static final Log LOG = LogFactory.getLog(EasyMQProducer.class);

	private DefaultMQProducer producer;

	public EasyMQProducer(Properties prop)
	{
		init(prop);
	}

	private void init(Properties prop)
	{
		if (!checkVaild(prop))
		{
			throw new MqProducerConfigException("easymq wrong. producer config properties error.");
		}
		//从配置文件读取
		producer = new DefaultMQProducer(prop.getProperty(MQConstant.CONFIG_PRODUCER_GROUPNAME));
		producer.setNamesrvAddr(prop.getProperty(MQConstant.CONFIG_PRODUCER_NAMESERVER));
		String topicQueueNums = prop.getProperty(MQConstant.CONFIG_PRODUCER_TOPICQUEUENUMS);
		if (!StringUtils.isEmpty(topicQueueNums))
		{
			producer.setDefaultTopicQueueNums(Integer.parseInt(topicQueueNums));
		}
		else
		{
			producer.setDefaultTopicQueueNums(MQConstant.CONFIG_PRODUCER_TOPICQUEUENUMS_DEFAULT);
		}
		String timeout = prop.getProperty(MQConstant.CONFIG_PRODUCER_TIMEOUT);
		if (!StringUtils.isEmpty(timeout))
		{
			producer.setSendMsgTimeout(Integer.parseInt(timeout));
		}
		String instanceName = prop.getProperty(MQConstant.CONFIG_PRODUCER_INSTANCENAME);
		if (!StringUtils.isEmpty(instanceName))
		{
			producer.setInstanceName(instanceName);
		}
		try
		{
			producer.start();
			LOG.info("easymq running. a producer started. use properties:" + prop);
		}
		catch (MQClientException e)
		{
			String warn = "easymq wrong. producer init failed. prop:" + prop;
			LOG.fatal(warn, e);
			throw new MqConsumerConfigException(warn, e);
		}
	}

	private boolean checkVaild(Properties props)
	{
		String nameserver = props.getProperty(MQConstant.CONFIG_PRODUCER_NAMESERVER);
		if (!nameserver.matches("[0-9.:;]+"))
		{
			return false;
		}
		String groupname = props.getProperty(MQConstant.CONFIG_PRODUCER_GROUPNAME);
		if (StringUtils.isEmpty(groupname))
		{
			return false;
		}
		String topicQueueNums = props.getProperty(MQConstant.CONFIG_PRODUCER_TOPICQUEUENUMS);
		if (!StringUtils.isEmpty(topicQueueNums) && !topicQueueNums.matches("[0-9]+"))
		{
			return false;
		}
		String timeout = props.getProperty(MQConstant.CONFIG_PRODUCER_TIMEOUT);
		if (!StringUtils.isEmpty(timeout) && !timeout.matches("[0-9]+"))
		{
			return false;
		}
		return true;
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
		ProducerTransferMode.SYNC.sendMsg(producer, topic, MQConstant.TOPIC_DEFAULT_TAG, keys, msg,
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
		ProducerTransferMode.SYNC.sendMsg(producer, topic, MQConstant.TOPIC_DEFAULT_TAG, keys, msg, charset, null);
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
	public void sendMsg(String topic, String keys, String msg, String charset, ProducerTransferMode transferMode,
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
		ProducerTransferMode.SYNC.sendMsgOrderly(producer, topic, MQConstant.TOPIC_DEFAULT_TAG, keys, msg, orderTag,
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
		ProducerTransferMode.SYNC.sendMsgOrderly(producer, topic, MQConstant.TOPIC_DEFAULT_TAG, keys, msg, orderTag,
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
			ProducerTransferMode transferMode, SendCallback callback) throws MqWapperException, MqBussinessException
	{
		transferMode.sendMsgOrderly(producer, topic, MQConstant.TOPIC_DEFAULT_TAG, keys, msg, orderTag, charset,
				callback);
	}

	public void shutdown()
	{
		producer.shutdown();
	}
}
