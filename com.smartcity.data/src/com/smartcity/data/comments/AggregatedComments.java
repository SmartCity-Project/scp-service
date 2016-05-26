/**
 * 
 */
package com.smartcity.data.comments;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.smartcity.data.BaseDocumentDetails;

/**
 * @author gperreas
 *
 */
@Document
@CompoundIndex(name = "docId_classRef_idx", def = "{'documentId' : 1, 'classRef': 1}")
public class AggregatedComments 
	extends BaseDocumentDetails
{
	private static final long serialVersionUID = 1L;
	
	private int totalComments = 0;

	/**
	 * @return the totalComments
	 */
	public int getTotalComments() {
		return totalComments;
	}

	/**
	 * @param totalComments the totalComments to set
	 */
	public void setTotalComments(int totalComments) {
		this.totalComments = totalComments;
	}
}
