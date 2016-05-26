package com.smartcity.business.controllers;

import org.springframework.data.geo.Point;

import com.smartcity.data.geo.Location;

/**
 * @author gperreas
 *
 */
public interface ILocationController 
{
	Location findByLocationWithinPolygon(Point point);

}
