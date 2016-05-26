/**
 * 
 */
package com.smartcity.data.voting;

import java.io.Serializable;

import com.smartcity.data.IDocument;

/**
 * @author gperreas
 * @param <ID>
 *
 */
public interface IVotable<ID extends Serializable>
	extends IDocument<ID>
{

}
