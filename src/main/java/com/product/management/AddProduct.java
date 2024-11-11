package com.product.management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddProduct {
	
	//PostgreSQL database url and credentials
	static final String dbUrl = "jdbc:postgresql://localhost:5432/sanmauto";
	static final String dbUser = "postgres";
	static final String dbPass = "postql";
	
	public boolean addProduct(String name, String category, int price, int quantity) {
		
		Connection inCon = null;
		PreparedStatement inStmt = null;
		
		try {
			
			//db Connection establishment
			inCon = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			
			//insert Query
			String inQuery = "INSERT INTO ProductData(productname, productcategory, productprice, productquantity)"
					+ "VALUES (?, ?, ?, ?)";
			
			//Adding values via Prepared Statement to avoid SQL injection
			inStmt = inCon.prepareStatement(inQuery);
			inStmt.setString(1, name);
			inStmt.setString(2, category);
			inStmt.setInt(3, price);
			inStmt.setInt(4, quantity);
			
			//Check for the insertion
			int rows = inStmt.executeUpdate();
			
			//return the response to the ProductAddServer
			if(rows > 0) {
				return true;
			}else {
				return false;
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
			return false;
			
		}finally {
				
				//Closing the resources
				try {
					
					if(inStmt != null){
	
						inStmt.close();
					}
					if(inCon != null) {
						
						inCon.close();
					}
					
				} catch (SQLException e) {
					
					e.printStackTrace();
					
				}
			
		}
	
	}

}
