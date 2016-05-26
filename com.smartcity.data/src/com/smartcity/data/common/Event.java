package com.smartcity.data.common;


import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gperreas
 *
 */
@Document
public class Event
	extends Article
{
	private static final long serialVersionUID = 1L;

	private String locationId;
	
	private String organizationId;
	
	private EventState eventState;
	
	@GeoSpatialIndexed(name="loc_index",type=GeoSpatialIndexType.GEO_2DSPHERE)
	private GeoJsonPoint location;
	
	public Event() {}

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
	 * @return the locationId
	 */
	public String getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
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
