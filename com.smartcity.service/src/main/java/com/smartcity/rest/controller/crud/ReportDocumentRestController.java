/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcity.business.controllers.impl.EventController;
import com.smartcity.business.controllers.impl.PetitionController;
import com.smartcity.business.controllers.impl.ReportDocumentController;
import com.smartcity.business.repositories.impl.ReportDocumentRepoImpl;
import com.smartcity.business.rest.IReportDocumentRestController;
import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.access.User;
import com.smartcity.data.common.Article;
import com.smartcity.data.common.Event;
import com.smartcity.data.common.Petition;
import com.smartcity.data.common.ReportDocument;

/**
 * @author gperreas
 *
 */
@RestController
@RequestMapping(value="/api/reports/{documentType}")
public class ReportDocumentRestController 
	extends GenericTypeRestController
	implements IReportDocumentRestController
{
	private Logger log = LoggerFactory.getLogger(ReportDocumentRestController.class);

	@Autowired
	private ReportDocumentRepoImpl reportDocumentRepoImpl;
	
	@Autowired
	private ReportDocumentController reportDocumentController;
	
	@Autowired
	private EventController eventController;
	
	@Autowired
	private PetitionController petitionController;

	@Override
	@RequestMapping(method=RequestMethod.POST)
	public Map<String, Object> report(@PathVariable(value = "documentType") String documentType,
			@RequestBody ReportDocument report) {

		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		
		if(documentTypeClass!=null) {
			try {
				Assert.notNull(report.getDocumentId());
				
				if(!report.getDocumentId().isEmpty()) {
					report.setClassRef(documentTypeClass.getCanonicalName());
					
					Article document = findDocument(documentType, report);
					User current = getCurrentUser();
					if(document!=null) {
						ReportDocument exReport = 
								reportDocumentRepoImpl.findReportByBaseDocumentDetailsAndUserId(
										report, current.getId());
						boolean isDocumentCreatedFromCurrentUser = 
								document.getTracking().getCreatedBy().equals(current.getId());
						if(exReport==null&&!isDocumentCreatedFromCurrentUser) {
							reportDocumentController.report(report);
							//reportDocumentRepoImpl.save(report);
							getResponseMap().put("data", report);
						} else if(isDocumentCreatedFromCurrentUser) {
							getResponseMap().put("error", "you cannot report your own document");
						} else if(exReport!=null) {
							getResponseMap().put("error", "you cannot report again");
						}
					} else {
						getResponseMap().put("error", "document not found");
					}
					
				} else {
					getResponseMap().put("error", "id must not be empty");
				}
				
			} catch(IllegalArgumentException e) {
				//e.printStackTrace();
				getResponseMap().put("error", e.getMessage());//"id must not be empty");
			}
		}
		
		return getResponseMap();
	}


	private Article findDocument(String documentType, ReportDocument report) {

		switch (documentType) {
			case "articles":
				return null;	
			case "events":
				return eventController.findEventById(Long.valueOf(report.getDocumentId()));
			case "petitions":
				return petitionController.findPetitionById(Long.valueOf(report.getDocumentId()));
		}
		
		return null;
	}


	@Override
	@RequestMapping(method=RequestMethod.GET)
	public Map<String, Object> getReports(@PathVariable String documentType, 
			@RequestParam String documentId) {

		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		
		if(documentTypeClass!=null) {			
			try {
				Assert.notNull(documentId);

				if(!documentId.isEmpty()) {
					BaseDocumentDetails baseDocumentDetails = new BaseDocumentDetails();
					baseDocumentDetails.setDocumentId(documentId);
					baseDocumentDetails.setClassRef(documentTypeClass.getCanonicalName());
					
					getResponseMap().put("data", 
							reportDocumentRepoImpl.findReportsByBaseDocumentDetails(baseDocumentDetails));
				} else {
					getResponseMap().put("error", "documentId must not be empty");
				}
			} catch(IllegalArgumentException e) {
				getResponseMap().put("error", "documentId must not be empty");
			}
		}
		
		return getResponseMap();
	}


	@Override
	//@RequestMapping(method=RequestMethod.GET)
	public Map<String, Object> getAggregatedReports(String documentType, String documentId) {

		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		if(documentTypeClass!=null) {

		}
		
		return null;
	}


	@Override
	//@RequestMapping(method=RequestMethod.GET)
	public Map<String, Object> getAggregatedReportsList(String documentType) {

		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		if(documentTypeClass!=null) {

		}
		
		return null;
	}
	
	protected Map<String,Class> initDocumentTypeMap() {
		Map<String, Class> documentTypeMap = new HashMap<String, Class>();

		documentTypeMap.put("articles", Article.class);
		documentTypeMap.put("events", Event.class);
		documentTypeMap.put("petitions", Petition.class);
		
		return documentTypeMap;
	}
	
}
