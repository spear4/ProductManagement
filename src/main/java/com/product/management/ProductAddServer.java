package com.product.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductAddServer implements HttpHandler {
	
	//Httpserver creation
	public static void main(String[] args) {
		
		try {
			
			//Creating a HttpServer 
			HttpServer postServer = HttpServer.create(new InetSocketAddress(9001), 0); // adding listener port and backlog
			postServer.createContext("/postproducts", new ProductAddServer()); // // creating an end-point to access this ProductPostApiServer
			postServer.setExecutor(null);
			postServer.start(); //start the server
			System.out.println("Product Post API Listening at port 9001...");
			
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		//Checking for POST request
		if("POST".equals(exchange.getRequestMethod())) {//the client request should be same
		
			//Reading the request from client
			InputStream inp = exchange.getRequestBody();
		
			//Mapping the product data to JSON
			ObjectMapper obm = new ObjectMapper();
			Product product = obm.readValue(inp, Product.class);
		
			//Adding data in the database // and getting number of rows affected
			int rowsAffected = 0;
		
			//Response of successful product addition
			String response = (rowsAffected > 0) ? "Product added Successfully" : "Failed to add product";

		}else {
			
			exchange.sendResponseHeaders(500, -1);
			
		}
		
		
	}
	

}
