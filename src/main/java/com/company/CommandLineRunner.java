package com.company;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.ValidationException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.company.model.CommandLineArgument;
import com.company.model.NamedObject;
import com.company.service.AbstractNamedService;
import com.company.service.CustomerService;
import com.company.service.ProductService;
import com.company.service.ProductSupplierService;
import com.company.service.SupplierService;

@Component
public class CommandLineRunner<T extends NamedObject> implements ApplicationRunner { // No Unit Tests, tested by CLI
	
	private static final String PROPERTY_GENERATE_PRODUCT_SUPPLIERS 	= "generateSuppliers";
	private static final String PROPERTY_DOCTYPE 						= "doctype";
	private static final String PROPERTY_FILTER 						= "filter";
	private static final String PROPERTY_SORT 							= "sort";
	
	private static final String OPTION_NAME	= "name:";
	
	private static final Logger logger = Logger.getLogger(CommandLineRunner.class);
	
	@Autowired
	private ProductService productService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private ProductSupplierService productSupplierService;
	
	private Map<String, String> arguments;
	private AbstractNamedService<T> service = null;
	private List<T> objectList = null;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		initialiseArguments(Arrays.asList(args.getSourceArgs()));
		
		if (!sanityCheckRequiredParamsAreOK())
			return;

		String docType = this.getCommandLineArguments().get(PROPERTY_DOCTYPE);
		try {
		processDocType(docType);
		if (this.getCommandLineArguments().containsKey(PROPERTY_FILTER))
			this.objectList = processFiltering(docType, this.getCommandLineArguments().get(PROPERTY_FILTER));
		if (this.getCommandLineArguments().containsKey(PROPERTY_SORT)) 
			this.objectList = processSorting(docType, this.getCommandLineArguments().get(PROPERTY_SORT));
		
		JSONArray json = new JSONArray(this.objectList);
		
		System.out.println(json.toString(4)); // logger output is disabled by logback configuration, should really write to appropriate Stream, but requirements imply this is only ever going to output to console
		
		} catch (ValidationException vex) {
			System.out.println(vex.getMessage()); // as above, logger disabled
		}
	}

	private boolean sanityCheckRequiredParamsAreOK() {
		if (this.getCommandLineArguments().containsKey(PROPERTY_GENERATE_PRODUCT_SUPPLIERS)) {
			String filename = this.getCommandLineArguments().get(PROPERTY_GENERATE_PRODUCT_SUPPLIERS);
			productSupplierService.generateJsonDataFile(filename);
			System.out.println("JSON data output to " + filename);
			return false;
		}
		
		if (!this.getCommandLineArguments().containsKey(PROPERTY_DOCTYPE)) {
			logger.error("No document type specified");
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private void processDocType(String docType) throws ValidationException {
		switch (docType) {
		case "customer":
			objectList = (List<T>) customerService.list();
			service = (AbstractNamedService<T>) customerService;
			break;
		case "product" :
			objectList = (List<T>) productService.list();
			service = (AbstractNamedService<T>) productService;
			break;
		case "supplier" :
			objectList = (List<T>) supplierService.list();
			service = (AbstractNamedService<T>) supplierService;
			break;
		default : throw new ValidationException("Unrecognized Doctype!");
		}
	}
	
	private List<T> processFiltering(String docType, String filter) {
		if (filter.startsWith(OPTION_NAME)) {
			return this.service.filterByNameParameter(this.objectList, filter);
		} else return this.service.filterByParameter(this.objectList, filter);
	}
	
	private List<T> processSorting(String docType, String sorting) {
		if (sorting.equals(OPTION_NAME)) {
			return this.service.sortByName(this.objectList);
		} else return this.service.sortByParameter(this.objectList, sorting.toLowerCase());
	}
	
	private void initialiseArguments(List<String> args) {
		for (String arg : args) {
			CommandLineArgument argument = new CommandLineArgument(arg);
			this.getCommandLineArguments().put(argument.getName(), argument.getValue());
		}
	}
		
	private Map<String, String> getCommandLineArguments() {
		if (this.arguments == null)
			this.arguments = new HashMap<String, String>();
		return this.arguments;
	}
}
