package com.company.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.company.model.Product;
import com.company.model.Supplier;
import com.company.service.iface.ISupplierService;

@Component
public class SupplierService extends AbstractNamedService<Supplier> implements ISupplierService {

	private static final Logger logger = Logger.getLogger(SupplierService.class);
	
	@Autowired
	@Qualifier("supplierFile")
	private File supplierFile;
	
	@Autowired 
	private ProductSupplierService productSupplierService;
	
	public SupplierService() {
		super(logger);
		this.setFileStore(supplierFile);
		if (this.getFileStore() != null)
			initialiseSuppliedProducts();
	}
	
	public SupplierService(File fileStore, ProductSupplierService productSupplierService) {
		super(logger);
		this.productSupplierService = productSupplierService;
		productSupplierService.setSupplierService(this);
		this.setFileStore(fileStore);
		if (this.getFileStore() != null)
			initialiseSuppliedProducts();
	}

	private void initialiseSuppliedProducts() {
		List<Supplier> initialisedSuppliers = new ArrayList<Supplier>();
		for (Supplier supplier : this.list()) {
			List<Product> distinctProducts = productSupplierService.getAllProductsFor(supplier.getName());
			supplier.addAllProducts(distinctProducts);
			initialisedSuppliers.add(supplier);
		}
		this.objects = initialisedSuppliers;
	}
	
	@Override
	public List<Supplier> filterByProductName(List<Supplier> suppliers, String productName) {
		return suppliers.stream()
			.filter(suppliesProduct(productName))
			.collect(Collectors.toList());
	}
	
	private Predicate<Supplier> suppliesProduct(String productName) {
		return supplier -> supplier.getProducts().stream()
				.anyMatch(product -> Objects.equals(product.getName(), productName));
	}
	
	@Override
	public List<Supplier> filterBy(List<Supplier> list, String filterType, String filterValue) {
		if ("product".equals(filterType))
			return filterByProductName(list, filterValue);
		return list;
	}
	
	@Override
	public List<Supplier> sortBy(List<Supplier> list, String sortType, String sortValue) { // supplier only supports name, so just return the whole list
		return list;
	}
	
	@Override
	protected Class<Supplier> getModelClass() {
		return Supplier.class;
	}
	
}
