package com.mkyong.rest;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class jdbc_con{
    int num_conn;
    boolean is_connected=false;
    Connection connection;
    public jdbc_con(String driver,String url,String user, String pass) throws SQLException, ClassNotFoundException {
    // Load the JDBC driver
     Class.forName(driver);
    
    // Connect to a database
    connection = DriverManager.getConnection
      (url, user,pass);
    is_connected=true;
    }
    public Connection getConnection(){
        return this.connection;
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException{
       List<Connection> connections=new ArrayList<Connection>(); 
        jdbc_con db=new jdbc_con("com.mysql.jdbc.Driver","jdbc:mysql//localhost/cyborg","root","");
        if(db.is_connected){
            Connection con=db.getConnection();
            connections.add(con);
            System.out.println(connections);
        }
    }
}
