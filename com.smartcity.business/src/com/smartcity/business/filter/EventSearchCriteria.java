/**
 * 
 */
package com.smartcity.business.filter;

import java.util.Collections;
import java.util.Set;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.smartcity.data.common.EventState;
import com.smartcity.data.geo.Location;
import com.smartcity.data.tagging.Tag;

/**
 * @author gperreas
 *
 */
public class EventSearchCriteria 
	extends SearchCriteria
{
	private static final long serialVersionUID = 1L;
	
	private String authorId;
	
	private String organizationId;

	private GeoJsonPoint location;
	
	private double radius;
	
	private String title;
	
	private Set<Location> locations = Collections.emptySet();
	
	private Set<Tag> tags = Collections.emptySet();
	
	private EventState eventState;
	
	public EventSearchCriteria() {
		super();
	}

	/**
	 * @return the authorId
	 */
	public String getAuthorId() {
		return authorId;
	}

	/**
	 * @param authorId the authorId to set
	 */
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	/**
	 * @return the location
	 */
	public GeoJsonPoint getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(GeoJsonPoint location) {
		this.location = location;
	}

	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(double radius) {
		this.radius = radius;
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
	 * @return the eventState
	 */
	public EventState getEventState() {
		return eventState;
	}

	/**
	 * @param eventState the eventState to set
	 */
	public void setEventState(EventState eventState) {
		this.eventState = eventState;
	}

	/**
	 * @return the locations
	 */
	public Set<Location> getLocations() {
		return locations;
	}

	/**
	 * @param locations the locations to set
	 */
	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

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
	
	
}
