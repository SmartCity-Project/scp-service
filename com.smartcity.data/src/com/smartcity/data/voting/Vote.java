package com.smartcity.data.voting;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.common.Event;

/**
 * @author gperreas
 *
 */
@Document
@CompoundIndex(name = "vid_docId_classRef_idx", def = "{'voterId' : 1, 'documentId' : 1, 'classRef': 1}")
public class Vote 
	extends BaseDocumentDetails
{
	private static final long serialVersionUID = 1L;

	private String voterId;
	
	private Date created;
	
	private Date modified;
	
	private boolean canceled;
	
	private VoteTypeEnum type;
	
	public Vote() {}

	/**
	 * @return the voterId
	 */
	public String getVoterId() {
		return voterId;
	}

	/**
	 * @param voterId the voterId to set
	 */
	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}

	/**
	 * @return the type
	 */
	public VoteTypeEnum getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(VoteTypeEnum type) {
		this.type = type;
	}

	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * @param canceled the canceled to set
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}
	
}
