package com.company.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.company.model.NamedObject;
import com.company.service.iface.INamedListService;

public abstract class AbstractNamedService<T extends NamedObject> extends AbstractService<T> implements INamedListService<T> {
	
	public AbstractNamedService(Logger logr) {
		super(logr);
	}
	
	@Override
	public List<T> filterByName(List<T> list, String name) {
		if (list == null)
			list = this.list();
		return list.stream()
			.filter(x -> name.equals(x.getName()))
			.collect(Collectors.toList());
	}
	
	public List<T> filterByNameParameter(List<T> list, String filter) {
		String requestedName = filter.substring(5, filter.length());
		return filterByName(list, requestedName);
	}
	
	@Override
	public List<T> sortByName(List<T> list) {
		list.sort((s1, s2) -> s1.getName().compareTo(s2.getName()));
		return list;
	}

	public List<T> filterByParameter(List<T> objectList, String filter) {
		String[] parts = filter.split(":");
		if (parts.length > 1)
			return this.filterBy(objectList, parts[0].toLowerCase(), parts[1]);
		return objectList;
	}
	
	public List<T> sortByParameter(List<T> objectList, String sorting) {
		String[] parts = sorting.split(":");
		if (parts.length > 1)
			return this.sortBy(objectList, parts[0], parts[1]);
		return objectList;
	}
	
	public abstract List<T> filterBy(List<T> list, String filterType, String filterValue);
	public abstract List<T> sortBy(List<T> list, String sortType, String sortValue);
}
