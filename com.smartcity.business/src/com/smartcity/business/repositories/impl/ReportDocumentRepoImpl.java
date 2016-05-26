/**
 * 
 */
package com.smartcity.business.repositories.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.common.ReportDocument;
import com.smartcity.data.petition.Signature;

/**
 * @author gperreas
 *
 */
public class ReportDocumentRepoImpl 
	extends BaseRbacRepositoryImpl<ReportDocument>
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<ReportDocument> getModelType() {
		return ReportDocument.class;
	}
	
	public List<ReportDocument> findReportsByBaseDocumentDetails(
			BaseDocumentDetails baseDocumentDetails) {
		
		Map<String, Object> keyValueMap = new HashMap<String, Object>();
		keyValueMap.put("documentId", baseDocumentDetails.getDocumentId());
		keyValueMap.put("classRef", baseDocumentDetails.getClassRef());
		
		return super.findListByMapKeyValue(keyValueMap);
	}
	
	
	public ReportDocument findReportByBaseDocumentDetailsAndUserId(
			BaseDocumentDetails baseDocumentDetails, String userId) {
		
		Map<String, Object> keyValueMap = new HashMap<String, Object>();
		keyValueMap.put("documentId", baseDocumentDetails.getDocumentId());
		keyValueMap.put("classRef", baseDocumentDetails.getClassRef());
		keyValueMap.put("tracking.createdBy", userId);

		return super.findOneByMapKeyValue(keyValueMap);
	}
	
	/**
	 * @param report
	 * @return
	 */
	public ReportDocument findExistReportDocument(ReportDocument report) {
		Assert.notNull(report.getTracking());
		
		Map<String, Object> keyValueMap = new HashMap<String, Object>();
		keyValueMap.put("documentId", report.getDocumentId());
		keyValueMap.put("classRef", report.getClassRef());
		keyValueMap.put("tracking.createdBy", report.getTracking().getCreatedBy());

		return super.findOneByMapKeyValue(keyValueMap);
	}

}
