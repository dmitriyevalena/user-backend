package com.clearsolutions.userbackend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.clearsolutions.userbackend.exception.UserAlreadyExistsException;
import com.clearsolutions.userbackend.exception.UserNotFoundException;
import com.clearsolutions.userbackend.model.Address;
import com.clearsolutions.userbackend.model.User;
import com.clearsolutions.userbackend.model.dao.AddressDAO;
import com.clearsolutions.userbackend.model.dao.LocalUserDAO;

@Service
public class UserService {

	@Value("${user.age}")
	private String userAge;

	private LocalUserDAO localUserDAO;

	private AddressDAO addressDAO;

	public UserService(LocalUserDAO localUserDAO, AddressDAO addressDAO) {
		this.localUserDAO = localUserDAO;
		this.addressDAO = addressDAO;
	}

    /**
     * Save a user.
     *
     * @param user the entity to save
     * @return the persisted entity
     */
	public User create(User user) throws UserAlreadyExistsException {
		if (localUserDAO.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
		}
		Address persistedAddress = addressDAO.save(user.getAddress());
		user.setAddress(persistedAddress);
		return localUserDAO.save(user);
	}

	/**
	 * Update all user fields.
	 *
	 * @param id          the ID of the entity
	 * @param updatedUser the updated entity
	 * @return the updated entity
	 * @throws UserNotFoundException
	 */
	public User updateUser(Long id, User updatedUser) throws UserNotFoundException {
		Optional<User> existingUserOp = localUserDAO.findById(id);
		if (existingUserOp.isPresent()) {
			User existingUser = existingUserOp.get();
			existingUser.setEmail(updatedUser.getEmail());
			existingUser.setFirstName(updatedUser.getFirstName());
			existingUser.setLastName(updatedUser.getLastName());
			existingUser.setBirthDate(updatedUser.getBirthDate());
			existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
		
			Address updatedAddress = updatedUser.getAddress();
			Address existingAddress = existingUser.getAddress();
			
			existingAddress.setCountry(updatedAddress.getCountry());
			existingAddress.setCity(updatedAddress.getCity());
			existingAddress.setStreet(updatedAddress.getStreet());
			existingAddress.setZipCode(updatedAddress.getZipCode());
			
			Address persistedAddress = addressDAO.save(existingAddress);
			existingUser.setAddress(persistedAddress);
			return localUserDAO.save(existingUser);
		} else {
			throw new UserNotFoundException("User with id: " + id + " not found.");
		}
	}

	/**
	 * Delete the user by ID.
	 *
	 * @param id the ID of the entity
	 */
	public void deleteById(Long id) throws UserNotFoundException {
		Optional<User> userOp = localUserDAO.findById(id);
		if (!localUserDAO.findById(id).isPresent()) {
			throw new UserNotFoundException("User with id: " + id + " not found.");
		}
		localUserDAO.delete(userOp.get());
	}

	/**
	 * Get all the users.
	 *
	 * @return the list of entities
	 */
	public List<User> findAll() {
		return localUserDAO.findAll();
	}
	
	/**
	 * Get users by birth date range.
	 *
	 * @param fromDate   from which date to search entities
	 * @param toDate  until which date to search entities 
	 * @return the list of entities
	 */
	public List<User> findByBirthDate(LocalDate fromDate, LocalDate toDate) {
		return localUserDAO.findByBirthDateBetween(fromDate, toDate);
	}
	
	/**
	 * Update user address field.
	 *
	 * @param id          the ID of the entity
	 * @param updatedAddress the updated entity
	 * @return the updated entity
	 * @throws UserNotFoundException
	 */
	public Address updateUserAddress(Long id, Address updatedAddress) throws UserNotFoundException {
		Optional<User> existingUserOp = localUserDAO.findById(id);
		if (existingUserOp.isPresent()) {
			User existingUser = existingUserOp.get();
			
			Address existingAddress = existingUser.getAddress();
			
			existingAddress.setCountry(updatedAddress.getCountry());
			existingAddress.setCity(updatedAddress.getCity());
			existingAddress.setStreet(updatedAddress.getStreet());
			existingAddress.setZipCode(updatedAddress.getZipCode());
			
			Address persistedAddress = addressDAO.save(existingAddress);
			return persistedAddress;
//			existingUser.setAddress(persistedAddress);
//			return localUserDAO.save(existingUser);
		} else {
			throw new UserNotFoundException("User with id: " + id + " not found.");
		}
	}

}
