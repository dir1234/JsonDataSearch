package com.company.service.iface;

import java.util.List;

import com.company.model.NamedObject;

public interface INamedListService<NO extends NamedObject> extends IService<NO> {
	
	public List<NO> filterByName(List<NO> list, String name);
	
	public List<NO> sortByName(List<NO> list);
}
