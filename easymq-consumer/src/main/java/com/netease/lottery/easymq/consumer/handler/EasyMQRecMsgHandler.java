package com.netease.lottery.easymq.consumer.handler;

import java.util.List;

import com.alibaba.rocketmq.common.message.MessageExt;

public interface EasyMQRecMsgHandler
{
	public void handle(List<MessageExt> msg) throws Exception;
}
