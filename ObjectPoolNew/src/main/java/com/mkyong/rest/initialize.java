package com.mkyong.rest;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import java.io.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
			Connection con=db.getConnection(db);
			System.out.println(con);
			Object ob=(Object)con;
			ThreadPool.ObjectPool.push(ob);
		}	

		FileOutputStream f = new FileOutputStream("D:/log.txt");
		System.setOut(new PrintStream(f));
	}
}

class Manager extends Thread{
	@Override
	public void run(){
		while(true){
			if(ThreadPool.ObjectPool.size()==0 && ThreadPool.available.hasQueuedThreads())
				ThreadPool.increasePoolSize(5);
			else if(ThreadPool.ObjectPool.size()>0 && !ThreadPool.available.hasQueuedThreads())
				
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
	}
}
