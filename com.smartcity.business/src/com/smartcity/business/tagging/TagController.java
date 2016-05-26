/**
 * 
 */
package com.smartcity.business.tagging;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smartcity.business.repositories.impl.AggregatedTagsRepoImpl;
import com.smartcity.business.repositories.impl.TagRepoImpl;
import com.smartcity.data.tagging.AggregatedTags;
import com.smartcity.data.tagging.Tag;

import org.springframework.dao.DuplicateKeyException;

/**
 * @author gperreas
 *
 */
@Component
public class TagController 
	implements ITagController
{
	@Autowired
	private TagRepoImpl tagRepoImpl;
	
	@Autowired
	private AggregatedTagsRepoImpl aggregatedTagsRepoImpl;
	
	public TagController() {
		
	}
	
	public TagController(TagRepoImpl tagRepoImpl, AggregatedTagsRepoImpl aggregatedTagsRepoImpl) {
		this.tagRepoImpl = tagRepoImpl;
		this.aggregatedTagsRepoImpl = aggregatedTagsRepoImpl;
	}
	
	@Override
	public Tag saveTag(Tag tag) {
		
		try {
			tagRepoImpl.save(tag);
			AggregatedTags at = new AggregatedTags();
			at.setTag(tag);
			at.setTotalUses(1);
			aggregatedTagsRepoImpl.save(at);
		} catch(DuplicateKeyException e) {
			updateAggregatedTags(tag);
		}
		
		return tag;
	}

	@Override
	public Tag saveTag(String tagName) {
		Tag tag = new Tag();
		tag.setName(tagName);
		return saveTag(tag);
	}

	@Override
	public boolean deleteTag(Tag tag) {
		return deleteTag(tag.getName());
	}

	@Override
	public boolean deleteTag(String tagName) {
		return tagRepoImpl.delete(tagName);
	}
	
	@Override
	public List<Tag> find(String tagName) {
		return tagRepoImpl.findSimilarTags(tagName);
	}

	@Override
	public AggregatedTags updateAggregatedTags(Tag tag) {
		Assert.notNull(tag);
		return aggregatedTagsRepoImpl.updateTagTotalUses(tag);
	}

}
