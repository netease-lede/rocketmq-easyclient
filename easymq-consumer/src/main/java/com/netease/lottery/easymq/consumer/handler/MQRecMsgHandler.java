package com.netease.lottery.easymq.consumer.handler;

import java.util.List;

public interface MQRecMsgHandler
{
	public void handle(List<String> msg) throws Exception;
}
