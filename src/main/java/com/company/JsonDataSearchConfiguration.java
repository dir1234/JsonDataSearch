package com.company;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.company.service.CustomerService;
import com.company.service.ProductService;
import com.company.service.ProductSupplierService;
import com.company.service.SupplierService;

@Configuration	
public class JsonDataSearchConfiguration {

	 protected InputStream loadResource(String fileName) throws IOException {
		 ClassPathResource classPathResource = new ClassPathResource(fileName);
         return classPathResource.getInputStream();
     }
	 
	// Data Files
	
	@Bean(name="customerFile")
	public InputStream customerFile() throws IOException {
		return loadResource("customers.json");
		//return getResourceFile.apply("customers.json");
	}
	
	@Bean(name="productFile")
	public InputStream productFile() throws IOException {
		return loadResource("products.json");
		//return getResourceFile.apply("products.json");
	}
	
	@Bean(name="supplierFile")
	public InputStream supplierFile() throws IOException {
		return loadResource("suppliers.json");
	}
	
	@Bean(name="productSupplierFile")
	public InputStream productSupplierFile() throws IOException {
		return loadResource("product_suppliers.json");
	}
	
	// Services
	
	@Bean
	public CustomerService customerService() throws IOException {
		return new CustomerService(customerFile());
	}
	
	@Bean
	public ProductService productService() throws IOException {
		return new ProductService(productFile());
	}
	
	@Bean
	public SupplierService supplierService() throws IOException {
		return new SupplierService(supplierFile(), productSupplierService());
	}
	
	@Bean
	public ProductSupplierService productSupplierService() throws IOException {
		return new ProductSupplierService(productSupplierFile());
	}
	
}
