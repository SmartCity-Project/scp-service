/**
 * 
 */
package com.smartcity.business.repositories.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.smartcity.data.tagging.Tag;

/**
 * @author gperreas
 *
 */
public class TagRepoImpl 
	extends BaseRepositoryImpl<Tag>
{
	
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Tag> getModelType() {
		return Tag.class;
	}
	
	public List<Tag> findSimilarTags(Tag tag) {
		return findSimilarTags(tag.getName());
	}
	
	public List<Tag> findSimilarTags(String tagName) {
	
		Query query = new Query();
		
		if(tagName!=null) {
			if(!tagName.isEmpty()) {
				query.addCriteria(Criteria.where("name").regex(tagName));
				return this.mt.find(query, getModelType());
			} 
		}
		
		return null;
	}

}
