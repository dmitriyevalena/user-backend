package com.clearsolutions.userbackend.api.model;

import com.clearsolutions.userbackend.model.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.Flag;

/**
 * The information required to create the address
 */
public class AddressDto {

	/** The country. */
	@NotBlank(message = "The country is required.")
	private String country;

	/** The city. */
	@NotBlank(message = "The city is required.")
	private String city;

	/** The zipCode. */
	@NotBlank(message = "The Zip code is required.")
	@Pattern(regexp = "^\\d{1,5}$", flags = { Flag.CASE_INSENSITIVE,
			Flag.MULTILINE }, message = "The Zip code is invalid.")
	private String zipCode;

	/** The street. */
	@NotBlank(message = "The street name is required.")
	private String street;

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

	public Address toAddress() {
		Address address = new Address();
		address.setCountry(country);
		address.setCity(city);
		address.setZipCode(zipCode);
		address.setStreet(street);
		return address;				
	}
}
