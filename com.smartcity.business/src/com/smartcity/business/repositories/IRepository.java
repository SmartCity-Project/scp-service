package com.smartcity.business.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;
import com.smartcity.business.filter.SearchCriteria;
import com.smartcity.business.filter.SearchResult;
import com.smartcity.data.ISequence;

/**
 * @author gperreas
 *
 */
public interface IRepository<T>
	extends IBaseRepository<T>, ISequence, Serializable
{	

	List<T> findAll();

	T findOneByObjectId(Object id);
	
	T findOneByKeyValue(String key, Object value);
	
	T findOneByMapKeyValue(Map<String, Object> keyValueMap);
	
	T findOneByMapKeyValue(Criteria criteria);
	
	List<T> findListByKeyValue(String key, Object value);
	
	List<T> findSortedListByMapKeyValue(Map<String, Object> keyValueMap, Sort sort);
	
	List<T> findSortedListByKeyValue(String key, Object value, Sort sort);
	
	List<T> findListByMapKeyValue(Map<String, Object> keyValueMap);
	
	List<T> findListByMapKeyValue(Criteria criteria);
	
	SearchResult<T> findPageByMapKeyValue(Set<Criteria> criteriaSet, Sort sort, SearchCriteria criteria);

	boolean deleteByKeyValue(String key, Object value);

}
