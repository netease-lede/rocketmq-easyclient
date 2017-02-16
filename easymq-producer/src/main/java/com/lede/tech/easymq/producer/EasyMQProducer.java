package com.lede.tech.easymq.producer;

import java.util.Objects;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.springframework.util.StringUtils;

import com.lede.tech.easymq.common.constant.MQConstant;
import com.lede.tech.easymq.common.exception.MqBussinessException;
import com.lede.tech.easymq.common.exception.MqConsumerConfigException;
import com.lede.tech.easymq.common.exception.MqProducerConfigException;
import com.lede.tech.easymq.common.exception.MqWapperException;
import com.lede.tech.easymq.producer.assist.EasyMQMessageConfig;
import com.lede.tech.easymq.producer.enums.ProducerTransferMode;

/**
 * 
 * @Desc 
 * @Author bjguosong
 * @Author ykhu
 */
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

	private void checkConfig(EasyMQMessageConfig config) throws MqBussinessException
	{
		if (Objects.isNull(config))
		{
			throw new MqBussinessException("EasyMQMessageConfig config can not be null.");
		}
		if (Objects.isNull(config.getTransferMode()))
		{
			throw new MqBussinessException("EasyMQMessageConfig transferMode can not be null.");
		}
		if (StringUtils.isEmpty(config.getTopic()))
		{
			throw new MqBussinessException("EasyMQMessageConfig topic can not be empty.");
		}
		if (StringUtils.isEmpty(config.getKeys()))
		{
			throw new MqBussinessException("EasyMQMessageConfig keys can not be empty.");
		}
		if (StringUtils.isEmpty(config.getMessage()))
		{
			throw new MqBussinessException("EasyMQMessageConfig message can not be empty.");
		}
		if (StringUtils.isEmpty(config.getCharSet()))
		{
			throw new MqBussinessException("EasyMQMessageConfig charset can not be empty.");
		}
		if (StringUtils.isEmpty(config.getTags()))
		{
			throw new MqBussinessException("EasyMQMessageConfig tags can not be empty.");
		}
	}

	/**
	 * 发送方式同步，编码UTF-8。
	 * @param topic
	 * @param keys
	 * @param msg
	 * @throws MqWapperException
	 * @throws MqBussinessException
	 */
	public void sendMsg(String topic, String keys, String msg) throws MqWapperException, MqBussinessException
	{
		EasyMQMessageConfig config = new EasyMQMessageConfig(topic, keys, msg);
		config.getTransferMode().sendMsg(producer, config);
	}

	/**
	 * 发送方式异步，编码为UTF-8。
	 * @param topic
	 * @param keys
	 * @param msg
	 * @param callback
	 * @throws MqWapperException
	 * @throws MqBussinessException
	 */
	public void sendMsg(String topic, String keys, String msg, SendCallback callback)
			throws MqWapperException, MqBussinessException
	{
		EasyMQMessageConfig config = new EasyMQMessageConfig(topic, keys, msg);
		config.setTransferMode(ProducerTransferMode.ASYNC);
		config.setCallback(callback);
		config.getTransferMode().sendMsg(producer, config);
	}

	/**
	 * 定制化发送
	 * @param config 
	 * @throws MqWapperException
	 * @throws MqBussinessException
	 */
	public void sendMsg(EasyMQMessageConfig config) throws MqWapperException, MqBussinessException
	{
		checkConfig(config);
		if (Objects.nonNull(config.getCallback()))
		{
			config.setTransferMode(ProducerTransferMode.ASYNC);
		}
		ProducerTransferMode transferMode = config.getTransferMode();
		transferMode.sendMsg(producer, config);
	}

	public void shutdown()
	{
		producer.shutdown();
	}
}
