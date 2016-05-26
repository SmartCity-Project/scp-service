/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.smartcity.business.repositories.IRepository;
import com.smartcity.business.rest.IBaseCrudRestController;


/**
 * @author gperreas
 *
 */
//@RequestMapping(value="/api/crud")
public abstract class CrudRestController<T, ID extends Serializable> 
	extends BaseRestController
	implements IBaseCrudRestController<T, ID>
{
	private Logger log = LoggerFactory.getLogger(CrudRestController.class);
			
	protected abstract IRepository<T> getRepositoy();

	@Override
//	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> save(@RequestBody T json) {

		getRepositoy().save(json);
		
		return getResponseMap();
	}

	@Override
//	@RequestMapping(value="/edit", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> edit(@RequestBody T json) {

		getRepositoy().update(json);
		
		return getResponseMap();
	}

	@Override
//	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public boolean delete(@PathVariable ID id) {

		boolean result = getRepositoy().delete(id);
		
		return result;
	}
	
}
