/**
 * 
 */
package com.smartcity.business.repositories.impl;

import org.springframework.stereotype.Component;

import com.smartcity.data.access.Role;

/**
 * @author gperreas
 *
 */
public class RoleRepoImpl 
	extends BaseRepositoryImpl<Role>
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Role> getModelType() {
		return Role.class;
	}

}
