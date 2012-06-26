package com.mangofactory.swagger.springmvc;

import org.apache.commons.lang.NotImplementedException;


/**
 * Represents a class from the Model, described in the Swagger format
 * @author martypitt
 *
 */
public class MvcModelResource {
	// NOTE : Currently, this class handles the parsing, as well as the POJO aspects.
	// should probably split this out later.
	
	private final Class<?> modelClass;

	public MvcModelResource(Class<?> modelClass)
	{
		this.modelClass = modelClass;
	}

	public ModelProperty getProperty(String propertyName) {
		throw new NotImplementedException();
	}
	
}
