package com.ecs.rest;
 
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}