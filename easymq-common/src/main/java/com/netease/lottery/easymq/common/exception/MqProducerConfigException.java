package com.netease.lottery.easymq.common.exception;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
@SuppressWarnings("serial")
public class MqProducerConfigException extends RuntimeException
{
	public MqProducerConfigException()
	{
		super();
	}

	public MqProducerConfigException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MqProducerConfigException(String message)
	{
		super(message);
	}

	public MqProducerConfigException(Throwable cause)
	{
		super(cause);
	}

	public MqProducerConfigException(Object message)
	{
		super(message.toString());
	}
}
