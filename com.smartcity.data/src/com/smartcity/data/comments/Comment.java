/**
 * 
 */
package com.smartcity.data.comments;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.ISequenceId;
import com.smartcity.data.common.Author;

/**
 * @author gperreas
 *
 */
@Document
@CompoundIndexes(value = { 
		@CompoundIndex(name = "disId_posted_idx", def = "{'discussionId' : 1, 'posted' : 1}"),
		@CompoundIndex(name = "cid_docId_classRef_idx", def = "{'id' : 1, 'documentId' : 1, 'classRef': 1}")
})
public class Comment 
	extends BaseDocumentDetails
	implements ISequenceId<Long>, Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	private String data;
		
	private Date posted;
	
	private Author author;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the posted
	 */
	public Date getPosted() {
		return posted;
	}

	/**
	 * @param posted the posted to set
	 */
	public void setPosted(Date posted) {
		this.posted = posted;
	}

	/**
	 * @return the author
	 */
	public Author getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}
	

}
