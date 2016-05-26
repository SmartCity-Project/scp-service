/**
 * 
 */
package com.smartcity.business.rest;

import java.util.List;
import java.util.Map;

import com.smartcity.data.tagging.Tag;

/**
 * @author gperreas
 *
 */
public interface ITagRestController {
	
	Map<String, Object> saveTag(Tag tag);
	
	Map<String, Object> findTag(String name);

}
