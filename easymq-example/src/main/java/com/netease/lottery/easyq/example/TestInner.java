package com.netease.lottery.easyq.example;

public class TestInner
{

	public static class Inner
	{
		public void getBString()
		{
			System.out.println("b");
			getAString();
		}

	}

	private static void getAString()
	{
		System.out.println("a");
	}

	public static void main(String[] args)
	{
		TestEnum.A.print();
		TestEnum.B.print();
	}
}
