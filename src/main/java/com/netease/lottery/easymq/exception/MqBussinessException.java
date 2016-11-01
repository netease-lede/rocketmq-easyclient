package com.netease.lottery.easymq.exception;

@SuppressWarnings("serial")
public class MqBussinessException extends RuntimeException
{
	public MqBussinessException()
	{
		super();
	}

	public MqBussinessException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MqBussinessException(String message)
	{
		super(message);
	}

	public MqBussinessException(Throwable cause)
	{
		super(cause);
	}

	public MqBussinessException(Object message)
	{
		super(message.toString());
	}
}
