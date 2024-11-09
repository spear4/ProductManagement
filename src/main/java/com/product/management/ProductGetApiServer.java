package com.product.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductGetApiServer implements HttpHandler {

	static final String dbUrl = "jdbc:postgresql://localhost:5432/sanmauto";
	static final String dbUser = "postgres";
	static final String dbPass = "postql"; 
	
	public static void main(String[] args) {
		
		//create a HttpServer
		HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress(8087), 0); // adding listener port and backlog
			server.createContext("/get-products", new ProductGetApiServer()); // creating an end-point to access this ProductGetApiServer
			server.start();//start the connection 
//			System.out.println("Server is listening on port 8087...");	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//GET API response
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		String reqMethod = "GET"; // should use this same method in client request
		
		if(reqMethod.equals(exchange.getRequestMethod())) {
			
			//get the list of products
			List<Product> products = getProducts();
			
			//JSON conversion of product list
			ObjectMapper objectMapper = new ObjectMapper();
			
			//Use jack-son data-bind and core V.15.0
			String jsonResponse = objectMapper.writeValueAsString(products);
			
			//Setting response headers
			exchange.getResponseHeaders().set("Content-Type", "application/json"); // indicating that response content type is JSON
			exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);// To let the user know that request was successful
			
			//Writing JSON response in OutputStream
			OutputStream os = exchange.getResponseBody();// Pushing the response to the client
			os.write(jsonResponse.getBytes());// writes the JSON response to the Output Stream
			os.close();
			
		}else {
			
			//if the client sends different request rather than 'GET'
			exchange.sendResponseHeaders(500, -1);
			
		}
		
	}
	
	
	//fetching data from db
	private List<Product> getProducts(){
		
		List<Product> products = new ArrayList<>();
		
		Connection selConn = null;
		PreparedStatement selStmt = null;
		
		String selQuery = "SELECT * FROM ProductData";
		
		try {
			
			//Connection establishment
			selConn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			selStmt = selConn.prepareStatement(selQuery);
			
			//Catch the result after statement execution
			ResultSet result = selStmt.executeQuery();
			
			while(result.next()) {

				products.add(new Product(
						result.getInt("id"), 
						result.getString("productname"), 
						result.getString("productcategory"),
						result.getInt("productprice"),
						result.getInt("productquantity")
						));
			
			}
			
		} catch (Exception e) {
			
			// TODO: handle exception
			e.printStackTrace();
			
		}finally {
			
			// Ensuring resources are closed
            try {
                if (selStmt != null) {
                	selStmt.close();
                }
                if (selConn != null) {
                	selConn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
		}
		
		return products;
	}
	
	
	

}
