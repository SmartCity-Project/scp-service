/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcity.business.rest.ITagRestController;
import com.smartcity.business.tagging.TagController;
import com.smartcity.data.tagging.Tag;

/**
 * @author gperreas
 *
 */
@RestController
@RequestMapping(value="/api/tags")
public class TagRestController
	extends BaseRestController
	implements ITagRestController
{
	private Logger log = LoggerFactory.getLogger(TagRestController.class);
	
	@Autowired
	private TagController tagController;
	
	@Override
	@RequestMapping(method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> saveTag(Tag tag) {
		getResponseMap().clear();
		
		getResponseMap().put("data", tagController.saveTag(tag));
		
		return getResponseMap();
	}

	@Override
	@RequestMapping(value="/find",
		method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> findTag(@RequestParam String name) {
		getResponseMap().clear();
		
		List<Tag> result = tagController.find(name);
		if(result!=null) {
			getResponseMap().put("data", result);
		} else {
			getResponseMap().put("error", "something went wrong");
		}
		
		return getResponseMap();
	}

}
