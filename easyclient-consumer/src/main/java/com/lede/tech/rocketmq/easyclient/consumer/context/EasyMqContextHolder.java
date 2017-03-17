package com.lede.tech.rocketmq.easyclient.consumer.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class EasyMqContextHolder implements ApplicationContextAware
{
	private static ApplicationContext applicationContext;

	/**
	 * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
	{
		EasyMqContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name)
	{
		if (applicationContext == null)
		{
			return null;
		}
		return (T) applicationContext.getBean(name);
	}
}
