/**
 * 
 */
package com.smartcity.data;

import com.smartcity.exceptions.SequenceException;

/**
 * @author gperreas
 *
 */
public interface ISequence {

	long getNextSequenceId(String key) throws SequenceException;
}
