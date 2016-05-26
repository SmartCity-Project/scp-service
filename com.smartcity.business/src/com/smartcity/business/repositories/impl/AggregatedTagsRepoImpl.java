/**
 * 
 */
package com.smartcity.business.repositories.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.smartcity.data.tagging.AggregatedTags;
import com.smartcity.data.tagging.Tag;
import com.smartcity.data.voting.AggregatedVotes;

/**
 * @author gperreas
 *
 */
public class AggregatedTagsRepoImpl 
	extends BaseRepositoryImpl<AggregatedTags>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<AggregatedTags> getModelType() {
		return AggregatedTags.class;
	}
	
	public AggregatedTags updateTagTotalUses(Tag tag) {
		Query query = new Query();
		
		Update update = new Update();
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.returnNew(true);
	
		AggregatedTags at = this.mt.findAndModify(
				query.addCriteria(
						where("tag.name").is(tag.getName())), 
				update.inc("totalUses", +1), 
				findAndModifyOptions,
				getModelType());
		
		return at;
	}

}
