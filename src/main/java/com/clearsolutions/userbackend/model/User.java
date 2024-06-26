package com.clearsolutions.userbackend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * User.
 */
@Entity
@Table(name = "local_user")
public class User {

	/** Unique id for the user. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;

	/** The email of the user. */
	@Column(name = "email", nullable = false, unique = true, length = 320)
	private String email;

	/** The first name of the user. */
	@Column(name = "first_name", nullable = false)
	private String firstName;

	/** The last name of the user. */
	@Column(name = "last_name", nullable = false)
	private String lastName;

	/** The birth date of the user. */
	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;

	/** The address of the user. */
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "address_id", referencedColumnName = "id", nullable = true)
	private Address address;

	/** The phone number of the user. */
	@Column(name = "phone_number", nullable = true)
	private String phoneNumber;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
