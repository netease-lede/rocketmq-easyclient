package com.lede.tech.rocketmq.easyclient.example.producer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

import com.lede.tech.rocketmq.easyclient.common.exception.MqBussinessException;
import com.lede.tech.rocketmq.easyclient.common.exception.MqWapperException;
import com.lede.tech.rocketmq.easyclient.producer.EasyMQProducer;
import com.lede.tech.rocketmq.easyclient.producer.EasyMQProducerFactory;
import com.lede.tech.rocketmq.easyclient.producer.assist.EasyMQMessageConfig;
import com.lede.tech.rocketmq.easyclient.producer.assist.EasyMQMessageDelayLevel;
import com.lede.tech.rocketmq.easyclient.producer.enums.ProducerTransferMode;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
public class TestProducer
{
	private static EasyMQProducer producer = EasyMQProducerFactory.getProducer();

	public static void main(String[] args)
	{

		long begin = System.currentTimeMillis();
		try
		{
			System.out.println("begin");
			System.out.println(begin);
			for (int i = 0; i < 100; i++)
			{
				sendAsync();
			}
			//			producer.sendMsg("topic20170118", "id1", "onlyyou");
			//sendAsyncUseConfig();
			//sendMessageOrderly();
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

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run()
			{
				producer.shutdown();
			}
		}));
		System.exit(0);
	}

	private static void sendAsync() throws MqWapperException, MqBussinessException
	{
		producer.sendMsg("topic20170119", "new444", "XX4", new SendCallback() {
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
	}

	private static void sendAsyncUseConfig() throws MqWapperException, MqBussinessException
	{
		EasyMQMessageConfig config = new EasyMQMessageConfig("topic20170118", "id2", "onlyU");
		config.setTransferMode(ProducerTransferMode.ASYNC);
		config.setCallback(new SendCallback() {
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
		producer.sendMsg(config);
	}

	private static void sendMessageOrderly() throws MqWapperException, MqBussinessException
	{
		for (int i = 0; i < 20; i++)
		{
			EasyMQMessageConfig config = new EasyMQMessageConfig("topic20170119-con", "" + i, "onlyU" + i);
			//config.setOrderTag("order2");
			producer.sendMsg(config);
		}
	}

	private static void sendMessageDelay() throws MqWapperException, MqBussinessException
	{
		EasyMQMessageConfig config = new EasyMQMessageConfig("topic20170119-delay", "id1", "onlyU1");
		config.setDelayLevel(EasyMQMessageDelayLevel.FIVE_MINUTES);
		producer.sendMsg(config);
	}
}
