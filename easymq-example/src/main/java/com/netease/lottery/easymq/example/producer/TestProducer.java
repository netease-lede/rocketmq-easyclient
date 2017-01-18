package com.netease.lottery.easymq.example.producer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

import com.netease.lottery.easymq.common.exception.MqBussinessException;
import com.netease.lottery.easymq.common.exception.MqWapperException;
import com.netease.lottery.easymq.producer.EasyMQProducer;
import com.netease.lottery.easymq.producer.EasyMQProducerFactory;

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
			System.out.println("begin");
			System.out.println(begin);
			producer.sendMsg("topic20170118", "id3", "UU", new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult)
				{
					System.out.println("success");
				}

				@Override
				public void onException(Throwable e)
				{
					System.out.println("failed");
				}
			});
			//			producer.sendMsg("topic20170118", "id1", "onlyyou");
			//			EasyMQMessageConfig config = new EasyMQMessageConfig("topic20170118", "id2", "onlyU");
			//			config.setTransferMode(ProducerTransferMode.ASYNC);
			//			config.setCallback(new SendCallback() {
			//				@Override
			//				public void onSuccess(SendResult sendResult)
			//				{
			//					System.out.println("success");
			//				}
			//
			//				@Override
			//				public void onException(Throwable e)
			//				{
			//					System.out.println("failed");
			//				}
			//			});
			//			producer.sendMsg(config);
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
		 * Ӧ���˳�ʱ��Ҫ����shutdown��������Դ���ر��������ӣ���MetaQ��������ע���Լ� 
		 * ע�⣺���ǽ���Ӧ����JBOSS��Tomcat���������˳����������shutdown���� 
		 */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run()
			{
				producer.shutdown();
			}
		}));
		System.exit(0);
	}
}
