package com.company;

import java.io.File;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.service.CustomerService;
import com.company.service.ProductService;
import com.company.service.ProductSupplierService;
import com.company.service.SupplierService;

@Configuration	
public class JsonDataSearchConfiguration {

	private Function<String, File> getResourceFile = filename -> {
		ClassLoader cl = getClass().getClassLoader();
		String path = null;
		try {
			path = cl.getResource(filename).getFile();
		} catch (NullPointerException npe) {
			// file not found, other areas of code advised to expect null value!
		}
		if (path != null) {
			File f = new File(cl.getResource(filename).getFile());
			return f;
		}
		return new File(filename);
	};
	
	// Data Files
	
	@Bean(name="customerFile")
	public File customerFile() {
		return getResourceFile.apply("customers.json");
	}
	
	@Bean(name="productFile")
	public File productFile() {
		return getResourceFile.apply("products.json");
	}
	
	@Bean(name="supplierFile")
	public File supplierFile() {
		return getResourceFile.apply("suppliers.json");
	}
	
	@Bean(name="productSupplierFile")
	public File productSupplierFile() {
		return getResourceFile.apply("product_suppliers.json");
	}
	
	// Services
	
	@Bean
	public CustomerService customerService() {
		return new CustomerService(customerFile());
	}
	
	@Bean
	public ProductService productService() {
		return new ProductService(productFile());
	}
	
	@Bean
	public SupplierService supplierService() {
		return new SupplierService(supplierFile(), productSupplierService());
	}
	
	@Bean
	public ProductSupplierService productSupplierService() {
		return new ProductSupplierService(productSupplierFile());
	}
	
}
