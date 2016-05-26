package com.smartcity.business.repositories.impl;

import java.util.List;
import java.util.Set;

import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smartcity.business.controllers.ILocationController;
import com.smartcity.data.geo.Location;

/**
 * @author gperreas
 *
 */
public class LocationRepoImpl 
	extends BaseRepositoryImpl<Location>
	implements ILocationController
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Location> getModelType() {
		return Location.class;
	}

	@Override
	public Location findByLocationWithinPolygon(Point point) {

		//implement query - combine with Geometry collection
		Query query = new Query();

		return this.mt.findOne(
				query.addCriteria(
						Criteria.where("multiPolygon")
								.intersects(new GeoJsonPoint(point))), 
				getModelType());
	}
	
	public Location findByLocationId(String id, boolean withCoordinates) {
		Assert.notNull(id);
		Query query = new Query();
		
		query.fields().include("id");
		query.fields().include("name");
		query.fields().include("parentId");
		query.fields().include("depthValue");
		query.fields().include("children");
		
		if(withCoordinates) {
			query.fields().include("multiPolygon");
		}
		
		if(!id.isEmpty()) {
			query.addCriteria(Criteria.where("id").is(id));
			return this.mt.findOne(query, getModelType());
		} 
		
		return null;
	}
	
	public List<Location> findSimilarByLocationName(String name, boolean withCoordinates) {
		
		Query query = new Query();
		
		query.fields().include("id");
		query.fields().include("name");
		query.fields().include("parentId");
		query.fields().include("depthValue");
		query.fields().include("children");
		
		if(withCoordinates) {
			query.fields().include("multiPolygon");
		}
		
		if(name!=null) {
			if(!name.isEmpty()) {
				query.addCriteria(Criteria.where("name").regex(name));
				return this.mt.find(query, getModelType());
			} 
		}
		
		return null;
	}
	
	/**
	 * Get all locations without spatial data
	 */
	public List<Location> getAllLocations(boolean withCoordinates) {
		
		Query query = new Query();

		query.fields().include("id");
		query.fields().include("name");
		query.fields().include("parentId");
		query.fields().include("depthValue");
		query.fields().include("children");
				
		if(withCoordinates) {
			query.fields().include("multiPolygon");
		}
		
		return this.mt.find(query,getModelType());
	}

	public String getLocationId(Point point) {
		Location location = findByLocationWithinPolygon(point);
		
		return (location!=null)?location.getId():null;
	}	
	
}
