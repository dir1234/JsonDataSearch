package com.company.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Supplier extends NamedObject {
	
	private List<Product> products;
	
	public Supplier() {
		super();
	}
	
	public Supplier(String name) {
		super(name);
	}
	
	public List<Product> getProducts() {
		if (this.products == null)
			this.products = new ArrayList<Product>();
		return this.products;
	}

	public void addProduct(Product product) {
		if (!this.getProducts().contains(product))
			this.getProducts().add(product);
		// silent failure, caller asked to add it and it's already there, caller would consider this success and we save some memory!
	}

	public void addAllProducts(List<Product> distinctProducts) {
		this.getProducts().addAll(distinctProducts);
	}

	public List<ProductSupplier> generateProductSuppliers() {
		List<ProductSupplier> retval = new ArrayList<ProductSupplier>();
		
		for (Product p : this.getProducts())
			retval.add(new ProductSupplier(this.getName(), p.getName()));
			
		return retval;
	}
}
