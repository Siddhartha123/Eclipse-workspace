package com.mkyong.rest;
 
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloWorldService {
 
	@GET
	@Path("/query")
	public Response getObject(@QueryParam("param") String par) throws ClassNotFoundException, SQLException, InterruptedException {
		Connection con=(Connection)ThreadPool.getObject();
		Statement stmt=con.createStatement();
		String query = "select * from personal_details ;" ;
		ResultSet rs = stmt.executeQuery(query) ;
		if(rs.next())
			System.out.println("DB connnection successful");
		String s="hello world";
		istime(1000);
		ThreadPool.putObject((Object)con);
		return Response.status(200).entity(s).build();
	}
	@GET
	@Path("/addObject")
	public Response changePoolSize(@QueryParam("param") int n) throws ClassNotFoundException, SQLException, InterruptedException {
		ThreadPool.increasePoolSize();
		String s="Pool size increased";
		return Response.status(200).entity(s).build();
	}
	@GET
	@Path("/removeObject")
	public Response remove(@QueryParam("param") int n) throws ClassNotFoundException, SQLException, InterruptedException {
		ThreadPool.reducePoolSize(n);
		String s="Pool size decreased";
		return Response.status(200).entity(s).build();		
	}
	
	public void istime(int duration){
		long start_time=System.currentTimeMillis();
		while((System.currentTimeMillis()-start_time)<duration);
		//System.out.println(System.currentTimeMillis());
	}
}