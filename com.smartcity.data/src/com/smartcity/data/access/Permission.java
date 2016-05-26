package com.smartcity.data.access;

import java.io.Serializable;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gperreas
 *
 */
@Document
public class Permission 
	implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@Indexed(unique=true)
	private String name;
	
	@DBRef
	private Set<Role> roles;
	
	@DBRef
	private Set<Resource> recources;

	
	public Permission() {}

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
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the recources
	 */
	public Set<Resource> getRecources() {
		return recources;
	}

	/**
	 * @param recources the recources to set
	 */
	public void setRecources(Set<Resource> recources) {
		this.recources = recources;
	}
	
}
