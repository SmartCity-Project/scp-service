/**
 * 
 */
package com.smartcity.exceptions;

/**
 * @author gperreas
 *
 */
public class EntityNotFoundException 
	extends ApplicationException
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param entity
	 */
	public EntityNotFoundException(String entity) {
		super("could not find entity with id " + entity);
	}
	
	/**
	 * @param entity
	 */
	public EntityNotFoundException() {
		super("could not find entity");
	}
}
