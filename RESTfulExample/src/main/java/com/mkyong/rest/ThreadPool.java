package com.mkyong.rest;

import java.sql.*;  
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.sql.*;  

public class ThreadPool {
	static int i= 0;
	static int n=5;
	public static String output;
	public static Stack<Object> ObjectPool = new Stack<Object>();
    public static LinkedBlockingQueue<Runnable> queue=new LinkedBlockingQueue<Runnable>();
    public static ExecutorService executor =new ThreadPoolExecutor(n,n,0,TimeUnit.MILLISECONDS,queue);
    
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
        this.message=s;
    }
    
    @Override
    public void run() {
    	System.out.println(ThreadPool.queue.size()+" threads waiting in queue.");
        long current_time=System.currentTimeMillis();  
        System.out.print(Thread.currentThread().getName()+" (Start)"+message);
        System.out.println("-"+this.duration);
        while(ThreadPool.ObjectPool.empty());
        Connection conn=(Connection)ThreadPool.ObjectPool.pop();
        System.out.println("objects free: "+ThreadPool.ObjectPool.size());

        Statement stmt;
		try {
			stmt = conn.createStatement();
			String query = "select * from personal_details ;" ;
	        ResultSet rs = stmt.executeQuery(query) ;
	        if(rs.next())
	        System.out.println("database connection successful");  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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