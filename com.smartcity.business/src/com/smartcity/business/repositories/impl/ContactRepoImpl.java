/**
 * 
 */
package com.smartcity.business.repositories.impl;

import com.smartcity.data.common.Contact;

/**
 * @author gperreas
 *
 */
public class ContactRepoImpl
	extends BaseRepositoryImpl<Contact>
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Contact> getModelType() {
		return Contact.class;
	}

}
