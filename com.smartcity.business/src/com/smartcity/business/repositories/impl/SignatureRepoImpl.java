/**
 * 
 */
package com.smartcity.business.repositories.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Date;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;
import com.smartcity.data.petition.Signature;

/**
 * @author gperreas
 *
 */
public class SignatureRepoImpl
	extends BaseRepositoryImpl<Signature>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Signature> getModelType() {
		return Signature.class;
	}
	
	/**
	 * @param signature
	 */
	public boolean updateSignature(Signature s) {

		return false;
	}

	/**
	 * @param signature
	 */
	public boolean updateSignatureCancel(Signature s) {

		Query query = new Query();
		
		WriteResult wr = this.mt.updateFirst(
				query.addCriteria(
						where("petitionId").is(s.getPetitionId()).andOperator(
								where("email").is(s.getEmail()))),
				Update.update("canceled", s.isCanceled()), 
				getModelType());
		
		return wr.getN() == 1;
	}

	/**
	 * @param signature
	 * @return
	 */
	public Signature findExistSignature(Signature s) {
		Query query = new Query();

		return this.mt.findOne(
				query.addCriteria(
					where("petitionId").is(s.getPetitionId()).andOperator(
						where("email").is(s.getEmail()))), 
				getModelType());
	}

	
}
