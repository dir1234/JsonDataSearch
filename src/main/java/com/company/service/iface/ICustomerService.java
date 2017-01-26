package com.company.service.iface;

import java.util.List;

import com.company.model.Customer;

public interface ICustomerService extends INamedListService<Customer> {

	public List<Customer> filterByPostcode(List<Customer> customers, String postcode);
	
	public List<Customer> sortByOrderCountAscending(List<Customer> customers);
	public List<Customer> sortByOrderCountDescending(List<Customer> customers);
}
