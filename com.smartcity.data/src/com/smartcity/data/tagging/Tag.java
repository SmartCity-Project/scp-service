/**
 * 
 */
package com.smartcity.data.tagging;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gperreas
 *
 */
@Document
public class Tag {

	@Id
	@Indexed(unique=true)
	private String name;
	
	public Tag() {}
	
	public Tag(String name) {
		this.setName(name);
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
	
	
}
