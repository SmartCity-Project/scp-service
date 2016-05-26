/**
 * 
 */
package com.smartcity.business.filter;

import java.util.Date;
import java.util.Set;

import com.smartcity.data.petition.PetitionStatusEnum;
import com.smartcity.data.tagging.Tag;

/**
 * @author gperreas
 *
 */
public class PetitionSearchCriteria 
	extends SearchCriteria
{
	private static final long serialVersionUID = 1L;

	private String organizationId;

	private String title;
	
	private Set<Tag> tags;
	
	private Integer goal;
	
	private Date endBy;
	
	private PetitionStatusEnum petitionStatus;
	
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the tags
	 */
	public Set<Tag> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * @return the goal
	 */
	public Integer getGoal() {
		return goal;
	}

	/**
	 * @param goal the goal to set
	 */
	public void setGoal(Integer goal) {
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
	
}
