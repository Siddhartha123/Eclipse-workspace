package com.mkyong.rest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;

public class Objectpool {
	public jdbc_con db;
    static Stack<Object> ObjectPool = new Stack<Object>();
    int n,free;
    Objectpool(int num) throws SQLException, ClassNotFoundException{
        free=n=num;
        int i;
        for(i=n;i>0;i--){
          db = new jdbc_con("com.mysql.jdbc.Driver","jdbc:mysql://localhost/cyborg","root","");
          Connection con=db.getConnection(db);
          Object ob=(Object)con;
          ObjectPool.push(ob);
        }
    }
	
}
