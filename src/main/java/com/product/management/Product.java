package com.product.management;

public class Product {
	
	private int id;
    private String name;
    private String category;
    private int price;
    private int stock;

    public Product(int id, String name, String category, int price, int stock) {
    	this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }
    

}
