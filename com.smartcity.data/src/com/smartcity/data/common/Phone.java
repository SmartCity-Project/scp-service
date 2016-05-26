/**
 * 
 */
package com.smartcity.data.common;

/**
 * @author gperreas
 *
 */
public class Phone {

	//phone locale code
	private String locale;
	private String number;
	private PhoneTypeEnum type;
	
	public Phone() {}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the type
	 */
	public PhoneTypeEnum getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(PhoneTypeEnum type) {
		this.type = type;
	}
	
}
