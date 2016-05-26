/**
 * 
 */
package com.smartcity.data.mobile;

/**
 * @author gperreas
 *
 */
public class MobileInfo {
	
	private String applicationId;
	
	private MobileTypeEnum type;
	
	public MobileInfo(String applicationId, MobileTypeEnum type) {
		this.applicationId = applicationId;
		this.type= type;
	}
	
	public MobileInfo() {}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the type
	 */
	public MobileTypeEnum getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MobileTypeEnum type) {
		this.type = type;
	}	
}
