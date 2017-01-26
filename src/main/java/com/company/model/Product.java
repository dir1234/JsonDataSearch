package com.company.model;

public class Product extends NamedObject {

	private String type;
	private double price;
	
	public Product() {
		super();
	}
	
	public Product(String name, String type, double price) {
		super(name);
		this.type = type;
		this.price = price;
	}
	
	public String getType() {
		return this.type;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public int getPriceInPence() {
		return new Double(this.price * 100).intValue() ;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Product) {
			Product otherProduct = (Product) obj;
			if (!otherProduct.getName().equals(this.getName()))
				return false;
			if (otherProduct.getPrice() != this.getPrice())
				return false;
			if (!otherProduct.getType().equals(this.getType()))
				return false;
			return true;
		}
		return super.equals(obj);
	}
	
}
