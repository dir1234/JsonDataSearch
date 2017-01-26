package com.company.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyDouble;
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


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ContextConfiguration(classes=JsonSearchDataTestConfiguration.class)
public class ProductServiceTests {
	
	@Mock
	private ProductService productService = Mockito.spy(new ProductService());
	
	private List<Product> mockProducts;
	
	@Before
	public void setUp() {
		mockProducts = new ArrayList<Product>();
		mockProducts.add(new Product("Product1", "Type 1", 1d));
		mockProducts.add(new Product("Product2", "Type 2", 1.00d));
		mockProducts.add(new Product("ProductA", "Type 3", 105.75d));
		mockProducts.add(new Product("Product3", "Type 1", 10.5d));
		
		setupMockitoResponses();
	}
	
	@SuppressWarnings("unchecked")
	private void setupMockitoResponses() {
		when(productService.filterByPriceBetween(anyList(), anyDouble(), anyDouble()))
			.thenCallRealMethod();
		when(productService.filterByPriceEquals(anyList(), anyDouble()))
			.thenCallRealMethod();
		when(productService.filterByType(anyList(), anyString()))
			.thenCallRealMethod();
		when(productService.filterByParameter(anyList(), anyString()))
			.thenCallRealMethod();
		when(productService.filterBy(anyList(), anyString(), anyString()))
			.thenCallRealMethod();
		when(productService.filterByPriceParameter(anyList(), anyString()))
			.thenCallRealMethod();
		
		when(productService.sortByPriceAscending(anyList()))
			.thenCallRealMethod();
		when(productService.sortByPriceDescending(anyList()))
			.thenCallRealMethod();
		when(productService.sortByParameter(anyList(), anyString()))
			.thenCallRealMethod();
		when(productService.sortBy(anyList(), anyString(), anyString()))
			.thenCallRealMethod();
		
		doReturn(mockProducts).when(productService).list();
	}

	@Test
	public void canFilterByPriceBetween() { 
		List<Product> lowPricedProducts = productService.filterByPriceBetween(null, 0d, 1d);
		List<Product> noFreeProducts = productService.filterByPriceBetween(mockProducts, 0d, 0d);
		
		assertNotNull(lowPricedProducts);
		assertEquals(2, lowPricedProducts.size());
		assertEquals("Product1", lowPricedProducts.get(0).getName());
		assertEquals("Product2", lowPricedProducts.get(1).getName());
		
		assertNotNull(noFreeProducts);
		assertTrue(noFreeProducts.isEmpty());
	}
	
	@Test
	public void correctlyHandlesFilterByPriceReversedInput() {
		List<Product> products = productService.filterByPriceBetween(mockProducts, 1.5d, 0d);
		
		assertNotNull(products);
		assertEquals(2,  products.size());
		assertEquals("Product1", products.get(0).getName());
		assertEquals("Product2", products.get(1).getName());
	}
	
	@Test
	public void canFilterByPriceBetweenSameValues() {
		List<Product> products = productService.filterByPriceBetween(mockProducts, 10.5d, 10.5d);
		
		assertNotNull(products);
		assertEquals(1, products.size());
		assertEquals("Product3", products.get(0).getName());
	}
	
	@Test
	public void canFilterByType() {
		List<Product> products = productService.filterByType(mockProducts, "Type 1");
		
		assertNotNull(products);
		assertEquals(2, products.size());
		assertEquals("Product1", products.get(0).getName());
		assertEquals("Product3", products.get(1).getName());
	}
	
	@Test
	public void canOrderByPriceAscending() {
		List<Product> products = productService.sortByPriceAscending(mockProducts);
		
		assertNotNull(products);
		assertEquals(4, products.size());
		assertEquals("Product1", products.get(0).getName());
		assertEquals("Product2", products.get(1).getName());
		assertEquals("Product3", products.get(2).getName());
		assertEquals("ProductA", products.get(3).getName());
	}
	
	@Test
	public void canOrderByPriceDescending() { 
		List<Product> products = productService.sortByPriceDescending(mockProducts);
		
		assertNotNull(products);
		assertEquals(4, products.size());
		assertEquals("ProductA", products.get(0).getName());
		assertEquals("Product3", products.get(1).getName());
		assertEquals("Product2", products.get(2).getName());
		assertEquals("Product1", products.get(3).getName());
	}
			
	@Test
	public void canFilterByPriceParameter() {
		List<Product> products = productService.filterByParameter(mockProducts, "price:0.5-9");
		
		assertNotNull(products);
		assertEquals(2, products.size());
	}
	
	@Test
	public void canFilterByPriceEqualsParameter() {
		List<Product> products = productService.filterByParameter(mockProducts, "price:10.5");
		
		assertNotNull(products);
		assertEquals(1, products.size());
	}
	
	@Test
	public void canIgnoreFilterByNegativePriceParameter() {
		List<Product> products = productService.filterByParameter(mockProducts, "price:-100");
		
		assertNotNull(products);
		assertEquals(0, products.size());
	}
	
	@Test
	public void canFilterByTypeParameter() {
		List<Product> products = productService.filterByParameter(mockProducts, "type:Type 2");
		
		assertNotNull(products);
		assertEquals(1, products.size());
	}
	
	@Test
	public void sortByParameterPriceAscending() {
		List<Product> products = productService.sortByParameter(mockProducts, "price:ascending");
		
		assertNotNull(products);
		assertEquals(4, products.size());
		assertEquals("Product1", products.get(0).getName());
		assertEquals("Product2", products.get(1).getName());
		assertEquals("Product3", products.get(2).getName());
		assertEquals("ProductA", products.get(3).getName());
	}
	
	@Test
	public void sortByParameterPriceDescending() {
		List<Product> products = productService.sortByParameter(mockProducts, "price:descending");
		
		assertNotNull(products);
		assertEquals(4, products.size());
		assertEquals("ProductA", products.get(0).getName());
		assertEquals("Product3", products.get(1).getName());
		assertEquals("Product2", products.get(2).getName());
		assertEquals("Product1", products.get(3).getName());
	}
	
	@Test
	public void canIgnoreSortingByInvalidParameter() {
		List<Product> products = productService.sortByParameter(mockProducts, "randomstring");
		
		assertNotNull(products);
		assertEquals("Product1", products.get(0).getName());
		assertEquals("Product2", products.get(1).getName());
		assertEquals("ProductA", products.get(2).getName());
		assertEquals("Product3", products.get(3).getName());
	}
}
