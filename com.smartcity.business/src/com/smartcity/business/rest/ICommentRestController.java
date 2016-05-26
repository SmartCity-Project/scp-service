/**
 * 
 */
package com.smartcity.business.rest;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcity.data.comments.Comment;

/**
 * @author gperreas
 *
 */
public interface ICommentRestController 
{
	Map<String, Object> save(String documentType, Comment json);
	Map<String, Object> edit(String documentType, Comment json);
	boolean delete(String documentType, Long id);
	
	Map<String, Object> getComments(String documentType, String discussionId);
	Map<String, Object> getAggregatedCommentsList(String documentType);
	Map<String, Object> getAggregatedComments(String documentType, String documentId);
}
