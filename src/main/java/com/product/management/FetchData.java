package com.product.management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FetchData {

	//PostgreSQL database url and credentials
	static final String dbUrl = "jdbc:postgresql://localhost:5432/sanmauto";
	static final String dbUser = "postgres";
	static final String dbPass = "postql";
	
	//fetch data from database
	public List<Product> getProducts(){
		
		//Creating list of Product instance
		List<Product> products = new ArrayList<>();
		
		//Database variables
		Connection selConn = null;
		PreparedStatement selStmt = null;
		
		//select query to fetch all the data from the database
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
	
	public static void main(String[] args) {
		
	}

}
