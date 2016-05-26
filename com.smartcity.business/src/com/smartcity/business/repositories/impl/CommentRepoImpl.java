/**
 * 
 */
package com.smartcity.business.repositories.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;
import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.comments.Comment;
import com.smartcity.data.voting.Vote;

/**
 * @author gperreas
 *
 */
public class CommentRepoImpl 
	extends BaseRepositoryImpl<Comment>//BaseRbacRepositoryImpl<Comment>
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Comment> getModelType() {
		return Comment.class;
	}
	
	public List<Comment> findListCommentByBaseDocumentDetails(BaseDocumentDetails baseDocumentDetails) {

		Map<String, Object> keyValueMap = new HashMap<String, Object>();
		keyValueMap.put("documentId", baseDocumentDetails.getDocumentId());
		keyValueMap.put("classRef", baseDocumentDetails.getClassRef());
				
		return super.findSortedListByMapKeyValue(keyValueMap, new Sort(Sort.Direction.ASC,"posted"));
	}
	
	public Comment updateComment(Comment comment) {
		
		return null;
	}
}
