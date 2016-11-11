package com.netease.lottery.easymq.consumer;

import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.Lists;
import com.netease.lottery.easymq.common.exception.MqConsumerConfigException;
import com.netease.lottery.easymq.consumer.bean.MQConsumerConfigBean;

public class MQConsumerGroup
{
	private static final Log LOG = LogFactory.getLog(MQConsumerGroup.class);

	/**
	 * 消费者配置
	 */
	private MQConsumerConfigBean consumerConfigBean;

	/**
	 * 消费组名称
	 */
	private String consumerGroupName;
	/**
	 * 消费者数量
	 */
	private Integer consumerNumber;
	/**
	 * 消费组配置文件
	 */
	private Properties prop;
	/**
	 * 消费者列表
	 */
	private List<MQPushConsumer> consumerList = Lists.newArrayList();

	public void initConsumerGroup()
	{
		if (consumerNumber == null || consumerNumber <= 0)
		{
			String warn = "easymq wrong. consumerNumber:" + consumerNumber;
			LOG.fatal(warn);
			throw new MqConsumerConfigException(warn);
		}
		this.setConsumerGroupName(consumerConfigBean.getGroupName());
		for (int index = 1; index <= consumerNumber; index++)
		{
			MQPushConsumer consumer = MQConsumerFactory.getFactory().getMQConsumer(prop);
			try
			{
				consumer.loadConfigBean(consumerConfigBean);
				consumer.start();
				LOG.info("easymq running. a consumer started. use config:" + prop + ",use consumerConfigBean:"
						+ consumerConfigBean);
			}
			catch (Exception e)
			{
				String warn = "easymq wrong. load consumer error. consumerConfigBean:" + consumerConfigBean;
				LOG.fatal(warn, e);
				throw new MqConsumerConfigException(warn, e);
			}
			consumerList.add(consumer);
		}
	}

	public MQConsumerConfigBean getConsumerConfigBean()
	{
		return consumerConfigBean;
	}

	public void setConsumerConfigBean(MQConsumerConfigBean consumerConfigBean)
	{
		this.consumerConfigBean = consumerConfigBean;
	}

	public Properties getConsumerConfig()
	{
		return prop;
	}

	public void setConsumerConfig(Properties consumerConfig)
	{
		this.prop = consumerConfig;
	}

	public String getConsumerGroupName()
	{
		return consumerGroupName;
	}

	public void setConsumerGroupName(String consumerGroupName)
	{
		this.consumerGroupName = consumerGroupName;
	}

	public Integer getConsumerNumber()
	{
		return consumerNumber;
	}

	public void setConsumerNumber(Integer consumerNumber)
	{
		this.consumerNumber = consumerNumber;
	}
}
