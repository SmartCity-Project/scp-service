/**
 * 
 */
package com.smartcity.business.repositories.impl;

import com.smartcity.data.SequenceId;

/**
 * @author gperreas
 *
 */
public class SequenceRepoImpl 
	extends BaseRepositoryImpl<SequenceId>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<SequenceId> getModelType() {
		return SequenceId.class;
	}
}
