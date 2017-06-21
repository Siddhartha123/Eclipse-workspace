package com.mkyong.rest;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;
import java.io.*;
import java.util.EmptyStackException;
public class initialize extends HttpServlet {
	
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
	private int interval=0;
	final static Logger logger=Logger.getLogger("Pool Manager");
	@Override
	public void run(){
		while(true){
			if(ThreadPool.ObjectPool.size()<ThreadPool.count.get())
				interval=100;
			else
				interval=5000;
			logger.debug("#active DB connections: "+ThreadPool.count.get());
			logger.debug("#free DB connections: "+ThreadPool.ObjectPool.size());
			logger.debug("#threads waiting: "+ThreadPool.available.getQueueLength());
			if(ThreadPool.count.get()<ThreadPool.maxSize && ThreadPool.available.hasQueuedThreads()){
				try {
					ThreadPool.increasePoolSize();
				} catch (ClassNotFoundException e1) {
					logger.error(e1);
				} catch (SQLException e1) {
					logger.error(e1);
				}
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
					logger.error(e);
				}
			}
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
	}
}
