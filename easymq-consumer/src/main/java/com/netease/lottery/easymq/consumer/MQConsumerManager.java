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
import com.netease.lottery.easymq.consumer.bean.MQConsumerConfigBean;
import com.netease.lottery.easymq.consumer.scanner.ScanPackage;

public class MQConsumerManager
{
	private static final Log LOG = LogFactory.getLog(MQConsumerManager.class);
	private static final boolean IS_WINDOWS = System.getProperty("os.name").contains("indow");

	static
	{
		init();
	}

	private static void init()
	{
		Properties props = getConfigProp();
		String packages = props.getProperty(MQConstant.CONFIG_CONSUMER_SCANPACKAGE,
				MQConstant.CONFIG_CONSUMER_SCANPACKAGE_DEFAULT);
		List<MQConsumerConfigBean> genConsumerConfigList = ScanPackage.genConsumerConfigList(packages);

		for (MQConsumerConfigBean consumerConfig : genConsumerConfigList)
		{
			MQConsumerGroup consumerGroup = new MQConsumerGroup();

		}
		MQConsumerGroup consumerGroup = new MQConsumerGroup();
		consumerGroup.setConsumerConfig(props);
		consumerGroup.setConsumerGroupName("defaultConsumerGroup");
		consumerGroup.setConsumerNumber(1);
		try
		{
			consumerGroup.initConsumerGroup();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static Properties getConfigProp()
	{
		//从配置文件读取
		Properties props = new Properties();
		String root = MQConsumerManager.class.getClassLoader().getResource(MQConstant.CONFIG_DIR).getPath();
		root = IS_WINDOWS ? root.substring(1) : root;
		System.out.println(root);
		if (StringUtils.isEmpty(root))
		{
			String warn = "easymq wrong. consumer config dir not exist. config dir should be src/main/resources/"
					+ MQConstant.CONFIG_DIR;
			LOG.fatal(warn);
			throw new MqConsumerConfigException(warn);
		}
		Path configPath = Paths.get(root, MQConstant.DEFAULT_CONSUMER_FILENAME);
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
