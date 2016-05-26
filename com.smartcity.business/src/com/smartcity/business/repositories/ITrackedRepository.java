package com.smartcity.business.repositories;

import com.smartcity.data.tracking.ITracking;

/**
 * @author gperreas
 *
 */
public interface ITrackedRepository<T extends ITracking>	
	extends IRepository<T>
{

}
