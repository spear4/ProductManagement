package com.product.management;

public class Product {
	
	private int id;
    private String name;
    private String category;
    private int price;
    private int quantity;

    //Parameterised-constructor
    public Product(int id, String name, String category, int price, int quantity) {
    	this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }


	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public int getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}


	//Setters
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
    @Override
    public String toString() {
    	return "{id=" + id + ", name=" + name + ", category=" + category +
                ", price=" + price + ", quantity=" + quantity + "}";
    }


}
