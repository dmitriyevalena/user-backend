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
		return save(user);
	}

	/**
	 * Save a user.
	 *
	 * @param user the entity to save
	 * @return the persisted entity
	 */
	public User save(User user) {
		Address address = user.getAddress();
		if (address != null) {
			Address persistedAddress = addressDAO.save(address);
			user.setAddress(persistedAddress);
		}
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

		User existingUser = localUserDAO.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setFirstName(updatedUser.getFirstName());
		existingUser.setLastName(updatedUser.getLastName());
		existingUser.setBirthDate(updatedUser.getBirthDate());
		existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

		Address updatedAddress = updatedUser.getAddress();
		Address existingAddress = existingUser.getAddress();
		if (updatedAddress != null) {
			if (existingAddress == null) {
				existingAddress = new Address();
				existingUser.setAddress(existingAddress);
			}
			existingAddress.setCountry(updatedAddress.getCountry());
			existingAddress.setCity(updatedAddress.getCity());
			existingAddress.setStreet(updatedAddress.getStreet());
			existingAddress.setZipCode(updatedAddress.getZipCode());
		} else if (existingAddress != null) {
			existingAddress = null;
		}

		return save(existingUser);
	}

	/**
	 * Delete the user by ID.
	 *
	 * @param id the ID of the entity
	 */
	public void deleteById(Long id) throws UserNotFoundException {
		User user = localUserDAO.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		localUserDAO.delete(user);
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
	 * Get user by ID.
	 * 
	 * @param id
	 * @return optional entity
	 */
	public Optional<User> findById(Long id) {
		return localUserDAO.findById(id);
	}

	/**
	 * Get users by birth date range.
	 *
	 * @param fromDate from which date to search entities
	 * @param toDate   until which date to search entities
	 * @return the list of entities
	 */
	public List<User> findByBirthDate(LocalDate fromDate, LocalDate toDate) {
		return localUserDAO.findByBirthDateBetween(fromDate, toDate);
	}

}
