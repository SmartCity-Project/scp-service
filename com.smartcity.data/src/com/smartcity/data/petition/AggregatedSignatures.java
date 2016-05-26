/**
 * 
 */
package com.smartcity.data.petition;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gperreas
 *
 */
@Document
public class AggregatedSignatures {

	@Id
	private Long documentId;
	
	private int totalSignatures = 0;

	/**
	 * @return the documentId
	 */
	public Long getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the totalSignatures
	 */
	public int getTotalSignatures() {
		return totalSignatures;
	}

	/**
	 * @param totalSignatures the totalSignatures to set
	 */
	public void setTotalSignatures(int totalSignatures) {
		this.totalSignatures = totalSignatures;
	}
	
}
