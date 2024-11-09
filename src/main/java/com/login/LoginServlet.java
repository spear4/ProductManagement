package com.login;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet{
	
	private static final String predefinedUser = "rehmat@gmail.com";
	private static final String predefinedPass = "Rehmat@1001";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//PrintWriters used to return the results to the front end
		PrintWriter printOut = response.getWriter();
		
		//getting input from front end
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");
		
		//matching the user credentials with pre-defined ones
		if(predefinedUser.equalsIgnoreCase(userName)) {
			
			if(predefinedPass.equals(passWord)) {
				
				printOut.print("Welcome");
				
//				response.sendRedirect("product.html");
				
			}else {
				printOut.print("Password Mismatching");
			}
			
		}else {
			printOut.print("Not a User");
		}
		
	}

}
