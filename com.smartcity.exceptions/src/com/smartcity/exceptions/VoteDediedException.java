/**
 * 
 */
package com.smartcity.exceptions;

/**
 * @author gperreas
 *
 */
public class VoteDediedException 
	extends ApplicationException
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param message
	 */
	public VoteDediedException(String message) {
		super(message);
	}

}
