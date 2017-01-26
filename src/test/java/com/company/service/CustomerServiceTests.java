package com.company.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
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
import com.company.model.Customer;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ContextConfiguration(classes=JsonSearchDataTestConfiguration.class)
public class CustomerServiceTests {
	
	@Mock
	private CustomerService customerService = Mockito.spy(new CustomerService());
	
	private List<Customer> mockCustomers;
	
	@Before
	public void setUp() {
		mockCustomers = new ArrayList<Customer>();
		mockCustomers.add(new Customer("Jason Peters", "67710", 1));
		mockCustomers.add(new Customer("Howard Grant", "3200", 0));
		
		setupMockitoResponses();
	}
	
	@SuppressWarnings("unchecked")
	private void setupMockitoResponses() {
		when(customerService.filterByName(anyList(), anyString()))
			.thenCallRealMethod();
		when(customerService.filterByName(isNull(List.class), anyString()))
			.thenCallRealMethod();
		when(customerService.filterByNameParameter(anyList(), anyString()))
			.thenCallRealMethod();
		when(customerService.filterByPostcode(anyList(), anyString()))
			.thenCallRealMethod();
		when(customerService.filterByParameter(anyList(), anyString()))
			.thenCallRealMethod();
		when(customerService.filterBy(anyList(), anyString(), anyString()))
			.thenCallRealMethod();
		
		when(customerService.sortByName(anyList()))
			.thenCallRealMethod();
		when(customerService.sortByOrderCountAscending(anyList()))
			.thenCallRealMethod();
		when(customerService.sortByOrderCountDescending(anyList()))
			.thenCallRealMethod();
		when(customerService.sortBy(anyList(), anyString(), anyString()))
			.thenCallRealMethod();
		
		doReturn(mockCustomers).when(customerService).list();
	}
	
	@Test
	public void canFilterByName() { // testing specific implementation from AbstractNamedService, no need to repeat in other test cases
		
		List<Customer> filteredCustomers = customerService.filterByName(mockCustomers, "Jason Peters");
		List<Customer> notExistingCustomers = customerService.filterByName(mockCustomers, "Bob Hope");
		 
		assertNotNull(filteredCustomers);
		assertEquals(1, filteredCustomers.size());
		assertEquals("Jason Peters", filteredCustomers.get(0).getName());
		
		assertNotNull(notExistingCustomers);
		assertEquals(0, notExistingCustomers.size());
		
		// no requirement to verify, filterByName() doesn't call any other methods
	}
	
	@Test
	public void canFilterByNameUsingFallthrough() { 
		List<Customer> usingFallthrough = customerService.filterByName(null, "Jason Peters");
		
		assertNotNull(usingFallthrough);
		assertEquals(1, usingFallthrough.size());
		
		verify(customerService).list();
	}
	
	@Test
	public void canFilterByNameParameter() {
		List<Customer> customers = customerService.filterByNameParameter(mockCustomers, "name:Jason Peters");
		
		assertNotNull(customers);
		assertEquals(1, customers.size());
		assertEquals("Jason Peters", customers.get(0).getName());
	}
	
	@Test
	public void canFilterByPostcode() {
		List<Customer> filteredCustomers = customerService.filterByPostcode(mockCustomers, "3200");
		List<Customer> notExistingCustomers = customerService.filterByPostcode(mockCustomers, "1234");
		
		assertNotNull(filteredCustomers);
		assertEquals(1, filteredCustomers.size());
		assertEquals("Howard Grant", filteredCustomers.get(0).getName());
		
		assertNotNull(notExistingCustomers);
		assertEquals(0, notExistingCustomers.size());
	}
	
	@Test
	public void canFilterByPostcodeParameter() {
		List<Customer> customers = customerService.filterByParameter(mockCustomers, "postcode:3200");
		
		assertNotNull(customers);
		assertEquals(1, customers.size());
		assertEquals("Howard Grant", customers.get(0).getName());
	}
	
	@Test
	public void canIgnoreFilterByInvalidType() {
		List<Customer> customers = customerService.filterByParameter(mockCustomers, "randomstring:1000");
		
		assertNotNull(customers);
		assertEquals(2, customers.size());
	}
	
	@Test
	public void canFilterByNullPostcode() {
		List<Customer> customers = customerService.filterByParameter(mockCustomers, "postcode:");
		
		assertNotNull(customers);
		assertEquals(2, customers.size());
	}
	
	@Test
	public void canSortByNameAlphabetically() {
		List<Customer> sortedCustomers = customerService.sortByName(mockCustomers);
		
		assertNotNull(sortedCustomers);
		assertEquals(2, sortedCustomers.size());
		assertEquals("Howard Grant", sortedCustomers.get(0).getName());
		assertEquals("Jason Peters", sortedCustomers.get(1).getName());
	}

	@Test
	public void canSortByOrderCountAscending() {
		List<Customer> sortedCustomers = customerService.sortByOrderCountAscending(mockCustomers);
		
		assertNotNull(sortedCustomers);
		assertEquals(2, sortedCustomers.size());
		assertEquals("Howard Grant", sortedCustomers.get(0).getName());
		assertEquals("Jason Peters", sortedCustomers.get(1).getName());
	}
	
	@Test
	public void canSortByOrderCountDescending() {
		List<Customer> sortedCustomers = customerService.sortByOrderCountDescending(mockCustomers);
		
		assertNotNull(sortedCustomers);
		assertEquals(2, sortedCustomers.size());
		assertEquals("Jason Peters", sortedCustomers.get(0).getName());
		assertEquals("Howard Grant", sortedCustomers.get(1).getName());
	}
	
	@Test
	public void canSortByParameterOrderCountAscending() {
		List<Customer> customers = customerService.sortBy(mockCustomers, "orders", "ascending");
		
		assertNotNull(customers);
		assertEquals(2, customers.size());
		assertEquals("Howard Grant", customers.get(0).getName());
		assertEquals("Jason Peters", customers.get(1).getName());
	}
	
	@Test
	public void canSortByParameterOrderCountDescending() {
		List<Customer> customers = customerService.sortBy(mockCustomers, "orders", "descending");
		
		assertNotNull(customers);
		assertEquals(2, customers.size());
		assertEquals("Jason Peters", customers.get(0).getName());
		assertEquals("Howard Grant", customers.get(1).getName());
	}
	
	@Test
	public void canIgnoreSortingByInvalidParameterType() {
		List<Customer> customers = customerService.sortBy(mockCustomers, "randomstring", "descending");
		
		assertNotNull(customers);
		assertEquals(2, customers.size());
		assertEquals("Jason Peters", customers.get(0).getName());
		assertEquals("Howard Grant", customers.get(1).getName());
	}
	
	@Test
	public void canIgnoreSortingByInvalidParameterValue() {
		List<Customer> customers = customerService.sortBy(mockCustomers, "orders", "randomstring");
		
		assertNotNull(customers);
		assertEquals(2, customers.size());
		assertEquals("Jason Peters", customers.get(0).getName());
		assertEquals("Howard Grant", customers.get(1).getName());
	}
	
}
