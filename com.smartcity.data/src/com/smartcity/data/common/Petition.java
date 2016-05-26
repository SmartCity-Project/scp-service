/**
 * 
 */
package com.smartcity.data.common;

import java.util.Date;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.smartcity.data.access.Organization;
import com.smartcity.data.petition.PetitionStatusEnum;

/**
 * @author gperreas
 *
 */
@Document
@CompoundIndex(name = "aId_orgId_idx", def = "{'id' : 1, 'organizationId': 1}")
public class Petition 
	extends Article
{
	private static final long serialVersionUID = 1L;
	
	@Indexed(name="orgIdx")
	private String organizationId;
	
	private PetitionStatusEnum petitionStatus;
	
	private int goal = 1;
	
	private Date endBy;


	/**
	 * @return the organizationId
	 */
	public String getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the petitionStatus
	 */
	public PetitionStatusEnum getPetitionStatus() {
		return petitionStatus;
	}

	/**
	 * @param petitionStatus the petitionStatus to set
	 */
	public void setPetitionStatus(PetitionStatusEnum petitionStatus) {
		this.petitionStatus = petitionStatus;
	}

	/**
	 * @return the goal
	 */
	public int getGoal() {
		return goal;
	}

	/**
	 * @param goal the goal to set
	 */
	public void setGoal(int goal) {
		this.goal = goal;
	}

	/**
	 * @return the endBy
	 */
	public Date getEndBy() {
		return endBy;
	}

	/**
	 * @param endBy the endBy to set
	 */
	public void setEndBy(Date endBy) {
		this.endBy = endBy;
	}
	
}
