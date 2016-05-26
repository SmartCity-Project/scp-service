/**
 * 
 */
package com.smartcity.data.petition;

import java.util.Date;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gperreas
 *
 */
@Document
@CompoundIndex(name = "pid_email_idx", def = "{'petitionId' : 1, 'email' : 1}")
public class Signature {
	
	private Long petitionId;
	
	private String email;
	
	private String ipAddress;
	
	private Date signed;
	
	private boolean canceled;

	/**
	 * @return the petitionId
	 */
	public Long getPetitionId() {
		return petitionId;
	}

	/**
	 * @param petitionId the petitionId to set
	 */
	public void setPetitionId(Long petitionId) {
		this.petitionId = petitionId;
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
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the signed
	 */
	public Date getSigned() {
		return signed;
	}

	/**
	 * @param signed the signed to set
	 */
	public void setSigned(Date signed) {
		this.signed = signed;
	}

	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * @param cancel the cancel to set
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
}
