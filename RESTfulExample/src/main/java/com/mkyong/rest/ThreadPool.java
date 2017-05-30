package com.mkyong.rest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
	static int i= 0;
	static int n=3;
	public jdbc_con db;
	public static String output;
	static Stack<Object> ObjectPool = new Stack<Object>();
    public static LinkedBlockingQueue<Runnable> queue=new LinkedBlockingQueue<Runnable>();
    public static ExecutorService executor =new ThreadPoolExecutor(n,n,0,TimeUnit.MILLISECONDS,queue);
    
   /* ThreadPool(int n) throws ClassNotFoundException, SQLException{
    	for(i=n;i>0;i--){
    		db = new jdbc_con("com.mysql.jdbc.Driver","jdbc:mysql://localhost/cyborg","root","");
    		Connection con=db.getConnection(db);
    		System.out.println(con);
    		Object ob=(Object)con;
    		ObjectPool.push(ob);
    	}
    	
    }*/
    
    public static void getPoolObject(){
    	i++;
    	Runnable worker = new WorkerThread("p" + i, (int) ((Math.random()*10000)%5000));
        executor.execute(worker);
    }
}
class WorkerThread implements Runnable{
    private String message;
    Object object;
    public boolean isComplete=false;
    int duration;   
    public WorkerThread(String s,int duration){
        this.duration=duration;
        //System.out.println("objects free: "+Poolnew.ObjectPool.size());
        this.message=s;
    }
    
    @Override
    public void run() {
    	System.out.println(ThreadPool.queue.size()+" threads waiting in queue.");
        long current_time=System.currentTimeMillis();  
        //System.out.println(current_time);
        System.out.println(Thread.currentThread().getName()+" (Start)"+message);
        //System.out.println("-"+this.duration);
        //istime(current_time);
        while(ThreadPool.ObjectPool.empty());
        System.out.println(Thread.currentThread().getName()+" (End)");
        this.isComplete=true;
        //p.free++;
    }
    public void istime(long start_time){
        while((System.currentTimeMillis()-start_time)<duration);
        //System.out.println(System.currentTimeMillis());
    }
    
 
    
}