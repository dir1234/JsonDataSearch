package com.company.model;

import com.company.model.iface.INamedObject;

public abstract class NamedObject implements INamedObject {

	private String name;
	
	public NamedObject() {}
	
	public NamedObject(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
