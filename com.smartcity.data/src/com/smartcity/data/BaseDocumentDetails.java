/**
 * 
 */
package com.smartcity.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author gperreas
 *
 */
public class BaseDocumentDetails 
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String documentId;
	
    @JsonIgnore
	private String classRef;
    
    public BaseDocumentDetails() {}

	/**
	 * @return the documentId
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the classRef
	 */
	public String getClassRef() {
		return classRef;
	}

	/**
	 * @param classRef the classRef to set
	 */
	public void setClassRef(String classRef) {
		this.classRef = classRef;
	}
    
    

}
