/**
 * 
 */
package com.smartcity.business.repositories;

/**
 * @author gperreas
 *
 */
public interface IBaseRepository<T>
	extends IMongoTemplate
{
	T save(T object);
	
	T update(T object);
	
	boolean delete(Object id);

	void createCollection();

	void dropCollection();
}
