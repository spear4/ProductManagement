package com.product.management;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.json.JSONObject;

public class ProductAddServer implements HttpHandler {
	
	//Httpserver creation
	public static void main(String[] args) throws IOException {
		
		//Creating a HttpServer 
		HttpServer postServer = HttpServer.create(new InetSocketAddress(9001), 0); // adding listener port and backlog
		postServer.createContext("/postproducts", new ProductAddServer()); // // creating an end-point to access this ProductPostApiServer
		postServer.setExecutor(null);
		postServer.start(); //start the server
		System.out.println("Product Post API Listening at port 9001...");
		
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		//CORS Setup
		exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
	    exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
	    exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
		
		//handles pre-flight request
		if ("OPTIONS".equals(exchange.getRequestMethod())) {
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
		    exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
		    exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
		    exchange.sendResponseHeaders(200, -1); // 200 OK, no body for OPTIONS
		    return;
		}
		
		if("POST".equals(exchange.getRequestMethod())) {
			
			//Read the user input using InputStreamReader
			InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
			
			BufferedReader bfr = new BufferedReader(isr);
			
			StringBuilder requestBody = new StringBuilder();
			
			String line;
			while((line = bfr.readLine()) != null) {
				requestBody.append(line);
			}
			
			// Parse the incoming JSON data
            JSONObject jsonRequest = new JSONObject(requestBody.toString());
            String name = jsonRequest.getString("name");
            String category = jsonRequest.getString("category");
            int price = jsonRequest.getInt("price");
            int quantity = jsonRequest.getInt("quantity");

            // Checking purpose
            System.out.println("Received product: " + name + ", " + category + ", " + price + ", " + quantity);
            
            // Insert product data into the database
            AddProduct addData = new AddProduct();
            boolean success = addData.addProduct(name, category, price, quantity);

            // Respond back to the client
            if (success) {
            	
            	//success response to the client
                String response = "Product added successfully!";
                exchange.sendResponseHeaders(201, response.getBytes().length); // sends success response
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes()); // writes response
                os.close();
                
            } else {
            	
            	//error response to the client
                String errorResponse = "Failed to add product to the database.";
                exchange.sendResponseHeaders(500, errorResponse.getBytes().length); // sends error response
                OutputStream os = exchange.getResponseBody();
                os.write(errorResponse.getBytes());// writes response
                os.close();
                
            }
			
		}else {
			
			// Handle non-POST methods
            exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
			
		}
		
		
	}
	

}
