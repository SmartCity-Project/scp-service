/**
 * 
 */
package com.smartcity.business.rest;

import java.io.Serializable;
import java.util.Map;

/**
 * @author gperreas
 *
 */
public interface IBaseCrudRestController<T, ID extends Serializable> 
{
	Map<String, Object> save(T json);
	Map<String, Object> edit(T json);
	boolean delete(ID id);
}
