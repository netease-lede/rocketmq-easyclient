package com.netease.lottery.easymq.example.producer;

import com.netease.lottery.easymq.common.exception.MqBussinessException;
import com.netease.lottery.easymq.common.exception.MqWapperException;
import com.netease.lottery.easymq.producer.EasyMQProducer;
import com.netease.lottery.easymq.producer.EasyMQProducerFactory;

public class TestProducer {
	public static void main(String[] args) {
		EasyMQProducer producer = EasyMQProducerFactory.getProducer();
		try {
			//producer.sendMsg("topic20161116", "order10", "order10detail");
			System.out.println("begin");
			long begin = System.currentTimeMillis();
			for (int i = 10; i <= 14; i++) {
				producer.sendMsgOrderly("topic20161119", i + "", i + "detail", "onlyyou");
			}
			System.out.println("send,taking" + (System.currentTimeMillis() - begin) + "ms");
		} catch (MqWapperException e) {
			e.printStackTrace();
		} catch (MqBussinessException e) {
			e.printStackTrace();
		}

		/** 
		 * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己 
		 * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法 
		 */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				producer.shutdown();
			}
		}));
		System.exit(0);
	}
}
