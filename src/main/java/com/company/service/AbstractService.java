package com.company.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.company.service.iface.IService;
import com.google.gson.Gson;

import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public abstract class AbstractService<T> implements IService<T> {
	
	private static final Gson gson = new Gson();
	
	protected List<T> objects;
	private Logger logger;
	private InputStream fileStore;
	
	public AbstractService(Logger logr) {
		this.logger = logr;
	}
	
	public void setLogger(Logger logr) {
		this.logger = logr;
	}
	
	protected void setFileStore(InputStream jsonData) {
		if (jsonData != null) {
			this.fileStore = jsonData;
			readFileStore();
		} else {
			String msg = "Required file is missing: ";
			logger.warn( (jsonData == null) ? msg + "Unknown" : msg);
		}
	}
	
	protected InputStream getFileStore() {
		return this.fileStore;
	}
	
	private void readFileStore() {
		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		
		try (Reader is = new InputStreamReader(this.fileStore)) {
			JSONArray jsonArray = (JSONArray) parser.parse(is);
			
			objects = new ArrayList<T>();
			for (Object item : jsonArray) {
				objects.add(gson.fromJson(item.toString(), getModelClass()));
			};
			
		} catch (IOException | ParseException ex) {
			logger.error("Exception occured during parsing! Datafile is likely corrupt.", ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> list() {
		if (objects == null) {
			if (this.fileStore != null) {
				this.readFileStore();
				return this.list();
			}
			return new ArrayList<T>();
		}
		return (List<T>) (List<?>) objects; // smelly code, double casting to obey interface segregation - 
											// should discuss approach with architecture perhaps NamedObject should be considered
											// obsolete? Customer/Product/Supplier could own "name" member. Would also require
											// duplication of sortByName(). Overall, probably less evil to double cast.
	}

	protected abstract Class<T> getModelClass();
	
}
