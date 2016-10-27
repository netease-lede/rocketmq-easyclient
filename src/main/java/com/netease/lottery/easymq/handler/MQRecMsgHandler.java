package com.netease.lottery.easymq.handler;

import java.util.List;

public interface MQRecMsgHandler
{
	public void handle(List<String> msg) throws Exception;

	public void registerHandler(String topic, MQRecMsgHandler handler);
}
