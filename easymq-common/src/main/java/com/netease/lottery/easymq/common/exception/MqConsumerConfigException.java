package com.netease.lottery.easymq.common.exception;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
@SuppressWarnings("serial")
public class MqConsumerConfigException extends RuntimeException
{
	public MqConsumerConfigException()
	{
		super();
	}

	public MqConsumerConfigException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MqConsumerConfigException(String message)
	{
		super(message);
	}

	public MqConsumerConfigException(Throwable cause)
	{
		super(cause);
	}

	public MqConsumerConfigException(Object message)
	{
		super(message.toString());
	}
}
