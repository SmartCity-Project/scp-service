/**
 * 
 */
package com.smartcity.data.reporting;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Set;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.smartcity.data.BaseDocumentDetails;

/**
 * @author gperreas
 *
 */
@Document
@CompoundIndex(name = "docId_classRef_idx", def = "{'documentId' : 1, 'classRef': 1}")
public class AggregatedReports
	extends BaseDocumentDetails
{
	private static final long serialVersionUID = 1L;
	
	private EnumMap<ReportStatus, Long> totalPairMap = new EnumMap<ReportStatus, Long>(ReportStatus.class);

	/**
	 * @return the totalPairMap
	 */
	public EnumMap<ReportStatus, Long> getTotalPairMap() {
		return totalPairMap;
	}

	/**
	 * @param totalPairMap the totalPairMap to set
	 */
	public void setTotalPairMap(EnumMap<ReportStatus, Long> totalPairMap) {
		this.totalPairMap = totalPairMap;
	} 


}
