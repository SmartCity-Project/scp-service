/**
 * 
 */
package com.smartcity.business.repositories.impl;

import org.springframework.stereotype.Component;

import com.smartcity.data.access.Permission;

/**
 * @author gperreas
 *
 */
public class PermissionRepoImpl 
	extends BaseRepositoryImpl<Permission>
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Permission> getModelType() {
		return Permission.class;
	}

}
