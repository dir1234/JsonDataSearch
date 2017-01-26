package com.company.service.iface;

import java.util.List;

import com.company.model.Supplier;

public interface ISupplierService extends INamedListService<Supplier> {

	public List<Supplier> filterByProductName(List<Supplier> suppliers, String productName);
	
}
