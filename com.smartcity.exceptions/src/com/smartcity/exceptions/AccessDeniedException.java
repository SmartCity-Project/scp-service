/**
 * 
 */
package com.smartcity.exceptions;

/**
 * @author gperreas
 *
 */
public class AccessDeniedException 
	extends ApplicationException
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param repository
	 */
	public AccessDeniedException(Object repository) {
		super("Access denied for " + repository.getClass().getSimpleName() + " repository");
	}
	
	public AccessDeniedException(String message) {
		super("Access denied for " + message);
	}

}
