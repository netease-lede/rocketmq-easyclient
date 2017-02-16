package com.lede.tech.easymq.example;

import com.lede.tech.easymq.consumer.EasyMQConsumerManager;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
public class TestScan
{
	public static void main(String[] args)
	{
		EasyMQConsumerManager.init();
		//		String config = "10.120.241.54:9876;10.120.241.29:9876";
		//		config = "";
		//		System.out.println(config.matches("[0-9.:;]+"));
		//		String packages = "com.netease.lottery.easymq";
		//		System.out.println("检测前的package: " + packages);
		//		List<ConsumerConfigBean> genConsumerConfigList = ScanPackage.genConsumerConfigList(packages);
		//		System.out.println(genConsumerConfigList);
	}
}
