package com.mkyong.rest;

import java.sql.*;  
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;
import java.sql.*;  
import java.util.concurrent.Semaphore;

public class ThreadPool {
	final static Logger logger=Logger.getLogger(ThreadPool.class);
	public static AtomicInteger count=new AtomicInteger(0);
	public static int maxSize = 10;
	public static int baseSize=5;
	public static final Semaphore available = new Semaphore(baseSize, true);
	public static Stack<Object> ObjectPool = new Stack<Object>();
	public static boolean manage=false;
	public static Object getObject() throws InterruptedException {
		available.acquire();
		return getAvailableItem();
	}

	public static void putObject(Object x) {
		ObjectPool.push(x);
		available.release();
		logger.info("Object returned to pool");
		logger.info("Current Pool size: "+ThreadPool.ObjectPool.size());
	}

	private static synchronized Object getAvailableItem() {
		while(ObjectPool.empty());
		logger.info("Object removed from pool");
		Object ob=ObjectPool.pop();
		//ThreadPool.count.decrementAndGet();
		logger.info("Current Pool size: "+ThreadPool.ObjectPool.size());
		logger.info("Number of threads waiting in queue: "+ThreadPool.available.getQueueLength());
		return ob;
	}

	public static void reducePoolSize(int n){
		int reduction;
		if((ThreadPool.ObjectPool.size()-n)<ThreadPool.baseSize)
			reduction=ThreadPool.ObjectPool.size()-ThreadPool.baseSize;
		else
			reduction=n;
		//ThreadPool.available.reducePermits(reduction);
	}

	public static void increasePoolSize() throws ClassNotFoundException, SQLException{
			jdbc_con db=new jdbc_con("com.mysql.jdbc.Driver","jdbc:mysql://localhost/cyborg","root","");
			Object x=(Object)db.getConnection();
			ThreadPool.putObject(x);
			ThreadPool.count.incrementAndGet();
	}
}
