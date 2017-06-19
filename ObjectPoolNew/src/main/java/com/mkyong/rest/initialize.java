package com.mkyong.rest;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;
import java.io.*;
import java.util.EmptyStackException;
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
	final static Logger logger = Logger.getLogger(initialize.class);
	public initialize() throws ClassNotFoundException, SQLException, FileNotFoundException {
		int i;
		for(i=ThreadPool.baseSize;i>0;i--){
			jdbc_con db = new jdbc_con("com.mysql.jdbc.Driver","jdbc:mysql://localhost/cyborg","root","");
			Connection con=db.getConnection();
			logger.info("DB connection created: "+con);
			Object ob=(Object)con;
			ThreadPool.ObjectPool.push(ob);
			ThreadPool.count.incrementAndGet();
		}	
		Manager ThreadManager=new Manager();
		ThreadManager.start();
	}
}

class Manager extends Thread{
	final static Logger logger=Logger.getLogger(Manager.class);
	@Override
	public void run(){
		while(true){
			logger.debug("No. of active DB connections: "+ThreadPool.count.get());
			if(ThreadPool.count.get()<ThreadPool.maxSize && ThreadPool.available.hasQueuedThreads())
				try {
					ThreadPool.increasePoolSize();
				} catch (ClassNotFoundException e1) {
					logger.error(e1);
				} catch (SQLException e1) {
					logger.error(e1);
				}
			else if(ThreadPool.count.get()>ThreadPool.baseSize && !ThreadPool.available.hasQueuedThreads())
			{
				try {
					ThreadPool.available.acquire();
					((Connection)ThreadPool.ObjectPool.pop()).close();
					ThreadPool.count.decrementAndGet();
				} catch (SQLException e) {
					logger.error(e);
				} catch (InterruptedException e) {
					logger.error(e);
				}
				catch(EmptyStackException e){
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
	}
}
