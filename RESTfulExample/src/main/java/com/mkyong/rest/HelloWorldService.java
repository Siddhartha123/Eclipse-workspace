package com.mkyong.rest;
 
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
@Path("/hello")
public class HelloWorldService {
 
	@GET
	@Path("/query")
	public Response getMsg(@QueryParam("param") String par) throws ClassNotFoundException, SQLException, InterruptedException {
		ThreadPool t=new ThreadPool();
		ThreadPool.getPoolObject();
		String s="hello";
		return Response.status(200).entity(s).build();
		//ThreadPool.waitFor();
		//return Response.status(200).entity(output).build();
 
	}
 
}