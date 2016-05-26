/**
 * 
 */
package com.smartcity.business.controllers.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.mongodb.DuplicateKeyException;
import com.smartcity.business.repositories.impl.AggregatedReportsRepoImpl;
import com.smartcity.business.repositories.impl.ReportDocumentRepoImpl;
import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.common.ReportDocument;
import com.smartcity.data.petition.AggregatedSignatures;
import com.smartcity.data.petition.Signature;
import com.smartcity.data.reporting.AggregatedReports;


/**
 * @author gperreas
 *
 */
@Component
public class ReportDocumentController {
	
	@Autowired
	private ReportDocumentRepoImpl reportDocumentRepoImpl;
	
	@Autowired
	private AggregatedReportsRepoImpl aggregatedReportsRepoImpl;
	
	
	public AggregatedReports report(ReportDocument reportDocument) {
		Assert.notNull(reportDocument);
		Assert.notNull(reportDocument.getDocumentId());
		Assert.notNull(reportDocument.getStatus());
		
		ReportDocument report = reportDocumentRepoImpl.save(reportDocument);
		AggregatedReports aggregatedReports = updateAggregatedReports(reportDocument);

//		ReportDocument existReport = reportDocumentRepoImpl.findExistReportDocument(reportDocument);
//		AggregatedReports aggregatedReports = null;
//		
//		if(existReport!=null) {
//
//			ReportDocument report = reportDocumentRepoImpl.save(reportDocument);
//			
//			aggregatedReports = aggregatedReportsRepoImpl.updateAggregatedReports(reportDocument);
//		} else {
//			
//		}
		
		return aggregatedReports;
	}
	
	public List<ReportDocument> findReportsByBaseDocumentDetails(ReportDocument report) {
		return reportDocumentRepoImpl.findReportsByBaseDocumentDetails(report);
	}
	
	public ReportDocument findReportByBaseDocumentDetailsAndUserId(BaseDocumentDetails baseDocumentDetails, String userId) {
		Assert.notNull(baseDocumentDetails);
		Assert.notNull(userId);
		
		return reportDocumentRepoImpl.findReportByBaseDocumentDetailsAndUserId(baseDocumentDetails, userId);
	}
	
	public AggregatedReports findAggregatedReportsByBaseDocumentDetails(BaseDocumentDetails baseDocumentDetails) {
		return this.aggregatedReportsRepoImpl.findAggregatedReportsByBaseDocumentDetails(baseDocumentDetails);
	}
	
	public List<AggregatedReports> findAggregatedReportsListByBaseDocumentDetails(BaseDocumentDetails baseDocumentDetails) {
		return this.aggregatedReportsRepoImpl.findAggregatedReportsListByBaseDocumentDetails(baseDocumentDetails);
	}

	public void initAggregatedReports(AggregatedReports aggregatedReports) {
		this.aggregatedReportsRepoImpl.save(aggregatedReports);
	}
	
	private AggregatedReports updateAggregatedReports(ReportDocument reportDocument) {
		return this.aggregatedReportsRepoImpl.updateAggregatedReports(reportDocument);
	}
	
}
