package com.smartcity.data.tracking;

import java.io.Serializable;

import com.smartcity.data.IDocument;

/**
 * @author gperreas
 * @param <ID>
 *
 */
public interface ITracking<ID extends Serializable>
	extends IDocument<ID>
{
	//ID getId();
	
	//void setId(ID id);
	
	Tracking getTracking();
	
	void setTracking(Tracking tracking);
}
