/**
 * 
 */
package com.smartcity.business.repositories;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author gperreas
 *
 */
public interface IMongoTemplate {

	void setMongoTemplate(MongoTemplate mongoTemplate);

}
