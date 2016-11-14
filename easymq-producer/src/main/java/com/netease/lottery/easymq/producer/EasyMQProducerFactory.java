package com.netease.lottery.easymq.producer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.netease.lottery.easymq.common.constant.MQConstant;
import com.netease.lottery.easymq.common.exception.MqConsumerConfigException;
import com.netease.lottery.easymq.common.exception.MqProducerConfigException;

public class EasyMQProducerFactory
{
	private static final Log LOG = LogFactory.getLog(EasyMQProducerFactory.class);

	private static EasyMQProducer producer;

	static
	{
		init();
	}

	public static EasyMQProducer getProducer()
	{
		return producer;
	}

	private static void init()
	{
		Properties prop = getConfigProp();
		buildProducer(prop);
	}

	private static Properties getConfigProp()
	{
		//从配置文件读取
		Properties props = new Properties();
		String root = EasyMQProducerFactory.class.getClassLoader().getResource(MQConstant.CONFIG_DIR).getPath();
		root = MQConstant.IS_WINDOWS ? root.substring(1) : root;
		if (StringUtils.isEmpty(root))
		{
			String warn = "easymq wrong. producer config dir not exist. config dir should be src/main/resources/"
					+ MQConstant.CONFIG_DIR;
			LOG.fatal(warn);
			throw new MqProducerConfigException(warn);
		}
		Path configPath = Paths.get(root, MQConstant.DEFAULT_PRODUCER_FILENAME);
		LOG.info("easymq running. find producer properties in " + configPath);
		if (Files.exists(configPath) && Files.isRegularFile(configPath))
		{
			try
			{
				props.load(Files.newInputStream(configPath));
			}
			catch (IOException e)
			{
				String warn = "easymq wrong. producer read config io error. configPath:" + configPath;
				LOG.fatal(warn, e);
				throw new MqConsumerConfigException(warn, e);
			}
		}
		else
		{
			String warn = "easymq wrong. producer config not exist. plz check. configPath should be :" + configPath;
			LOG.fatal(warn);
			throw new MqConsumerConfigException(warn);
		}
		return props;
	}

	private static void buildProducer(Properties prop)
	{
		LOG.info("easymq running. producer use:" + prop);
		producer = new EasyMQProducer(prop);
	}

	//	private static void loadConfig()
	//	{
	//		try
	//		{
	//			String filePath = MQProducerFactory.class.getClassLoader().getResource(MQConstant.CONFIG_DIR).getPath();
	//			File configDir = new File(filePath);
	//			if (!configDir.exists())
	//			{
	//				String warn = "easymq wrong. product config dir find. filePath:" + filePath;
	//				LOG.fatal(warn);
	//				throw new Exception(warn);
	//			}
	//			if (configDir.isFile())
	//			{
	//				throw new Exception("#RocketMQ config dir is a file.");
	//			}
	//			File[] files = configDir.listFiles();
	//			if (files != null && files.length > 0)
	//			{
	//				for (File file : files)
	//				{
	//					try
	//					{
	//						buildProducer(file.getName());
	//					}
	//					catch (Exception e)
	//					{
	//						LOG.fatal("#Builder producer:" + file.getName() + " error.Cause:", e);
	//					}
	//				}
	//			}
	//			else
	//			{
	//				LOG.fatal("#No RocketMQ config file found.No file load.");
	//			}
	//		}
	//		catch (Exception e)
	//		{
	//			LOG.fatal("#Load RocketMQ file error!Cause:", e);
	//		}
	//
	//	}
}
