package com.mkyong.rest;

import java.sql.*;  
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.sql.*;  
import java.util.concurrent.Semaphore;
public class ThreadPool {
	public static final int MAX_AVAILABLE = 10;
	public static int baseSize=5;
	public static final Semaphore available = new Semaphore(baseSize, true);
	public static Stack<Object> ObjectPool = new Stack<Object>();

	public static Object getObject() throws InterruptedException {
		available.acquire();
		return getAvailableItem();
	}

	public static void putObject(Object x) {
		ObjectPool.push(x);
		System.out.println("Object returned to pool");
		System.out.println("Current Pool size: "+ThreadPool.ObjectPool.size());
		available.release();
	}

	private static synchronized Object getAvailableItem() {
		if(ObjectPool.empty())
			return null;
		System.out.println("Object removed from pool");
		Object ob=ObjectPool.pop();
		System.out.println("Current Pool size: "+ThreadPool.ObjectPool.size());
		System.out.println("Number of threads waiting in queue: "+ThreadPool.available.getQueueLength());
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

	public static void increasePoolSize(int n){
		int inc;
		if(ThreadPool.ObjectPool.size()+n>ThreadPool.MAX_AVAILABLE)
			inc=ThreadPool.MAX_AVAILABLE-ThreadPool.ObjectPool.size();
		else
			inc=n;
		ThreadPool.available.release(inc);
	}
}

class WorkerThread implements Runnable{
	private String message; 
	Object object;
	public boolean isComplete=false;
	int duration;   
	Connection conn;
	public WorkerThread(String s,int duration){
		this.duration=duration;
		this.message=s;
	}

	@Override
	public void run() {

		long current_time=System.currentTimeMillis();  
		System.out.print(Thread.currentThread().getName()+" (Start)"+message);
		System.out.println("-"+this.duration);
		while(ThreadPool.ObjectPool.empty());
		synchronized(ThreadPool.ObjectPool){
			while(ThreadPool.ObjectPool.empty());
			conn=(Connection)ThreadPool.ObjectPool.pop();
		}
		System.out.println("objects free: "+ThreadPool.ObjectPool.size());
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String query = "select * from personal_details ;" ;
			ResultSet rs = stmt.executeQuery(query) ;
			if(rs.next())
				System.out.println("database connection successful");  
		} catch (SQLException e) {
			e.printStackTrace();
		}

		istime(current_time);
		System.out.println(Thread.currentThread().getName()+" (End)"+message);
		this.isComplete=true;
		ThreadPool.ObjectPool.push(conn);
		System.out.println("objects free: "+ThreadPool.ObjectPool.size());
	}

	public void istime(long start_time){
		while((System.currentTimeMillis()-start_time)<duration);
		//System.out.println(System.currentTimeMillis());
	}
}