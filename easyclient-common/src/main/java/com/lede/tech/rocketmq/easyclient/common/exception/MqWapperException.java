package com.lede.tech.rocketmq.easyclient.common.exception;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
@SuppressWarnings("serial")
public class MqWapperException extends Exception
{
	public MqWapperException()
	{
		super();
	}

	public MqWapperException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MqWapperException(String message)
	{
		super(message);
	}

	public MqWapperException(Throwable cause)
	{
		super(cause);
	}

	public MqWapperException(Object message)
	{
		super(message.toString());
	}
}
