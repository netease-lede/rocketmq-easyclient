package com.netease.lottery.easyq.example;

import java.util.List;

import com.netease.lottery.easymq.consumer.bean.MQConsumerConfigBean;
import com.netease.lottery.easymq.consumer.scanner.ScanPackage;

public class TestScan
{
	public static void main(String[] args)
	{
		String packages = "com.netease.lottery.easymq";
		System.out.println("检测前的package: " + packages);
		List<MQConsumerConfigBean> genConsumerConfigList = ScanPackage.genConsumerConfigList(packages);
		System.out.println(genConsumerConfigList);
	}
}
