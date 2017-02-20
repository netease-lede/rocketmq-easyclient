package com.lede.tech.rocketmq.easyclient.producer.assist;

/**
 * 
 * @Desc 
 * @Author ykhu
 */
public enum EasyMQMessageDelayLevel
{
	ONE_SECOND(1, "1s"), FIVE_SECONDS(2, "5s"), TEN_SECONDS(3, "10s"), THIRTY_SECONDS(4, "30s"), ONE_MINUTE(5,
			"1m"), TWO_MINUTES(6, "2m"), THREE_MINUTES(7, "3m"), FOUR_MINUTES(8, "4m"), FIVE_MINUTES(9,
					"5m"), SIX_MINUTES(10, "6m"), SEVEN_MINUTES(11, "7m"), EIGHT_MINUTES(12, "8m"), NINE_MINUTES(13,
							"9m"), TEN_MINUTES(14, "10m"), TWENTY_MINUTES(15,
									"20m"), THIRTY_MINUTES(16, "30m"), ONE_HOUR(17, "1h"), TWO_HOURS(18, "2h");
	private int delayLevel;
	private String desc;

	private EasyMQMessageDelayLevel(int delayLevel, String desc)
	{
		this.delayLevel = delayLevel;
		this.desc = desc;
	}

	public int getDelayLevel()
	{
		return delayLevel;
	}

	public void setDelayLevel(int delayLevel)
	{
		this.delayLevel = delayLevel;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}
}
