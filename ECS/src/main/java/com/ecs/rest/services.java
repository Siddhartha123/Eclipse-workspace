package com.ecs.rest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/hello")
public class services {
 
	@GET
	@Path("/query")
	public Response getObject(@QueryParam("param") String par) {
		String s="hello world";
		return Response.status(200).entity(s).build();
	}
}