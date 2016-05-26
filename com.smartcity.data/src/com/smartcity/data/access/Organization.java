/**
 * 
 */
package com.smartcity.data.access;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartcity.data.common.Address;
import com.smartcity.data.common.Contact;
import com.smartcity.data.geo.Location;
import com.smartcity.data.tagging.Tag;

/**
 * @author gperreas
 *
 */
@Document
public class Organization
{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String name;
	private String description;
	
	@Indexed(unique=true, sparse=true)
	private String url;
	
	private Address address;
	
	@DBRef
	private Set<Contact> contacts = new HashSet<Contact>();
	
	@DBRef
	private Set<Location> favoriteLocations = new HashSet<Location>();
	
	@DBRef
	private Set<Tag> favoriteTags = new HashSet<Tag>();

	@JsonIgnore
	@DBRef(lazy=true)
	private Set<User> users = new HashSet<User>();
	
	public Organization() {}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the users
	 */
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public void addUser(User user) {
		this.users.add(user);
	}
	
	public void removeUser(User user) {
		this.users.remove(user);
	}

	/**
	 * @return the contacts
	 */
	public Set<Contact> getContacts() {
		return contacts;
	}

	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}
	
	public void addContact(Contact contact) {
		this.contacts.add(contact);
	}
	
	public void removeContact(Contact contact) {
		this.contacts.remove(contact);
	}

	/**
	 * @return the favoriteLocations
	 */
	public Set<Location> getFavoriteLocations() {
		return favoriteLocations;
	}

	/**
	 * @param favoriteLocations the favoriteLocations to set
	 */
	public void setFavoriteLocations(Set<Location> favoriteLocations) {
		this.favoriteLocations = favoriteLocations;
	}
	
	public void addFavoriteLocation(Location location) {
		this.favoriteLocations.add(location);
	}
	
	public void removeFavoriteLocation(Location location) {
		this.favoriteLocations.remove(location);
	}
	

	/**
	 * @return the favoriteTags
	 */
	public Set<Tag> getFavoriteTags() {
		return favoriteTags;
	}

	/**
	 * @param favoriteTags the favoriteTags to set
	 */
	public void setFavoriteTags(Set<Tag> favoriteTags) {
		this.favoriteTags = favoriteTags;
	}
	
	public void addFavoriteTag(Tag tag) {
		this.favoriteTags.add(tag);
	}
	
	public void removeFavoriteTag(Tag tag) {
		this.favoriteTags.remove(tag);
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
}
