/**
 * 
 */
package com.smartcity.business.repositories.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.comments.AggregatedComments;
import com.smartcity.data.petition.AggregatedSignatures;
import com.smartcity.data.petition.Signature;

/**
 * @author gperreas
 *
 */
public class AggregatedSignaturesRepoImpl 
	extends BaseRepositoryImpl<AggregatedSignatures>
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<AggregatedSignatures> getModelType() {
		return AggregatedSignatures.class;
	}
	
	/**
	 * @param signature
	 * @return
	 */
	public AggregatedSignatures updateAggregatedSignatures(Signature s) {
		Query query = new Query();
		
		Update update = new Update();
		update.inc("totalSignatures", getIncreasedNumberByCanceled(s.isCanceled()));
		
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.returnNew(true);
	
		AggregatedSignatures av = this.mt.findAndModify(
				query.addCriteria(
						where("documentId").is(s.getPetitionId())), 
				update, 
				findAndModifyOptions,
				getModelType());
		
		return av;
	}

	private Number getIncreasedNumberByCanceled(boolean canceled) {
		if(!canceled) {
			return +1;
		}
		else {
			return -1;
		}	
	}

}
