package com.mkyong.rest;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import java.io.*;

public class initialize extends HttpServlet {
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws FileNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public initialize() throws ClassNotFoundException, SQLException, FileNotFoundException {
        int n=5;
        int i;
		for(i=n;i>0;i--){
    		jdbc_con db = new com.mkyong.rest.jdbc_con("com.mysql.jdbc.Driver","jdbc:mysql://localhost/cyborg","root","");
    		Connection con=db.getConnection(db);
    		System.out.println(con);
    		Object ob=(Object)con;
    		ThreadPool.ObjectPool.push(ob);
    	}	
		/*
		FileOutputStream f = new FileOutputStream("D:/log.txt");
	      System.setOut(new PrintStream(f));*/
    }
}
