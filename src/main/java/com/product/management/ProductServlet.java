package com.product.management;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProductServlet extends HttpServlet{
	
    //jdbc Connection
	static final String dbUrl = "jdbc:postgresql://localhost:5432/sanmauto";
	static final String dbUser = "postgres";
	static final String dbPass = "postql";    
	
	//to write product data into db
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
    	PrintWriter postOut = response.getWriter();
        
    	String productName = request.getParameter("productName");
        String productCategory = request.getParameter("productCategory");
        String price = request.getParameter("productPrice");
        String quantity = request.getParameter("productQuantity");
        
        int productPrice = Integer.parseInt(price);
        int productQuantity = Integer.parseInt(quantity);
        
        
        //jdbc initializers
		Connection inConn = null;
		PreparedStatement inStmt = null;
		
		String inQuery = "INSERT INTO ProductData (productname, productcategory, productprice, productquantity)"
				+ " VALUES(?, ?, ?, ?)";
		
		try {
			
			inConn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			inStmt = inConn.prepareStatement(inQuery);
			inStmt.setString(1, productName);
			inStmt.setString(2, productCategory);
			inStmt.setInt(3, productPrice);
			inStmt.setInt(4, productQuantity);
			
			int rowsAffected = inStmt.executeUpdate();
			if(rowsAffected > 0) {
				postOut.println(200);
			}else {
				postOut.println(404);
			}

			inConn.close();
			inStmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    	
		postOut.close();
    }
	
    
    //to fetch the product from db
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
		
		
		PrintWriter getOut = response.getWriter();
		
		// Convert products list to JSONArray to send as JSON response // It is easier to parseIt this way
		JSONArray productJsonArray = new JSONArray();

		Connection selConn = null;
		PreparedStatement selStmt = null;
		
		String selQuery = "SELECT * FROM ProductData";
		
		try {
			
			selConn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			selStmt = selConn.prepareStatement(selQuery);
			
			//Catch the result after statement execution
			ResultSet result = selStmt.executeQuery();
			
			while(result.next()) {

				JSONObject productJson = new JSONObject();
				
				productJson.put("productId", result.getInt("id"));
				productJson.put("productName", result.getString("productname"));
				productJson.put("productCategory", result.getString("productcategory"));
				productJson.put("productPrice", result.getInt("productprice"));
				productJson.put("productQuantity", result.getInt("productquantity"));
				
				productJsonArray.add(productJson);
			
			}
			
			selConn.close();
			selStmt.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//pushing response as in JSON
		getOut.println(productJsonArray);
        getOut.close();
        
        System.out.println(productJsonArray);
        
    }
	
	
	//try Codes:
	
	 // Mock product data generation
//   private List<Product> getProducts() {
//       List<Product> products = new ArrayList<>();
//       products.add(new Product("Product A", "Category 1", 100.0, 50));
//       products.add(new Product("Product B", "Category 2", 150.0, 20));
//       products.add(new Product("Product C", "Category 3", 200.0, 30));
//       return products;
//   }
	

	
	  // Generate mock product data
//  List<Product> products = getProducts();

//  // Convert products list to JSON array
//  JSONObject productJson = new JSONObject();
//  JSONArray productJsonArray = new JSONArray();
  
//  for (Product product : products) {
//      productJson.put("name", product.getName());
//      productJson.put("category", product.getCategory());
//      productJson.put("price", product.getPrice());
//      productJson.put("stock", product.getStock());
//      productJsonArray.add(productJson);
//  }
//  
  

  // Send JSON response
//  getOut.println(productJsonArray);
//  getOut.close();
//  
//  System.out.println(productJsonArray);
  
//  response.setContentType("application/json");
//  response.setCharacterEncoding("UTF-8");
//  PrintWriter out = response.getWriter();
//  out.write(jsonArray.toJSONString());
//  out.close();
	

}
