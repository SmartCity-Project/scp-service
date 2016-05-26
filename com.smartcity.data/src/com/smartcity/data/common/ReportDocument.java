/**
 * 
 */
package com.smartcity.data.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.reporting.ReportStatus;
import com.smartcity.data.tracking.ITracking;
import com.smartcity.data.tracking.Tracking;

/**
 * @author gperreas
 *
 */
@Document
public class ReportDocument 
	extends BaseDocumentDetails
	implements ITracking<String>
{
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	
	private ReportStatus status;
	
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
	 * @return the status
	 */
	public ReportStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ReportStatus status) {
		this.status = status;
	}
	
	private Tracking tracking;

	@Override
	public Tracking getTracking() {
		return tracking;
	}

	@Override
	public void setTracking(Tracking tracking) {
		this.tracking = tracking;
	}
}
