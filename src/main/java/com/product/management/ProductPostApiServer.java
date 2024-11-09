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

public class ProductPostApiServer implements HttpHandler {

	//JDBC connection creds& Url
	static final String dbUrl = "jdbc:postgresql://localhost:5432/sanmauto";
	static final String dbUser = "postgres";
	static final String dbPass = "postql"; 
	
	public static void main(String[] args) {
		
		try {
			//Creating a HttpServer 
			HttpServer postServer = HttpServer.create(new InetSocketAddress(8085), 0); // adding listener port and backlog
			postServer.createContext("/post-products", new ProductPostApiServer()); // // creating an end-point to access this ProductPostApiServer
			postServer.start(); //start the server
//			System.out.println("Product Post API Listening at port 8085...");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		if("POST".equals(exchange.getRequestMethod())) {//the client request should be same
		
			//Reading the request from client
			InputStream inp = exchange.getRequestBody();
		
			//Mapping the product data to JSON
			ObjectMapper obm = new ObjectMapper();
			Product product = obm.readValue(inp, Product.class);
		
			//Adding data in the database // and getting number of rows affected
			int rowsAffected = addProduct(product);
		
			//Response of successful product addition
			String response = (rowsAffected > 0) ? "Product added Successfully" : "Failed to add product";
		
			//	API response body
			exchange.getResponseHeaders().set("Content-Type", "application/json"); // setting response in JSON format
			exchange.sendResponseHeaders(201, response.getBytes().length); // Letting the client know of the request status is success
		
			OutputStream ous = exchange.getResponseBody();
			ous.write(response.getBytes());
			ous.close();

		}else {
			exchange.sendResponseHeaders(500, -1);
		}
		
		
	}

	
	private int addProduct(Product product) {
		
		int row = 0;
	
		//db connection parameters
		Connection inCon = null;
		PreparedStatement inpPrepStmt = null;
		
		//Insert Query
		String inQuery = "INSERT INTO ProductData(productname, productcategory, productprice, productquantity)"
				+ "VALUES (?, ?, ?, ?)";
		
		try {
			
			//establish connection
			inCon = DriverManager.getConnection(dbUrl);
			inpPrepStmt = inCon.prepareStatement(inQuery);
			
			//returns row count
			row = inpPrepStmt.executeUpdate();			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			
			// Ensuring resources are closed
            try {
                if (inpPrepStmt != null) {
                	inpPrepStmt.close();
                }
                if (inCon != null) {
                	inCon.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
		}
		
		return row;
	}
	
	

}
