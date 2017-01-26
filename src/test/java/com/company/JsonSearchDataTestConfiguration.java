package com.company;

import java.io.File;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonSearchDataTestConfiguration {

	private Function<String, File> getResourceFile = filename -> {
		ClassLoader cl = getClass().getClassLoader();
		String path = null;
		try {
			path = cl.getResource(filename).getFile();
		} catch (NullPointerException npe) {
			// file not found, other areas of code advised to expect null value!
		}
		if (path != null) {
			File f = new File(cl.getResource(filename).getFile());
			return f;
		}
		return null;
	};
	
	// Data Files
	
	@Bean(name="customerFile")
	public File customerFile() {
		return getResourceFile.apply("customers.json");
	}
	
	@Bean(name="missingFile")
	public File missingFile() {
		return getResourceFile.apply("missing.json");
	}
	
	@Bean(name="corruptFile")
	public File corruptFile() {
		return getResourceFile.apply("corrupt.json");
	}
	
	// Service stubs
	
}
