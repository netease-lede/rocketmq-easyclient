package com.netease.lottery.easymq.consumer.handler;

import java.util.List;

import com.netease.lottery.easymq.consumer.bean.MessageBean;

public interface EasyMQRecMsgHandler
{
	public void handle(List<MessageBean> msg) throws Exception;
}
