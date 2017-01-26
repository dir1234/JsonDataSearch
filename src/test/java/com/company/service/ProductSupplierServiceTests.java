package com.company.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.company.JsonSearchDataTestConfiguration;
import com.company.model.Product;
import com.company.model.ProductSupplier;
import com.company.model.Supplier;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ContextConfiguration(classes=JsonSearchDataTestConfiguration.class)
public class ProductSupplierServiceTests {
	
	@Mock
	private ProductService productService; 
	
	@InjectMocks
	private ProductSupplierService productSupplierService = Mockito.spy(new ProductSupplierService());
	
	private List<Product> mockProducts;
	private List<Supplier> mockSuppliers;
	private List<ProductSupplier> mockProductSuppliers;
	
	@Before
	public void setUp() { 
		mockProducts = new ArrayList<Product>();
		mockProducts.add(new Product("Product1", "Type 1", 1d));
		mockProducts.add(new Product("Product2", "Type 2", 1.00d));
		mockProducts.add(new Product("ProductA", "Type 3", 105.75d));
		mockProducts.add(new Product("Product3", "Type 1", 10.5d));
		
		Supplier supplier1 = new Supplier("Supplier1");
		supplier1.addProduct(mockProducts.get(0));
		supplier1.addProduct(mockProducts.get(2));
		
		Supplier supplier2 = new Supplier("Supplier2");
		supplier2.addProduct(mockProducts.get(1));
		supplier2.addProduct(mockProducts.get(2));
		
		mockSuppliers = new ArrayList<Supplier>();
		mockSuppliers.add(supplier1);
		mockSuppliers.add(supplier2);
		
		mockProductSuppliers = new ArrayList<ProductSupplier>();
		for (Supplier supplier : mockSuppliers) {
			mockProductSuppliers.addAll(supplier.generateProductSuppliers());
		}
		setupMockitoResponses();
	}
	
	@SuppressWarnings("unchecked")
	private void setupMockitoResponses() {
		// fall through to real implementation
		doCallRealMethod().when(productSupplierService).getAllProductsFor(anyString());
		doCallRealMethod().when(productService).filterByName(anyList(), anyString());
	
		// return mocks when appropriate
		doReturn(mockProductSuppliers).when(productSupplierService).list();
		doReturn(mockProducts).when(productService).list();
	}

	@Test
	public void canGetProductsForSupplier() {
		List<Product> products = productSupplierService.getAllProductsFor("Supplier1");
		
		assertNotNull(products);
		assertEquals(2, products.size());
		assertEquals("Product1", products.get(0).getName());
		assertEquals("ProductA", products.get(1).getName());
	}
	
}
