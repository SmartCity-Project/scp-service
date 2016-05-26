/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcity.business.repositories.impl.LocationRepoImpl;
import com.smartcity.data.geo.Location;

/**
 * @author gperreas
 *
 */
@RestController
@RequestMapping(value="/api/locations")
public class LocationRestController
	extends BaseRestController
{	
	private Logger log = LoggerFactory.getLogger(LocationRestController.class);

	@Autowired
	private LocationRepoImpl locationRepoImpl;
	
	@RequestMapping(value="/find", 
					method=RequestMethod.POST, 
					consumes=MediaType.APPLICATION_JSON_VALUE)
	public Location findLocation(@RequestBody Point point) {
		
		return locationRepoImpl.findByLocationWithinPolygon(point);
	}
	
	@RequestMapping(value="/get", 
			method=RequestMethod.GET, 
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public Location findLocationById(@RequestParam String id,
			@RequestParam boolean withCoordinates) {
	
		return locationRepoImpl.findByLocationId(id, withCoordinates);
	}
	
	@RequestMapping(value="/find",
			method=RequestMethod.GET, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Location> findSimilar(@RequestParam String name, 
			@RequestParam boolean withCoordinates) {
	
		return locationRepoImpl.findSimilarByLocationName(name, withCoordinates);
	}
	
	@RequestMapping(value="/getAll",
					method=RequestMethod.GET, 
					produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Location> getAll(@RequestParam boolean withCoordinates) {
		
		return locationRepoImpl.getAllLocations(withCoordinates);
	}
	
}
