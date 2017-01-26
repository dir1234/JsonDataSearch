package com.company.model;

public class Customer extends NamedObject {

	private String postcode;
	private Integer orderCount;
	
	public Customer() {
		super();
	}
	
	public Customer(String name, String postcode, int orderCount) {
		super(name);
		this.postcode = postcode;
		this.orderCount = new Integer(orderCount);
	}
	
	public String getPostcode() {
		return this.postcode;
	}
	
	public Integer getOrderCount() {
		return this.orderCount;
	}
}
