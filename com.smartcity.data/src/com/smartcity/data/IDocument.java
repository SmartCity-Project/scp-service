/**
 * 
 */
package com.smartcity.data;

import java.io.Serializable;

/**
 * @author gperreas
 *
 */
public interface IDocument<ID extends Serializable> {

	ID getId();
	
	void setId(ID id);
}
