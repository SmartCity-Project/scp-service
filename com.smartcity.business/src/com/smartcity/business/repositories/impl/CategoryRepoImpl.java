/**
 * 
 */
package com.smartcity.business.repositories.impl;

import org.springframework.stereotype.Component;

import com.smartcity.data.common.Category;

/**
 * @author gperreas
 *
 */
public class CategoryRepoImpl 
	extends BaseRbacRepositoryImpl<Category>
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Category> getModelType() {
		return Category.class;
	}


}
