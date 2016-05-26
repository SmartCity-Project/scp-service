/**
 * 
 */
package com.smartcity.business.repositories.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.common.ReportDocument;
import com.smartcity.data.reporting.AggregatedReports;
import com.smartcity.data.voting.AggregatedVotes;

/**
 * @author gperreas
 *
 */
public class AggregatedReportsRepoImpl 
	extends BaseRepositoryImpl<AggregatedReports>
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<AggregatedReports> getModelType() {
		return AggregatedReports.class;
	}

	public AggregatedReports findAggregatedReportsByBaseDocumentDetails(BaseDocumentDetails baseDocumentDetails) {

		Map<String, Object> keyValueMap = new HashMap<String, Object>();
		keyValueMap.put("documentId", baseDocumentDetails.getDocumentId());
		keyValueMap.put("classRef", baseDocumentDetails.getClassRef());
		
		return super.findOneByMapKeyValue(keyValueMap);
	}

	public List<AggregatedReports> findAggregatedReportsListByBaseDocumentDetails(
			BaseDocumentDetails baseDocumentDetails) {

		Map<String, Object> keyValueMap = new HashMap<String, Object>();
		keyValueMap.put("classRef", baseDocumentDetails.getClassRef());
		
		return super.findListByMapKeyValue(keyValueMap);
	}
	
	/**
	 * @param baseDocumentDetails
	 * @return
	 */
	public AggregatedReports updateAggregatedReports(ReportDocument report) {
		Query query = new Query();
		
		Update update = new Update();
		//update.push("totalPairMap."+report.getStatus().name(), +1);
		update.inc("totalPairMap."+report.getStatus().name(), +1);
		
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.returnNew(true);
		findAndModifyOptions.upsert(true);

		AggregatedReports av = this.mt.findAndModify(
				query.addCriteria(
						where("documentId").is(report.getDocumentId())
						.andOperator(where("classRef").is(report.getClassRef()))), 
				update, 
				findAndModifyOptions,
				getModelType());
		
		return av;
	}

	
}
