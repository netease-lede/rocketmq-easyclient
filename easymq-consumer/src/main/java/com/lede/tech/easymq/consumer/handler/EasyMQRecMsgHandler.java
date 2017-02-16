package com.lede.tech.easymq.consumer.handler;

import java.util.List;

import com.lede.tech.easymq.consumer.bean.MessageBean;

/**
 * 
 * @Desc 
 * @Author bjguosong
 */
public interface EasyMQRecMsgHandler
{
	public void handle(List<MessageBean> msg) throws Exception;
}
