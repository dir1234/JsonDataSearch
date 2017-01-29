package com.company.service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.company.model.Customer;
import com.company.service.iface.ICustomerService;

@Component
public class CustomerService extends AbstractNamedService<Customer> implements ICustomerService {
	
	private static final Logger logger = Logger.getLogger(CustomerService.class);
	
	@Autowired
	@Qualifier("customerFile")
	private InputStream customerFile;
	
	public CustomerService() {
		super(logger);
		this.setFileStore(customerFile);
	}
	
	public CustomerService(InputStream fileStore) {
		super(logger);
		this.setFileStore(fileStore);
	}

	@Override
	public List<Customer> filterByPostcode(List<Customer> customers, String postcode) {
		return customers.stream()
			.filter(x -> postcode.equals(x.getPostcode()))
			.collect(Collectors.toList());
	}

	@Override
	public List<Customer> sortByOrderCountAscending(List<Customer> customers) {
		if (customers == null)
			customers = this.list();
		customers.sort((c1, c2) -> c1.getOrderCount() - c2.getOrderCount());
		return customers;
	}

	@Override
	public List<Customer> sortByOrderCountDescending(List<Customer> customers) {
		if (customers == null)
			customers = this.list();
		customers.sort((c1, c2) -> c2.getOrderCount() + c1.getOrderCount());
		return customers;
	}
	
	@Override
	public List<Customer> filterBy(List<Customer> list, String filterType, String filterValue) {
		if ("postcode".equals(filterType))
			return filterByPostcode(list, filterValue);
		return list;
	}
	
	@Override
	public List<Customer> sortBy(List<Customer> list, String sortType, String sortValue) {
		if (sortType.equals("orders") && sortValue.equals("ascending"))
			return sortByOrderCountAscending(list);
		if (sortType.equals("orders") && sortValue.equals("descending"))
			return sortByOrderCountDescending(list);
		return list;
	}
	
	@Override
	protected Class<Customer> getModelClass() {
		return Customer.class;
	}

}
