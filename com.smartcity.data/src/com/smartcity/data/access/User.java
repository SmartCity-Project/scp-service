package com.smartcity.data.access;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartcity.data.mobile.MobileInfo;

/**
 * @author gperreas
 *
 */
@Document
public class User
	implements UserDetails, CredentialsContainer, Serializable
{

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
    private String username;
    
    private String name;
    
    private String surname;
	
	@JsonIgnore
    private String password;
    
    @Indexed(unique=true)
    private String email;
    
    @JsonIgnore
    private boolean accountNonExpired;
    
    @JsonIgnore
    private boolean accountNonLocked;
    
    @JsonIgnore
    private boolean credentialsNonExpired;
    
    @JsonIgnore
    private boolean enabled;
    
    @JsonIgnore
	private String salt;
	
    @JsonIgnore
	private long expires;
    
    @JsonIgnore
	@DBRef(lazy=true)
	private Set<Role> roles = new HashSet<Role>();

    @DBRef(lazy=true)
	private Organization organization;
	
	private Set<MobileInfo> mobileInfoSet = new HashSet<MobileInfo>();
	
	public User() {}


	/**
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public void eraseCredentials() {
		
	}

	/**
	 * @return the user id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param user id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public String getPassword() {

		return this.password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
	
	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}
	
	/**
	 * @param accountNonExpired the accountNonExpired to set
	 */
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}
	
	/**
	 * @param accountNonLocked the accountNonLocked to set
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}
	
	/**
	 * @param credentialsNonExpired the credentialsNonExpired to set
	 */
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}
	
	public void removeRole(Role role) {
		this.roles.remove(role);
	}

	/**
	 * @return the expires
	 */
	public long getExpires() {
		return expires;
	}

	/**
	 * @param expires the expires to set
	 */
	public void setExpires(long expires) {
		this.expires = expires;
	}


	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}


	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}


	/**
	 * @return the mobileInfoSet
	 */
	public Set<MobileInfo> getMobileInfoSet() {
		return mobileInfoSet;
	}


	/**
	 * @param mobileInfoSet the mobileInfoSet to set
	 */
	public void setMobileInfoSet(Set<MobileInfo> mobileInfoSet) {
		this.mobileInfoSet = mobileInfoSet;
	}
	
	public void addMobileInfo(MobileInfo mobileInfo) {
		this.mobileInfoSet.add(mobileInfo);
	}

	public void removeMobileInfo(MobileInfo mobileInfo) {
		this.mobileInfoSet.remove(mobileInfo);
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}


	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}


	/**
	 * @return the organization
	 */
	public Organization getOrganization() {
		return organization;
	}


	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
