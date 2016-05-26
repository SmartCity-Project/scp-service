/**
 * 
 */
package com.smartcity.business.repositories.impl;

import org.springframework.stereotype.Component;

import com.smartcity.data.access.Resource;

/**
 * @author gperreas
 *
 */
public class ResourceRepoImpl 
	extends BaseRepositoryImpl<Resource>
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Resource> getModelType() {
		return Resource.class;
	}

}
