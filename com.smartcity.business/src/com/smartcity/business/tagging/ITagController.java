/**
 * 
 */
package com.smartcity.business.tagging;

import java.util.List;

import com.smartcity.data.tagging.AggregatedTags;
import com.smartcity.data.tagging.Tag;

/**
 * @author gperreas
 *
 */
public interface ITagController {
	
	Tag saveTag(Tag tag);
	
	Tag saveTag(String tagName);
	
	boolean deleteTag(Tag tag);
	
	boolean deleteTag(String tagName);
	
	List<Tag> find(String tagName);
	
	AggregatedTags updateAggregatedTags(Tag tag);

}
