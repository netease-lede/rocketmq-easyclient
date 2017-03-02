package com.lede.tech.rocketmq.easyclient.consumer.handler;

import java.util.List;

import com.lede.tech.rocketmq.easyclient.consumer.bean.MessageBean;

/**
 * 
 * @Desc 
 * @Author bjguosong
 */
public interface EasyMQRecMsgHandler
{
	public void handle(List<MessageBean> msg) throws Exception;
}
