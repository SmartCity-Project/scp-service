package com.smartcity.data.common;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.smartcity.data.ISequenceId;
import com.smartcity.data.comments.IComment;
import com.smartcity.data.reporting.IReportDocument;
import com.smartcity.data.tagging.Tag;
import com.smartcity.data.tracking.ITracking;
import com.smartcity.data.tracking.Tracking;
import com.smartcity.data.voting.AggregatedVotes;
import com.smartcity.data.voting.IVotable;

/**
 * @author gperreas
 *
 */
@Document
public class Article 
	implements ISequenceId<Long>, ITracking<Long>, 
	IVotable<Long>, IComment<Long>, IReportDocument<Long>, Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	private String title;
	
	private String description;
	
	private Image image;
	
	@DBRef
	private Category category;
	
	private PublishStatus publishStatus = PublishStatus.published;
	
	private Tracking tracking;
	
	@DBRef
	private Set<Tag> tags = Collections.emptySet();
		
	public Article() {}
	
	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public Tracking getTracking() {
		return this.tracking;
	}

	@Override
	public void setTracking(Tracking tracking) {
		this.tracking = tracking;
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

	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
	
	public void removeTag(Tag tag) {
		this.tags.remove(tag);
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * @return the publishStatus
	 */
	public PublishStatus getPublishStatus() {
		return publishStatus;
	}

	/**
	 * @param publishStatus the publishStatus to set
	 */
	public void setPublishStatus(PublishStatus publishStatus) {
		this.publishStatus = publishStatus;
	}

}
