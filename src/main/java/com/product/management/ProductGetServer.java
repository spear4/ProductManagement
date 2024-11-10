package com.product.management;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ProductGetServer {
	
	private static final Logger logger = Logger.getLogger(ProductGetServer.class.getName());
	
	
	public static void main(String[] args) throws IOException {
		
		try {
			
			//Creating the Http Server
			HttpServer getServer = HttpServer.create(new InetSocketAddress(8889), 0); //setting port number and backlog, 0 by default(accepts number of connection based on running machine)
			getServer.createContext("/getproducts", new GetHandler());//setting end-point
			getServer.setExecutor(null); // executor for handling the request - use default executor(null)
			getServer.start(); // start the server
			System.out.println("Sever is running in the port 8889 . . .");
			
			//Checking purpose
			// FetchData fetch = new FetchData();
			// for(Product products : fetch.getProducts()) {
				// System.out.println(products);
			// }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	static class GetHandler implements HttpHandler  {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			
			//instance of the db class
			FetchData fetch = new FetchData();
			
			//JSON response
			StringBuilder jsonResponse = new StringBuilder();
			
			//Structuring the JSON
			jsonResponse.append("[");
			
			for(int i = 0; i < fetch.getProducts().size(); i++) {
				
				//getting the values from the encapsulation
				Product product = fetch.getProducts().get(i);
				
				jsonResponse.append("{")
				.append("\"id\":").append(product.getId()).append(",")
				.append("\"name\":\"").append(product.getName()).append("\",")
				.append("\"category\":\"").append(product.getCategory()).append("\",")
				.append("\"price\":").append(product.getPrice()).append(",")
				.append("\"quantity\":").append(product.getQuantity())
				.append("}");
				
				
				//If required to sent empty json
				if(i < fetch.getProducts().size() - 1) {
					jsonResponse.append(",");
				}
				
			}
			
			jsonResponse.append("]");
			
			//Setting CORS headers
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");//Providing cross-origin access
			exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET");// Allowed only GET method for product fetching
			exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");//allowed header types
			
			//response body
			exchange.getResponseHeaders().set("Content-Type", "application/json");//setting response type as JSON
			
			//converting the jsonResponse into byteArray coz HTTP is a Binary protocol
			byte[] responseBytes = jsonResponse.toString().getBytes(); 
			
			
			//send response
			exchange.sendResponseHeaders(200, jsonResponse.length());
			
			//writing the response
			OutputStream os = exchange.getResponseBody();
			os.write(jsonResponse.toString().getBytes());
			os.close();
			
		}
		
	}
	
	
}
