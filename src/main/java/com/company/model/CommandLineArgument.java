package com.company.model;

import java.util.Arrays;
import java.util.List;

public class CommandLineArgument {

	private String name;
	private String value;
	
	public CommandLineArgument(String arg) {
		this.parse(arg);
	}
	
	private void parse(String arg) {
		if (arg.startsWith("\"") && arg.endsWith("\""))
			arg = arg.substring(1, arg.length() -2);
		List<String> parts = Arrays.asList(arg.split("="));
		this.name = (!parts.isEmpty()) ? parts.get(0) : new String();
		this.value = (parts.size() > 1) ? parts.get(1) : new String();
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getValue() {
		return this.value;
	}

}
