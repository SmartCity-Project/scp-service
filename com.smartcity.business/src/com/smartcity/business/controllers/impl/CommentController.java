/**
 * 
 */
package com.smartcity.business.controllers.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.smartcity.business.repositories.impl.AggregatedCommentsRepoImpl;
import com.smartcity.business.repositories.impl.CommentRepoImpl;
import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.comments.AggregatedComments;
import com.smartcity.data.comments.Comment;
import com.smartcity.data.voting.AggregatedVotes;
import com.smartcity.data.voting.Vote;

/**
 * @author gperreas
 *
 */
@Controller
public class CommentController {

	@Autowired
	private CommentRepoImpl commentRepoImpl;
	
	@Autowired
	private AggregatedCommentsRepoImpl aggregatedCommentsRepoImpl;
	
	public CommentController() {}
	
	public Comment saveComment(Comment comment) {

		AggregatedComments exac = aggregatedCommentsRepoImpl.findAggregatedCommentsByBaseDocumentDetails(comment);
		if(exac==null) {
			exac = new AggregatedComments();
			exac.setClassRef(comment.getClassRef());
			exac.setDocumentId(comment.getDocumentId());
			initAggregatedCommentDocument(exac);
		}
		
		calculateComments(comment, false);
		
		return commentRepoImpl.save(comment);
	}
	
	public void editComment(Comment comment) {
		this.commentRepoImpl.save(comment);
	}
	
	public boolean deleteComment(Long id) {
		Comment excomment = commentRepoImpl.findOneByObjectId(id);
		calculateComments(excomment, true);
		
		return this.commentRepoImpl.delete(id);
	}
	
	public List<Comment> findListCommentByBaseDocumentDetails(BaseDocumentDetails baseDocumentDetails) {
		return this.commentRepoImpl.findListCommentByBaseDocumentDetails(baseDocumentDetails);
	}
	
	public void initAggregatedCommentDocument(AggregatedComments aggregatedComments) {
		this.aggregatedCommentsRepoImpl.save(aggregatedComments);
	}
	
	public List<AggregatedComments> getAggregatedCommentsList(BaseDocumentDetails baseDocumentDetails) {
		return this.aggregatedCommentsRepoImpl.findAggregatedCommentsListByBaseDocumentDetails(baseDocumentDetails);
	}
	
	public AggregatedComments getAggregatedComments(BaseDocumentDetails baseDocumentDetails) {
		return this.aggregatedCommentsRepoImpl.findAggregatedCommentsByBaseDocumentDetails(baseDocumentDetails);
	}
	
	private AggregatedComments calculateComments(Comment c, boolean hasCommentDeleted) {
		return this.aggregatedCommentsRepoImpl.updateAggregatedComments(c, hasCommentDeleted);
	}
}
