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
