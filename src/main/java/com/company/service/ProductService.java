package com.company.service;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.company.model.Product;
import com.company.service.iface.IProductService;

@Service
public class ProductService extends AbstractNamedService<Product> implements IProductService {

	private static final Logger logger = Logger.getLogger(ProductService.class);
	
	@Autowired
	@Qualifier("productFile")
	private File productFile;
	
	public ProductService() {
		super(logger);
		this.setFileStore(productFile);
	}
	
	public ProductService(File fileStore) {
		super(logger);
		this.setFileStore(fileStore);
	}
	
	public List<Product> filterByPriceParameter(List<Product> products, String parameter) {
		if (parameter.contains("-") && !parameter.startsWith("-")) { // range, being mindful of negative number input
			String[] parts = parameter.split("-");
			if (parts.length > 1)
				return this.filterByPriceBetween(products, parts[0].trim(), parts[1].trim());
		}
		return this.filterByPriceEquals(products, parameter);
	}
	
	private List<Product> filterByPriceEquals(List<Product> products, String number) {
		try {
			Double value = Double.valueOf(number);
			return this.filterByPriceEquals(products, value);
		} catch (NumberFormatException nfe) {
			if (products == null)
				return this.list();
			return products;
		}
	}
	
	public List<Product> filterByPriceEquals(List<Product> products, double price) {
		if (products == null)
			products = this.list();
		return products.stream()
				.filter(product -> product.getPrice() == price)
				.collect(Collectors.toList());
	}
	
	private List<Product> filterByPriceBetween(List<Product> products, String minPrice, String maxPrice) {
		try {
			Double minValue = Double.valueOf(minPrice);
			Double maxValue = Double.valueOf(maxPrice);
			return this.filterByPriceBetween(products, minValue, maxValue);
		} catch (NumberFormatException nfe) {
			if (products == null)
				return this.list();
			return products;
		}
	}
	
	public List<Product> filterByPriceBetween(List<Product> products, double minSize, double maxSize) {
		double realMinSize = (minSize <= maxSize) ? minSize : maxSize;
		double realMaxSize = (minSize <= maxSize) ? maxSize : minSize;
		
		if (products == null)
			products = this.list();
		
		return products.stream()
			.filter(x -> x.getPrice() >= realMinSize)
			.filter(x -> x.getPrice() <= realMaxSize)
			.collect(Collectors.toList());
	}
	
	public List<Product> filterByType(List<Product> products, String type) {
		if (products == null)
			products = this.list();
		return products.stream()
			.filter(x -> type.equals(x.getType()))
			.collect(Collectors.toList());
	}
	
	@Override
	public List<Product> sortByPriceAscending(List<Product> products) {
		if (products == null)
			products = this.list();
		products.sort((c1, c2) -> c1.getPriceInPence() - c2.getPriceInPence());
		return products;
	}

	@Override
	public List<Product> sortByPriceDescending(List<Product> products) {
		if (products == null)
			products = this.list();
		Comparator<Product> productComparator = new Comparator<Product>() {
			@Override
			public int compare(Product p1, Product p2) {
				if (p1.getPrice() >= p2.getPrice())
					return -1;
				return 1;
			}
		};
		
		products.sort(productComparator);
		return products;
	}
	
	@Override
	public List<Product> filterBy(List<Product> list, String filterType, String filterValue) { 
		switch (filterType) {
		case "price" : return filterByPriceParameter(list, filterValue);
		case "type"  : return filterByType(list, filterValue);
		}
		return list;
	}
	
	@Override
	public List<Product> sortBy(List<Product> list, String sortType, String sortValue) {
		if (sortType.equals("price") && sortValue.equals("ascending"))
			return sortByPriceAscending(list);
		if (sortType.equals("price") && sortValue.equals("descending"))
			return sortByPriceDescending(list);
		return list;
	}

	@Override
	protected Class<Product> getModelClass() {
		return Product.class;
	}
}
