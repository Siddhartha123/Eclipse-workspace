package com.ecs.rest;
 
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
@Path("/services")
public class services {
 
	@GET
	@Path("/login")
	public Response login(@QueryParam("user") String user,@QueryParam("pass") String pass) throws ClassNotFoundException, SQLException, InterruptedException {
		// Load the JDBC driver
		Class.forName("com.mysql.jdbc.Driver");
	    // Connect to a database
	    Connection con= DriverManager.getConnection("jdbc:mysql://localhost/ecs","root","");
	    Statement stmt=con.createStatement();
		String query = "select * from user_profiles where User_Nam='"+user+"' and Secret_Key='"+pass+"';";
		ResultSet rs = stmt.executeQuery(query) ;
		String s="";
		if(rs.next())
			s="Login successful";
		else
			s="Login Failed";
		return Response.status(200).entity(s).build();
	}
	
	@GET
	@Path("/app")
	public Response appOnboard(@QueryParam("app_name") String appName,@QueryParam("war") String war,@QueryParam("sql") String sql) throws ClassNotFoundException, SQLException, IOException{
		// Load the JDBC driver
				Class.forName("com.mysql.jdbc.Driver");
			    // Connect to a database
			    Connection con= DriverManager.getConnection("jdbc:mysql://localhost/ecs","root","");
			    Statement stmt=con.createStatement();
			    
			    String query = "insert into app_master (app_name,app_url) values ('"+appName+"','localhost:8080/"+appName+"');";
			    stmt.executeUpdate(query);
			    
			    ResultSet rs = stmt.executeQuery("select app_id from app_master where app_name='"+appName+"';") ;
			    int id = rs.getInt(0);
			    
			    
			    byte[] decodedWar = Base64.getDecoder().decode(war);
			    FileOutputStream fos = new FileOutputStream("C:\\apache-tomee-plume-7.0.3\\webapps\\"+id+"_"+appName+".war");
			    fos.write(decodedWar);
			    fos.close();
			    
			    
			    byte[] decodedsql = Base64.getDecoder().decode(sql);
			    FileOutputStream fos1 = new FileOutputStream("C:\\apache-tomee-plume-7.0.3\\sql\\"+id+"_"+appName+".sql");
			    fos1.write(decodedsql);
			    fos1.close();
			    
			    stmt.executeUpdate("update table app_master set app_status='ACTIVE' where app_id="+id+";");
			    
			    String s="hello world";
			    System.out.println("Application onboarded");
			    return Response.status(200).entity(s).build();
				
	}
}