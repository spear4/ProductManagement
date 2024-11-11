package com.product.management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.StructuredTaskScope.ShutdownOnSuccess;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class LoginServer {

	private static final String predefinedUser = "rehmat@gmail.com";
	private static final String predefinedPass = "Rehmat@1001";
	
	public static void main(String[] args) throws IOException {
		
		//Creating a server for login
		HttpServer loginServer = HttpServer.create(new InetSocketAddress(9001), 0);
		loginServer.createContext("/login", new LoginHandler()); // creating a end-point
		loginServer.setExecutor(null); // setting the default executor for handling request
		loginServer.start(); // start the server
		System.out.println("Login Server listening at port 9001 . . .");
		
	}
	
	
	static class LoginHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange exchange) throws IOException {

			//CORS setup
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
			exchange.getResponseHeaders().set("Access-Control-Allow-Method", "POST, OPTIONS");
			exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Context-Type, Authorization");
			
			//handles pre-flight request
			if ("OPTIONS".equals(exchange.getRequestMethod())) {
				exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
			    exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
			    exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
			    exchange.sendResponseHeaders(200, -1); // 200 OK, no body for OPTIONS
			    return;
			}
			
			if("POST".equals(exchange.getRequestMethod())) {
				
				//read the user request data
				InputStreamReader insr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
				BufferedReader bfr = new BufferedReader(insr);
				
				//Store each line in a StringBuilder
				StringBuilder inpStringBuilder = new StringBuilder();
				String line;
				while((line = bfr.readLine()) != null) {
					inpStringBuilder.append(line);	
				}
				
				
				//reading the JSON input
				JSONObject jsonObj = new JSONObject(inpStringBuilder.toString());
				
				//separating values from front-end
				String user = jsonObj.getString("user");
				String pass = jsonObj.getString("pass");
				
				if(user.equalsIgnoreCase(predefinedUser)) {
					
					if(pass.equals(predefinedPass)) {
						
						String successResponse = "Welcome to PM";
						exchange.sendResponseHeaders(201, successResponse.getBytes().length);
						OutputStream ous = exchange.getResponseBody();
						ous.write(successResponse.getBytes());
						ous.close();
						
					}else {
						
						String passMismatch = "Password not matching with the records";
						exchange.sendResponseHeaders(422, passMismatch.getBytes().length);//sends an validation error to notify the customer
						OutputStream oup = exchange.getResponseBody();
						oup.write(passMismatch.getBytes());
						oup.close();
						
					}
					
				}else {
					
					String userMismatch = "! You are not registered with us";
					exchange.sendResponseHeaders(401, userMismatch.getBytes().length);//sends an unauthorized error Code					
					OutputStream oum = exchange.getResponseBody();
					oum.write(userMismatch.getBytes());
					oum.close();
					
				}
				
				
				
			}
			
			
		}
		
		
		
	}

}
