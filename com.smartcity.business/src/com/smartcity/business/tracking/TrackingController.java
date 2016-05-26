package com.smartcity.business.tracking;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.smartcity.business.repositories.impl.UserRepoImpl;
import com.smartcity.data.access.User;
import com.smartcity.data.tracking.ITracking;
import com.smartcity.data.tracking.Tracking;

/**
 * 
 */

/**
 * @author gperreas
 *
 */
@Component
public class TrackingController 
{
	//Logger
	
	@Autowired
	private UserRepoImpl userRepoImpl;
	
	public TrackingController(UserRepoImpl userRepoImpl) {
		this.userRepoImpl = userRepoImpl;
	}
	
	public TrackingController() {}
	
	public Tracking tracking(Tracking track) {
		Tracking t = track;
		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(t==null) {
			t = new Tracking();
			t.setCreated(new Date());
			t.setCreatedBy(current.getId());
		}
		else {
			t.setModified(new Date());
			t.setModifiedBy(current.getId());	
		}
		
		return t;
	}
	
	public Map<String, Object> getModifiedMap() {
		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Map<String, Object> modifiedMap = new HashMap<String, Object>();
		modifiedMap.put("tracking.modified", new Date());
		modifiedMap.put("tracking.modifiedBy", current.getId());
		
		return modifiedMap;
	}
	
	/**
	 * @param trackedRepo
	 * @return User type
	 */
	public User getCreator(ITracking trackeRepoy) {
		
		Tracking tracking = trackeRepoy.getTracking();
		
		if(tracking == null) return null;
		
		User createdBy = getUser(tracking.getCreatedBy());
		
		return createdBy;
	}
	
	private User getUser(String id) {
		return this.userRepoImpl.findOneByObjectId(id);
	}
}
