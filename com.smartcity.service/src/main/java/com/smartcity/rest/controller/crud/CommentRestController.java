/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcity.business.controllers.impl.CommentController;
import com.smartcity.business.repositories.impl.CommentRepoImpl;
import com.smartcity.business.repositories.impl.UserRepoImpl;
import com.smartcity.business.rest.ICommentRestController;
import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.access.Organization;
import com.smartcity.data.access.User;
import com.smartcity.data.comments.AggregatedComments;
import com.smartcity.data.comments.Comment;
import com.smartcity.data.common.Author;
import com.smartcity.data.common.Event;
import com.smartcity.data.common.Petition;
import com.smartcity.data.voting.AggregatedVotes;

/**
 * @author gperreas
 *
 */
@RestController
@RequestMapping(value="/api/crud/{documentType}/comments")
public class CommentRestController 
	extends GenericTypeRestController
	implements ICommentRestController
{
	private Logger log = LoggerFactory.getLogger(CommentRestController.class);
	
	@Autowired
	private UserRepoImpl userRepoImpl;
	
	@Autowired
	private CommentController commentController;

	@Override
	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> save(
			@PathVariable(value = "documentType") String documentType, @RequestBody Comment json) {
		
		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		if(documentTypeClass!=null) {
			
			if(json.getDocumentId()==null) {
				getResponseMap().put("error", "you must provide reference document id for te comment");
				return getResponseMap();
			}
			
			Comment comment = json;
			
			User currentUser = getCurrentUser();
			
			Author author = new Author();
			
			if(currentUser.getOrganization()!=null) {
				Organization org = currentUser.getOrganization();
				author.setId(currentUser.getId());
				author.setName(currentUser.getUsername());
				author.setOrganizationAuthor(true);
			} else {
				author.setId(currentUser.getId());
				author.setName(currentUser.getUsername());
			}
			
			comment.setAuthor(author);
			comment.setPosted(new Date());
			comment.setClassRef(documentTypeClass.getName());
			commentController.saveComment(comment);
			
			getResponseMap().put("comment", comment);
			getResponseMap().put("aggregatedComments", 
					commentController.getAggregatedComments(comment));
		}
		
		return getResponseMap();
	}

	@Override
	public Map<String, Object> edit(
			@PathVariable(value = "documentType") String documentType, @RequestBody Comment json) {

		return null;
	}

	@Override
	@RequestMapping(value="/{id}", 
			method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
	public boolean delete(@PathVariable(value = "documentType") String documentType, 
			@PathVariable(value="id") Long id) {
		
		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		if(documentTypeClass!=null) {
			return commentController.deleteComment(id);
		} else {
			return false;
		}
		
	}
	
	@Override
	@RequestMapping(value="/find", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getComments(@PathVariable(value = "documentType") String documentType, 
			@RequestParam String discussionId) {

		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		if(documentTypeClass!=null) {
			
			BaseDocumentDetails baseDocumentDetails = new BaseDocumentDetails();
			baseDocumentDetails.setClassRef(documentTypeClass.getCanonicalName());
			baseDocumentDetails.setDocumentId(discussionId);
			
			List<Comment> comments = commentController.findListCommentByBaseDocumentDetails(baseDocumentDetails);
			AggregatedComments ac = commentController.getAggregatedComments(baseDocumentDetails);
			if(comments!=null) {
				for(int i=0; i<comments.size(); i++) {
					User u = userRepoImpl.findOneByObjectId(comments.get(i).getAuthor().getId());
					
					if(u.getOrganization()!=null&&comments.get(i).getAuthor().isOrganizationAuthor()) {
						Organization org = u.getOrganization();
						comments.get(i).getAuthor().setId(org.getId());
						comments.get(i).getAuthor().setName(org.getName());
					} else {
						comments.get(i).getAuthor().setName(u.getName()+" "+u.getSurname());		
					}
				}
				getResponseMap().put("comments", comments);
				getResponseMap().put("aggregatedComments", ac);
			}
		}
		
		return getResponseMap();
	}
	
	@Override
	@RequestMapping(value="/getAggregatedComments", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getAggregatedComments(@PathVariable(value = "documentType") String documentType, 
			@RequestParam String documentId) {
		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		
		if(documentTypeClass!=null) {
			BaseDocumentDetails baseDocumentDetails = new BaseDocumentDetails();
			baseDocumentDetails.setClassRef(documentTypeClass.getCanonicalName());
			baseDocumentDetails.setDocumentId(documentId);
			
			AggregatedComments ac = 
					commentController.getAggregatedComments(baseDocumentDetails);
		}
		
		return getResponseMap();
	}
	
	@Override
	@RequestMapping(value="/getAggregatedCommentsList", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getAggregatedCommentsList(@PathVariable(value = "documentType") String documentType) {
		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		if(documentTypeClass!=null) {
		
			BaseDocumentDetails baseDocumentDetails = new BaseDocumentDetails();
			baseDocumentDetails.setClassRef(documentTypeClass.getCanonicalName());
			
			List<AggregatedComments> avList = commentController.getAggregatedCommentsList(baseDocumentDetails);
		}

		return getResponseMap();
	}
	
	protected Map<String,Class> initDocumentTypeMap() {
		Map<String, Class> documentTypeMap = new HashMap<String, Class>();

		documentTypeMap.put("events", Event.class);
		documentTypeMap.put("petitions", Petition.class);
		
		return documentTypeMap;
	}
}
