package com.clearsolutions.userbackend.api.model;

import java.time.LocalDate;

import com.clearsolutions.userbackend.model.LocalUser;

import jakarta.validation.constraints.*;

/**
 * The information required to create a user.
 */
public class CreateUserDto {

	/** The email. */
    @NotBlank(message = "Email is required.")
    @Email(message = "The email address is invalid.")    
    private String email;
    
    /** The first name. */
    @NotBlank(message = "First is required.")
    private String firstName;
    
    /** The last name. */
    @NotBlank(message = "Last is required.")
    private String lastName;
    
    /** The birth date. */
    @NotNull(message = "The date of birth is required.")
    @Past(message = "The date of birth must be in the past.")
    private LocalDate birthDate;
      
    /** The address. */
    private String address;
    
    /** The phone number. */
    private String phoneNumber;
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
    
	public LocalUser toLocalUser() {
		LocalUser user = new LocalUser();
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setBirthDate(birthDate);
		user.setAddress(address);
		user.setPhoneNumber(phoneNumber);
		return user;
	}
    
}
