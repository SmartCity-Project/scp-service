/**
 * 
 */
package com.smartcity.data.tagging;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gperreas
 *
 */
@Document
public class AggregatedTags {
	
	@Indexed(unique=true)
	private Tag tag;
	
	private int totalUses = 0;

	/**
	 * @return the tag
	 */
	public Tag getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(Tag tag) {
		this.tag = tag;
	}

	/**
	 * @return the totalUses
	 */
	public int getTotalUses() {
		return totalUses;
	}

	/**
	 * @param totalUses the totalUses to set
	 */
	public void setTotalUses(int totalUses) {
		this.totalUses = totalUses;
	}

}
