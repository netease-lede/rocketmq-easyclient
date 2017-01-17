package com.netease.lottery.easymq.example.producer;

import com.netease.lottery.easymq.common.exception.MqBussinessException;
import com.netease.lottery.easymq.common.exception.MqWapperException;
import com.netease.lottery.easymq.producer.EasyMQProducer;
import com.netease.lottery.easymq.producer.EasyMQProducerFactory;
import com.netease.lottery.easymq.producer.enums.ProducerTransferMode;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
public class TestProducer
{
	public static void main(String[] args)
	{
		EasyMQProducer producer = EasyMQProducerFactory.getProducer();
		long begin = System.currentTimeMillis();
		try
		{
			//producer.sendMsg("topic20161116", "order10", "order10detail");
			System.out.println("begin");
			System.out.println(begin);
			producer.sendMsg("topic20161119", "id1", "onlyyou", "utf-8", ProducerTransferMode.ONEWAY, null);
			//producer.sendMsg("topic20161119", "id1", "onlyyou");
			//			producer.sendMsg("topic20161119", "id1", "onlyyou", "utf-8", ProducerTransferMode.ASYNC,
			//					new SendCallback() {
			//						@Override
			//						public void onSuccess(SendResult sendResult)
			//						{
			//							System.out.println("success");
			//						}
			//
			//						@Override
			//						public void onException(Throwable e)
			//						{
			//							System.out.println("failed");
			//						}
			//					});
			//			for (int i = 10; i <= 14; i++)
			//			{
			//				producer.sendMsgOrderly("topic20161119", i + "", i + "detail", "onlyyou");
			//			}
		}
		catch (MqWapperException e)
		{
			e.printStackTrace();
		}
		catch (MqBussinessException e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("send,taking" + (System.currentTimeMillis() - begin) + "ms");
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
