package com.company.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.company.JsonSearchDataTestConfiguration;
import com.company.model.Product;
import com.company.model.Supplier;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ContextConfiguration(classes=JsonSearchDataTestConfiguration.class)
public class SupplierServiceTests {
	
	@Mock
	private SupplierService supplierService = Mockito.spy(new SupplierService());
	
	private List<Product> mockProducts;
	private List<Supplier> mockSuppliers;
	
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
		
		setupMockitoResponses();
	}
	
	@SuppressWarnings("unchecked")
	private void setupMockitoResponses() {
		// fall through to real implementation
		when(supplierService.filterByProductName(anyList(), anyString()))
			.thenCallRealMethod();
		when(supplierService.filterByParameter(anyList(), anyString()))
			.thenCallRealMethod();
		when(supplierService.filterBy(anyList(), anyString(), anyString()))
			.thenCallRealMethod();
		
		doReturn(mockSuppliers).when(supplierService).list();
	}

	@Test
	public void canFilterByProductName() {
		List<Supplier> suppliers = supplierService.filterByProductName(mockSuppliers, "Product1");
		
		assertNotNull(suppliers);
		assertEquals(1, suppliers.size());
		assertEquals("Supplier1", suppliers.get(0).getName());
	}
	
	@Test
	public void canFilterByProductParameter() {
		List<Supplier> suppliers = supplierService.filterByParameter(mockSuppliers, "product:Product2");
		
		assertNotNull(suppliers);
		assertEquals(1, suppliers.size());
		assertEquals("Supplier2", suppliers.get(0).getName());
	}
	
	@Test
	public void canIgnoreFilterWhenInvalidParameter() {
		List<Supplier> suppliers = supplierService.filterByParameter(mockSuppliers, "randomstring");
		
		assertNotNull(suppliers);
		assertEquals(2, suppliers.size());
	}
}
