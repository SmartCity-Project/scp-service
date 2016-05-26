package com.smartcity.data.access;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gperreas
 *
 */
@Document
public class Resource 
	implements Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String name;
	
	@DBRef(lazy=true)
	private Permission permission;
	
	private boolean readable;
	
	private boolean writable;
	
	private boolean creatable;
	
	private boolean modifiable;
	
	public Resource() {}


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
	 * @return the permission
	 */
	public Permission getPermission() {
		return permission;
	}


	/**
	 * @param permission the permission to set
	 */
	public void setPermission(Permission permission) {
		this.permission = permission;
	}


	/**
	 * @return the readable
	 */
	public boolean isReadable() {
		return readable;
	}


	/**
	 * @param readable the readable to set
	 */
	public void setReadable(boolean readable) {
		this.readable = readable;
	}


	/**
	 * @return the writable
	 */
	public boolean isWritable() {
		return writable;
	}


	/**
	 * @param writeble the writable to set
	 */
	public void setWritable(boolean writable) {
		this.writable = writable;
	}


	/**
	 * @return the modifiable
	 */
	public boolean isModifiable() {
		return modifiable;
	}


	/**
	 * @param modifiable the modifiable to set
	 */
	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}


	/**
	 * @return the creatable
	 */
	public boolean isCreatable() {
		return creatable;
	}


	/**
	 * @param creatable the creatable to set
	 */
	public void setCreatable(boolean creatable) {
		this.creatable = creatable;
	}

}
