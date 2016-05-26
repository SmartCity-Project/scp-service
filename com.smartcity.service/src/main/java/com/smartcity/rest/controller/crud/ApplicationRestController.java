/**
 * 
 */
package com.smartcity.rest.controller.crud;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gperreas
 *
 */
@RestController
@RequestMapping("/api/")
public class ApplicationRestController
	extends BaseRestController
{

	@RequestMapping("/")
	public String root() {
		return "This is the home of the SmmartCity-Project Rest API";
	}
	
}
