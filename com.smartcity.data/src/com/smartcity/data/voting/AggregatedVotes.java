/**
 * 
 */
package com.smartcity.data.voting;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartcity.data.BaseDocumentDetails;

/**
 * @author gperreas
 *
 */
@Document
@CompoundIndex(name = "docId_classRef_idx", def = "{'documentId' : 1, 'classRef': 1}")
public class AggregatedVotes
	extends BaseDocumentDetails
{
	
	private static final long serialVersionUID = 1L;
	
	private int totalPositives = 0;
	
	private int totalNegatives = 0;
	
	public AggregatedVotes() {}

	/**
	 * @return the totalPositives
	 */
	public int getTotalPositives() {
		return totalPositives;
	}

	/**
	 * @param totalPositives the totalPositives to set
	 */
	public void setTotalPositives(int totalPositives) {
		this.totalPositives = totalPositives;
	}

	/**
	 * @return the totalNegatives
	 */
	public int getTotalNegatives() {
		return totalNegatives;
	}

	/**
	 * @param totalNegatives the totalNegatives to set
	 */
	public void setTotalNegatives(int totalNegatives) {
		this.totalNegatives = totalNegatives;
	}

}
