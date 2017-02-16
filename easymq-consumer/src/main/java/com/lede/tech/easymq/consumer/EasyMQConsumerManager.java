package com.lede.tech.easymq.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lede.tech.easymq.common.constant.MQConstant;
import com.lede.tech.easymq.common.exception.MqConsumerConfigException;
import com.lede.tech.easymq.consumer.bean.ConsumerConfigBean;
import com.lede.tech.easymq.consumer.scanner.ScanPackage;

/**
 * 
 * @Desc 
 * @Author bjguosong
 * @Author ykhu
 */
public class EasyMQConsumerManager
{
	private static final Log LOG = LogFactory.getLog(EasyMQConsumerManager.class);

	public static void init()
	{
		try
		{
			LOG.info("easyMQ consumer initing.");
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
			LOG.info("easyMQ consumer init ok.");
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
		//		String root = EasyMQConsumerManager.class.getClassLoader().getResource(MQConstant.CONFIG_DIR).getPath();
		//		root = MQConstant.IS_WINDOWS ? root.substring(6) : root.substring(5);
		//		if (StringUtils.isEmpty(root))
		//		{
		//			String warn = "easymq wrong. consumer config dir not exist. config dir should be src/main/resources/"
		//					+ MQConstant.CONFIG_DIR;
		//			LOG.fatal(warn);
		//			throw new MqConsumerConfigException(warn);
		//		}
		//		Path configPath = Paths.get(root, MQConstant.CONFIG_DIR_CONSUMERS, MQConstant.DEFAULT_CONSUMER_FILENAME);
		//		LOG.info("easymq running. find cousumer properties in " + configPath);
		String filePath = MQConstant.CONFIG_DIR + "/" + MQConstant.CONFIG_DIR_CONSUMERS + "/"
				+ MQConstant.DEFAULT_CONSUMER_FILENAME;
		try
		{
			InputStream in = EasyMQConsumerManager.class.getClassLoader()
					.getResourceAsStream(filePath);
			//			in = Files.newInputStream(configPath);
			/*if (in == null)
			{
				String warn = "easymq wrong. consumer config not exist. plz check. configPath should be :" + configPath;
				LOG.fatal(warn);
				throw new MqConsumerConfigException(warn);
			}*/
			props.load(in);
		}
		catch (IOException e)
		{
			String warn = "easymq wrong. consumer read config io error. configPath:" + filePath;
			LOG.fatal(warn, e);
			throw new MqConsumerConfigException(warn, e);
		}
		return props;
	}
}
