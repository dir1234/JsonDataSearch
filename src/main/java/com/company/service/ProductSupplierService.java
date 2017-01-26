package com.company.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.company.model.Product;
import com.company.model.ProductSupplier;
import com.company.model.Supplier;


/*
 * A helper service to map Products to Suppliers
 */
@Component
public class ProductSupplierService extends AbstractService<ProductSupplier> {
	
	private static final Logger logger = Logger.getLogger(ProductSupplierService.class);
	private static final SecureRandom rand = new SecureRandom();
	
	@Autowired
	@Qualifier("productSupplierFile")
	private File productSupplierFile;
	
	@Autowired
	private ProductService productService;
	private SupplierService supplierService;
	
	public ProductSupplierService() {
		super(logger);
		this.setFileStore(productSupplierFile);
	}
	
	public ProductSupplierService(File fileStore) {
		super(logger);
		this.setFileStore(fileStore);
	}
	
	public void setSupplierService(SupplierService service) {
		this.supplierService = service;
	}
	
	public List<Product> getAllProductsFor(String supplierName) {
		List<Product> products = new ArrayList<Product>();
		
		for(String name : this.getProductNamesFromSupplierName(supplierName)) {
			products.addAll(productService.filterByName(null, name));
		}
		
		return products.stream().distinct().collect(Collectors.toList());
	}
	
	private List<String> getProductNamesFromSupplierName(String supplierName) {
		return this.list().stream()
				.filter(x -> supplierName.equals(x.getSupplierName()))
				.map(x -> x.getProductName())
				.collect(Collectors.toList());
	}

	public void generateJsonDataFile(String fileName) { // no unit test as this was used to generate /src/main/resources/product_suppliers.json
		File f = new File(fileName);
		if (f.exists())
			f.delete();
		
		List<ProductSupplier> productSuppliers = generateProductSuppliersAtRandom();
		JSONArray json = new JSONArray(productSuppliers);
		try { 
			Files.write(Paths.get(f.getAbsolutePath()), json.toString().getBytes("UTF8"), StandardOpenOption.CREATE);
		} catch (IOException ioe) {
			logger.error("An exception occured writing to file: " + f.getAbsolutePath(), ioe);
		}
	}
	
	private List<ProductSupplier> generateProductSuppliersAtRandom() {
		List<ProductSupplier> retval = new ArrayList<ProductSupplier>();
		List<Product> products = productService.list();
		
		for(Supplier supplier : supplierService.list()) {
			int countProducts = rand.nextInt(15);
			for (int i=0; i<countProducts; i++) {
				int productIndex = rand.nextInt(products.size()); // smelly, should check for potential modulo bias, but this is just a 
																  // helper to associate data
				supplier.addProduct(products.get(productIndex));
			}
			retval.addAll(supplier.generateProductSuppliers());
		}
		
		return retval;
	}
	
	@Override
	protected Class<ProductSupplier> getModelClass() {
		return ProductSupplier.class;
	}
}
