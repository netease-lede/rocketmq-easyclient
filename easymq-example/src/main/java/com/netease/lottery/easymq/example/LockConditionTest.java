package com.netease.lottery.easymq.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionTest
{
	private static Lock lock = new ReentrantLock();
	private static Condition cond = lock.newCondition();
	private static volatile int count = 0;

	public static LockWait getLw()
	{
		return new LockWait();
	}

	public static LockNotify getLn()
	{
		return new LockNotify();
	}

	public static class LockWait implements Runnable
	{
		@Override
		public void run()
		{
			lock.lock();
			try
			{
				while (true)
				{
					count++;
					cond.await();
					count++;
				}

			}
			catch (InterruptedException e)
			{
			}
			finally
			{
				lock.unlock();
			}
		}

	}

	public static class LockNotify implements Runnable
	{

		@Override
		public void run()
		{
			lock.lock();
			try
			{
				cond.signalAll();
			}
			finally
			{
				lock.unlock();
			}
		}

	}

	public static void main(String[] args)
	{
		new Thread(LockConditionTest.getLw()).start();
		new Thread(LockConditionTest.getLw()).start();
		new Thread(LockConditionTest.getLw()).start();
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		}
		new Thread(LockConditionTest.getLn()).start();
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		}
		System.out.println(count);
	}

}