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
		logger.info("Object returned to pool by:"+Thread.currentThread().getName());
	}

	private static synchronized Object getAvailableItem() {
		while(ObjectPool.empty());
		logger.info("Object removed from pool by:"+Thread.currentThread().getName());
		Object ob=ObjectPool.pop();
		return ob;
	}

	public static void increasePoolSize() throws ClassNotFoundException, SQLException{
			jdbc_con db=new jdbc_con("com.mysql.jdbc.Driver","jdbc:mysql://localhost/cyborg","root","");
			Object x=(Object)db.getConnection();
			ThreadPool.putObject(x);
			ThreadPool.count.incrementAndGet();
	}
}
