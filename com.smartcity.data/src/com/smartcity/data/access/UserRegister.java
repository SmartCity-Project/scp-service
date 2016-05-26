/**
 * 
 */
package com.smartcity.data.access;

import com.smartcity.data.mobile.MobileInfo;
import com.smartcity.data.mobile.MobileTypeEnum;

/**
 * @author gperreas
 *
 */
public class UserRegister {

	private String firstName;

	private String lastName;
	
	private String password;
	
	private String email;
	
	private boolean mobileUser;
	
	private MobileInfo mobileInfo;
	
	private OrganizationRegister organization;

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * @return the mobileUser
	 */
	public boolean isMobileUser() {
		return mobileUser;
	}

	/**
	 * @param mobileUser the mobileUser to set
	 */
	public void setMobileUser(boolean mobileUser) {
		this.mobileUser = mobileUser;
	}

	/**
	 * @return the mobileInfo
	 */
	public MobileInfo getMobileInfo() {
		return mobileInfo;
	}

	/**
	 * @param mobileInfo the mobileInfo to set
	 */
	public void setMobileInfo(MobileInfo mobileInfo) {
		this.mobileInfo = mobileInfo;
	}

	/**
	 * @return the organization
	 */
	public OrganizationRegister getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(OrganizationRegister organization) {
		this.organization = organization;
	}

}
