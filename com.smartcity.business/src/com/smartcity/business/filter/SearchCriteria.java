/**
 * 
 */
package com.smartcity.business.filter;

import java.io.Serializable;

/**
 * @author gperreas
 *
 */
public class SearchCriteria 
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	private int pageIndex;

	private int pageSize;

	private boolean retrievingTotalCount;

	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the retrievingTotalCount
	 */
	public boolean isRetrievingTotalCount() {
		return retrievingTotalCount;
	}

	/**
	 * @param retrievingTotalCount the retrievingTotalCount to set
	 */
	public void setRetrievingTotalCount(boolean retrievingTotalCount) {
		this.retrievingTotalCount = retrievingTotalCount;
	}
	
}
