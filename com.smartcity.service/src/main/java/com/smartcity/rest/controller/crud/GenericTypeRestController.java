/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.HashMap;
import java.util.Map;

import com.smartcity.data.common.Event;
import com.smartcity.data.common.Petition;

/**
 * @author gperreas
 *
 */
public abstract class GenericTypeRestController
	extends BaseRestController
{
	private Map<String, Class> documentTypeMap;
	
	protected abstract Map<String,Class> initDocumentTypeMap();
	
	protected GenericTypeRestController() {		
		this.documentTypeMap = initDocumentTypeMap();
		
		if(this.documentTypeMap==null)
			throw new NullPointerException("documentTypeMap cannot be null");
		if(this.documentTypeMap.isEmpty())
			throw new NullPointerException("documentTypeMap cannot be empty");
	}
	
	protected Map<String, Object> checkClassPath(Class documentTypeClass, String documentType) {
		getResponseMap().clear();
		if(documentTypeClass==null) {
			getResponseMap().put("error", "this " + documentType +" not supported");
		}
		return getResponseMap();
	}
	
	protected Class<?> getClassBasedOnPath(String pathParam) {
		if(pathParam!=null) {
			return this.documentTypeMap.get(pathParam);
		}
		
		return null;
	}
}
