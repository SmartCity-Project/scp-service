package com.smartcity.data.geo;

import java.io.Serializable;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gperreas
 *
 */

@Document
@CompoundIndex(name = "id_name_idx", def = "{'id' : 1, 'name' : 1}")
public class Location 
	implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
		
	private String name;
	
	private String parentId;
	
	private int depthValue = 0;
	
	@DBRef
	private Set<Location> children;
	
	@GeoSpatialIndexed(name="index",type=GeoSpatialIndexType.GEO_2DSPHERE)
	private GeoJsonMultiPolygon multiPolygon;
	
	public Location() {}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the depthValue
	 */
	public int getDepthValue() {
		return depthValue;
	}

	/**
	 * @param depthValue the depthValue to set
	 */
	public void setDepthValue(int depthValue) {
		this.depthValue = depthValue;
	}

	/**
	 * @return the children
	 */
	public Set<Location> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(Set<Location> children) {
		this.children = children;
	}

	/**
	 * @return the multiPolygon
	 */
	public GeoJsonMultiPolygon getMultiPolygon() {
		return multiPolygon;
	}

	/**
	 * @param multiPolygon the multiPolygon to set
	 */
	public void setMultiPolygon(GeoJsonMultiPolygon multiPolygon) {
		this.multiPolygon = multiPolygon;
	}

}
