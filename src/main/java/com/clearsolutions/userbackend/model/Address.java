package com.clearsolutions.userbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Address of user.
 */
@Entity
@Table(name = "addresses")
public class Address {

	/** Unique id for the address. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	/** The country of the address. */
	@Column(nullable = false)
	private String country;

	/** The city of the address. */
	@Column(nullable = false)
	private String city;

	/** The zip code of the address. */
	@Column(nullable = false)
	private String zipCode;

	/** The street of the address. */
	@Column(nullable = false)
	private String street;

	@JsonIgnore
	@OneToOne(mappedBy = "address")
	private User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
