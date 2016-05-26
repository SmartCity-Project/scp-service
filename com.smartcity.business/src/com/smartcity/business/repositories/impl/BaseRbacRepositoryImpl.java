/**
 * 
 */
package com.smartcity.business.repositories.impl;


import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;

import com.smartcity.business.security.authentication.RbacController;
import com.smartcity.data.access.Resource;
import com.smartcity.data.access.User;
import com.smartcity.data.tracking.ITracking;
import com.smartcity.exceptions.AccessDeniedException;

/**
 * @author gperreas
 *
 */
public abstract class BaseRbacRepositoryImpl<T>
	extends BaseRepositoryImpl<T>
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void onApplicationEvent(MongoMappingEvent<?> event) {
		super.onApplicationEvent(event);
	}
	
	@Override
	public void onBeforeConvert(BeforeConvertEvent<T> event) {
		super.onBeforeConvert(event);
	}
	
	@Override
	public void onBeforeSave(BeforeSaveEvent<T> event) {
		
		if (!canWrite(event.getSource()))
			throw new AccessDeniedException(event.getSource());
		
		super.onBeforeSave(event);
	}
	
	@Override
	public void onBeforeDelete(BeforeDeleteEvent<T> event) {
		super.onBeforeDelete(event);
	}
	
	private boolean canWrite(Object object) {
		
		Resource resource = getEntityAccess(object);
		
		if(resource == null) return false;
			
		if(object instanceof ITracking) {
			ITracking trackedRepo = (ITracking)object;
			
			if(resource.isWritable()) {
				
				User createdBy = this.trackingController.getCreator(trackedRepo);

				if (createdBy != null) {
					if(trackedRepo.getTracking().getModifiedBy()==null) {
						if(resource.isCreatable()) {
							return true;
						}
					} else {
						if(resource.isModifiable()) {
							return true;
						}
					}
				}
				else {
					return false;
				}
			}
		}
		else {
			if(resource.isWritable()) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean canRead(Object object) {
		
		Resource resource = getEntityAccess(object);
		
		if(resource == null) return false;

		if(object instanceof ITracking) {
			ITracking trackedDocument = (ITracking)object;
			
			if(resource.isReadable()) {	
				return true;
			}
		}
		else if(resource.isReadable()) {	
			return true;
		}
		
		return false;
	}
	
	/**
	 * @param document
	 * @return Resource type
	 */
	protected Resource getEntityAccess(Object object) {
		
		String documentName = object.getClass().getCanonicalName();
		
		if(documentName == null) return null;
		
		return RbacController.getInstance().hasResourceAccess(documentName);
	}

}
