package com.netease.lottery.easyq.example;

import java.util.List;

import com.netease.lottery.easymq.consumer.bean.MQConsumerConfigBean;
import com.netease.lottery.easymq.consumer.scanner.ScanPackage;

public class TestScan
{
	public static void main(String[] args)
	{
		String config = "10.120.241.54:9876;10.120.241.29:9876";
		config = "";
		System.out.println(config.matches("[0-9.:;]+"));
		String packages = "com.netease.lottery.easymq";
		System.out.println("检测前的package: " + packages);
		List<MQConsumerConfigBean> genConsumerConfigList = ScanPackage.genConsumerConfigList(packages);
		System.out.println(genConsumerConfigList);
	}
}
