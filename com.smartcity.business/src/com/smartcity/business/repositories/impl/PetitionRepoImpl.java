/**
 * 
 */
package com.smartcity.business.repositories.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import com.smartcity.data.common.Petition;
import com.smartcity.data.petition.PetitionStatusEnum;

/**
 * @author gperreas
 *
 */
public class PetitionRepoImpl 
	extends BaseRbacRepositoryImpl<Petition>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Petition> getModelType() {
		return Petition.class;
	}

	/**
	 * Total petitions per organization
	 * 
	 * @return count
	 */
	public long countPetitions(String orgId) {
		Assert.notNull(orgId);
		return super.count(new Query(Criteria.where("organizationId").is(orgId)));
	}

	/**
	 * Total petitions
	 * 
	 * @return count
	 */
	public long countPetitions() {
		return super.count(new Query());
	}

	public void modifyPetitionStatusByEndsDateAndNotComplete(Date endBy, PetitionStatusEnum status) {
		Assert.notNull(endBy);
		Assert.notNull(status);
		Query query = new Query();
		
		Update update = new Update();
		update.set("petitionStatus", status);
		
		this.mt.updateMulti(query.addCriteria(Criteria.where("endBy").lte(endBy).andOperator(
				Criteria.where("petitionStatus").ne(PetitionStatusEnum.completed))), update, getModelType());
	}


}
