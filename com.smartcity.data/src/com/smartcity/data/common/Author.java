/**
 * 
 */
package com.smartcity.data.common;

/**
 * @author gperreas
 *
 */
public class Author {
	
	private String id;
	
	private String name;
	
	private boolean isOrganizationAuthor;
		
	public Author() {}

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
	 * @return the isOrganizationAuthor
	 */
	public boolean isOrganizationAuthor() {
		return isOrganizationAuthor;
	}

	/**
	 * @param isOrganizationAuthor the isOrganizationAuthor to set
	 */
	public void setOrganizationAuthor(boolean isOrganizationAuthor) {
		this.isOrganizationAuthor = isOrganizationAuthor;
	}

}
