package com.company.service.iface;

import java.util.List;

import com.company.model.Product;

public interface IProductService extends INamedListService<Product> {

	public List<Product> filterByPriceBetween(List<Product> products, double minSize, double maxSize);
	public List<Product> filterByType(List<Product> products, String type);
	
	public List<Product> sortByPriceAscending(List<Product> products);
	public List<Product> sortByPriceDescending(List<Product> products);
}
