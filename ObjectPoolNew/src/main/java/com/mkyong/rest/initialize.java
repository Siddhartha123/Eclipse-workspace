package com.mkyong.rest;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import java.io.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.*;
public class initialize extends HttpServlet {
	/**
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 * @see HttpServlet#HttpServlet()
	 */
	public initialize() throws ClassNotFoundException, SQLException, FileNotFoundException {
		int i;
		for(i=ThreadPool.baseSize;i>0;i--){
			jdbc_con db = new jdbc_con("com.mysql.jdbc.Driver","jdbc:mysql://localhost/cyborg","root","");
			Connection con=db.getConnection();
			System.out.println(con);
			Object ob=(Object)con;
			ThreadPool.ObjectPool.push(ob);
			ThreadPool.count.incrementAndGet();
		}	
		Manager ThreadManager=new Manager();
		ThreadManager.start();
		FileOutputStream f = new FileOutputStream("D:/log.txt");
		System.setOut(new PrintStream(f));
	}
}

class Manager extends Thread{
	@Override
	public void run(){
		while(true){
			System.out.println("count: "+ThreadPool.count.get());
			if(ThreadPool.count.get()==ThreadPool.baseSize && ThreadPool.available.hasQueuedThreads())
				try {
					ThreadPool.increasePoolSize();
				} catch (ClassNotFoundException e1) {
					System.out.println(e1);
				} catch (SQLException e1) {
					System.out.println(e1);
				}
			else if(ThreadPool.count.get()>(ThreadPool.baseSize+1) && !ThreadPool.available.hasQueuedThreads())
			{
				try {
					((Connection)ThreadPool.ObjectPool.pop()).close();
					ThreadPool.count.decrementAndGet();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
	}
}
