package com.lede.tech.easymq.producer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lede.tech.easymq.common.constant.MQConstant;
import com.lede.tech.easymq.common.exception.MqConsumerConfigException;

/**
 * 
 * @Desc 
 * @Author bjguosong
 * @Author ykhu
 */
public class EasyMQProducerFactory
{
	private static final Log LOG = LogFactory.getLog(EasyMQProducerFactory.class);

	private static EasyMQProducer producer;

	public static EasyMQProducer getProducer()
	{
		try
		{
			if (producer == null)
			{
				synchronized (EasyMQProducerFactory.class)
				{
					if (producer == null)
					{
						init();
					}
				}
			}
		}
		catch (Exception e)
		{
			LOG.fatal("easymq wrong. producer init error.", e);
		}
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
		//		String root = EasyMQProducerFactory.class.getClassLoader().getResource(MQConstant.CONFIG_DIR).getPath();
		//		root = MQConstant.IS_WINDOWS ? root.substring(1) : root;
		//		if (StringUtils.isEmpty(root))
		//		{
		//			String warn = "easymq wrong. producer config dir not exist. config dir should be src/main/resources/"
		//					+ MQConstant.CONFIG_DIR;
		//			LOG.fatal(warn);
		//			throw new MqProducerConfigException(warn);
		//		}
		//		Path configPath = Paths.get(root, MQConstant.CONFIG_DIR_PRODUCERS, MQConstant.DEFAULT_PRODUCER_FILENAME);
		//		LOG.info("easymq running. find producer properties in " + configPath);
		//		if (Files.exists(configPath) && Files.isRegularFile(configPath))
		//		{
		//		}
		//		else
		//		{
		//			String warn = "easymq wrong. producer config not exist. plz check. configPath should be :" + configPath;
		//			LOG.fatal(warn);
		//			throw new MqConsumerConfigException(warn);
		//		}
		String filePath = MQConstant.CONFIG_DIR + "/" + MQConstant.CONFIG_DIR_PRODUCERS + "/"
				+ MQConstant.DEFAULT_PRODUCER_FILENAME;
		try
		{
			InputStream in = EasyMQProducerFactory.class.getClassLoader().getResourceAsStream(filePath);
			props.load(in);
			//			props.load(Files.newInputStream(configPath));
		}
		catch (IOException e)
		{
			String warn = "easymq wrong. producer read config io error. configPath:" + filePath;
			LOG.fatal(warn, e);
			throw new MqConsumerConfigException(warn, e);
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
