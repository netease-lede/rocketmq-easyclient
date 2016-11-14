package com.netease.lottery.easymq.example;

public enum TestEnum
{
	A
	{
		@Override
		public void print()
		{
			printC();
			System.out.println("a");
		}
	},
	B
	{
		@Override
		public void print()
		{
			printC();
			System.out.println("b");
		}
	};

	private TestEnum()
	{

	}

	public void print()
	{

	}

	private static void printC()
	{
		System.out.println("c");
	}
}
