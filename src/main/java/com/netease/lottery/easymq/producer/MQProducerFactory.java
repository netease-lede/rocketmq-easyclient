package com.netease.lottery.easymq.producer;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netease.lottery.easymq.constant.MQConstant;
import com.netease.lottery.easymq.producer.MQProducer;

public class MQProducerFactory
{
	private static final Log LOG = LogFactory.getLog(MQProducerFactory.class);
	private static final MQProducerFactory INSTANCE = new MQProducerFactory();

	private static HashMap<String, MQProducer> PRODUCER_MAP;

	private MQProducerFactory()
	{
		PRODUCER_MAP = new HashMap<String, MQProducer>();
		loadConfig();
	}

	public static MQProducerFactory getInstance()
	{
		return INSTANCE;
	}

	public MQProducer getDefaultProducer() throws Exception
	{
		return getProducer(MQConstant.DEFAULT_FILENAME);
	}

	/**
	 * 获取RocketMQ Producer实例
	 * @param mqConfigFileName
	 * @return
	 * @throws Exception 
	 */
	public MQProducer getProducer(String mqConfigFileName) throws Exception
	{
		if (mqConfigFileName == null || mqConfigFileName.length() <= 0)
		{
			throw new Exception("");
		}
		MQProducer mqProducer = PRODUCER_MAP.get(mqConfigFileName);
		if (mqProducer != null)
		{
			return mqProducer;
		}
		LOG.fatal("#No kafka config file find." + mqConfigFileName);
		throw new Exception("#No kafka config file find." + mqConfigFileName);
	}

	private static void loadConfig()
	{
		try
		{
			String filePath = MQProducerFactory.class.getClassLoader().getResource(MQConstant.CONFIG_DIR).getPath();
			LOG.info("#Load RocketMQ dir path:" + filePath);
			File configDir = new File(filePath);
			if (!configDir.exists())
			{
				LOG.error("#No RocketMQ config dir find.");
				throw new Exception("#RocketMQ is not exist.");
			}
			if (configDir.isFile())
			{
				throw new Exception("#RocketMQ config dir is a file.");
			}
			File[] files = configDir.listFiles();
			if (files != null && files.length > 0)
			{
				for (File file : files)
				{
					try
					{
						buildProducer(file.getName());
					}
					catch (Exception e)
					{
						LOG.fatal("#Builder producer:" + file.getName() + " error.Cause:", e);
					}
				}
			}
			else
			{
				LOG.fatal("#No RocketMQ config file found.No file load.");
			}
		}
		catch (Exception e)
		{
			LOG.fatal("#Load RocketMQ file error!Cause:", e);
		}
	}

	private static void buildProducer(String mqConfigFileName)
	{
		MQProducer mqMsgProducer = new MQProducer(mqConfigFileName);
		PRODUCER_MAP.put(mqConfigFileName, mqMsgProducer);
		LOG.info("#Load RocketMQ config:" + mqConfigFileName);
	}
}
