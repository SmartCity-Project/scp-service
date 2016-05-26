/**
 * 
 */
package com.smartcity.data;

import java.io.Serializable;

/**
 * @author gperreas
 *
 */
public abstract class Pair<K extends Serializable,V extends Serializable> 
	implements Serializable
{

	private static final long serialVersionUID = 1L;

	protected K key;
	
	protected V value;

	/**
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(K key) {
		this.key = key;
	}

	/**
	 * @return 
	 * @return the value
	 */
	public V getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(V value) {
		this.value = value;
	}
	
	
}
