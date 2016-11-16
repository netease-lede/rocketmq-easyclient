package com.netease.lottery.easymq.consumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.netease.lottery.easymq.common.constant.MQConstant;
import com.netease.lottery.easymq.common.exception.MqConsumerConfigException;
import com.netease.lottery.easymq.consumer.bean.ConsumerConfigBean;
import com.netease.lottery.easymq.consumer.scanner.ScanPackage;

public class EasyMQConsumerManager
{
	private static final Log LOG = LogFactory.getLog(EasyMQConsumerManager.class);

	public static void init()
	{
		try
		{
			Properties props = getConfigProp();
			String packages = props.getProperty(MQConstant.CONFIG_CONSUMER_SCANPACKAGE,
					MQConstant.CONFIG_CONSUMER_SCANPACKAGE_DEFAULT);
			List<ConsumerConfigBean> genConsumerConfigList = ScanPackage.genConsumerConfigList(packages);

			for (ConsumerConfigBean consumerConfigBean : genConsumerConfigList)
			{
				EasyMQConsumerGroup consumerGroup = new EasyMQConsumerGroup();
				consumerGroup.setConsumerConfig(props);
				consumerGroup.setConsumerNumber(1);
				consumerGroup.setConsumerConfigBean(consumerConfigBean);
				consumerGroup.initConsumerGroup();
			}
		}
		catch (Exception e)
		{
			LOG.fatal("easymq wrong. consumer init error.", e);
		}
	}

	private static Properties getConfigProp()
	{
		//从配置文件读取
		Properties props = new Properties();
		String root = EasyMQConsumerManager.class.getClassLoader().getResource(MQConstant.CONFIG_DIR).getPath();
		root = MQConstant.IS_WINDOWS ? root.substring(1) : root;
		if (StringUtils.isEmpty(root))
		{
			String warn = "easymq wrong. consumer config dir not exist. config dir should be src/main/resources/"
					+ MQConstant.CONFIG_DIR;
			LOG.fatal(warn);
			throw new MqConsumerConfigException(warn);
		}
		Path configPath = Paths.get(root, MQConstant.CONFIG_DIR_CONSUMERS, MQConstant.DEFAULT_CONSUMER_FILENAME);
		LOG.info("easymq running. find cousumer properties in " + configPath);
		if (Files.exists(configPath) && Files.isRegularFile(configPath))
		{
			try
			{
				props.load(Files.newInputStream(configPath));
			}
			catch (IOException e)
			{
				String warn = "easymq wrong. consumer read config io error. configPath:" + configPath;
				LOG.fatal(warn, e);
				throw new MqConsumerConfigException(warn, e);
			}
		}
		else
		{
			String warn = "easymq wrong. consumer config not exist. plz check. configPath should be :" + configPath;
			LOG.fatal(warn);
			throw new MqConsumerConfigException(warn);
		}
		return props;
	}
}
