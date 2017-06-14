package com.mkyong.rest;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;

public class Pool extends HttpServlet{
	public static void init(int n) throws ClassNotFoundException, SQLException, ServletException{
		int i;
		for(i=n;i>0;i--){
    		jdbc_con db = new com.mkyong.rest.jdbc_con("com.mysql.jdbc.Driver","jdbc:mysql://localhost/cyborg","root","");
    		Connection con=db.getConnection();
    		System.out.println(con);
    		Object ob=(Object)con;
    		ThreadPool.ObjectPool.push(ob);
    	}	
	}
}
