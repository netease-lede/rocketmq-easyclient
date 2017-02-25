package com.lede.tech.rocketmq.easyclient.consumer.holder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Repository;

@SuppressWarnings("rawtypes")
@Repository
public class SpringContextHolder implements ApplicationContextAware, ApplicationListener
{
	private static ApplicationContext applicationContext;
	private static boolean hasLoaded = false;
	private static boolean hasClosed = false;

	/**
	 * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
	{
		SpringContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext()
	{
		checkApplicationContext();
		return applicationContext;
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name)
	{
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}

	private static void checkApplicationContext()
	{
		if (applicationContext == null)
		{
			throw new IllegalStateException("applicaitonContext未注入");
		}
	}

	public static boolean containsBean(String beanName)
	{
		checkApplicationContext();
		if (hasClosed)
		{
			return false;
		}
		return applicationContext.containsBean(beanName);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event)
	{
		if (event instanceof ContextRefreshedEvent)
		{
			hasLoaded = true;
		}
		if (event instanceof ContextClosedEvent || event instanceof ContextStoppedEvent)
		{
			hasClosed = true;
		}
	}

	public static boolean isSpringContextLoaded()
	{
		return hasLoaded;
	}

	public static boolean isSpringContextClosed()
	{
		return hasClosed;
	}
}
