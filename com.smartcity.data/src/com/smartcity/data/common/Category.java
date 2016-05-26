package com.smartcity.data.common;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.smartcity.data.IDocument;
import com.smartcity.data.ISequenceId;

/**
 * @author gperreas
 *
 */
@Document
public class Category
	implements ISequenceId<Long>, Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	private Integer parentId;
	
	private String name;
	
	@DBRef
	private Set<Category> children = new HashSet<Category>();

	public Category() {}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}
	
	public void addChild(Category category) {
		this.children.add(category);
	}
	
	public void removeChild(Category category) {
		this.children.remove(category);
	}
}
