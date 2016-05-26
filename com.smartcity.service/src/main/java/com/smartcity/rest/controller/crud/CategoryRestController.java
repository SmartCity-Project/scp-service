/**
 * 
 */
package com.smartcity.rest.controller.crud;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartcity.business.repositories.impl.CategoryRepoImpl;
import com.smartcity.business.rest.IBaseCrudRestController;
import com.smartcity.data.common.Category;
import com.smartcity.data.tagging.Tag;

/**
 * @author gperreas
 *
 */
@RestController
@RequestMapping(value="/api/crud/categories")
public class CategoryRestController
	extends BaseRestController
	implements IBaseCrudRestController<Category, Long>
{
	private Logger log = LoggerFactory.getLogger(CategoryRestController.class);
	
	@Autowired
	private CategoryRepoImpl categoryRepoImpl;

	@Override
	public Map<String, Object> save(Category json) {

		return null;
	}

	@Override
	public Map<String, Object> edit(Category json) {

		return null;
	}

	@Override
	public boolean delete(Long id) {

		return false;
	}

}
