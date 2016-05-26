/**
 * 
 */
package com.smartcity.business.rest;

import java.util.Map;

import com.smartcity.data.common.ReportDocument;

/**
 * @author gperreas
 *
 */
public interface IReportDocumentRestController 
{
	Map<String, Object> report(String documentType, ReportDocument report);
	
	Map<String, Object> getReports(String documentType, String documentId);

	Map<String, Object> getAggregatedReports(String documentType, String documentId);

	Map<String, Object> getAggregatedReportsList(String documentType);
}
