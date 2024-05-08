package com.clearsolutions.userbackend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.clearsolutions.userbackend.exception.UserAlreadyExistsException;
import com.clearsolutions.userbackend.exception.UserNotFoundException;
import com.clearsolutions.userbackend.model.Address;
import com.clearsolutions.userbackend.model.LocalUser;
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

	public LocalUser create(LocalUser user) throws UserAlreadyExistsException {
		if (localUserDAO.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
		}

		Address persistedAddress = addressDAO.save(user.getAddress());

		user.setAddress(persistedAddress);
		return localUserDAO.save(user);
	}

	public void deleteById(Long id) throws UserNotFoundException {
		Optional<LocalUser> userOp = localUserDAO.findById(id);
		if (!localUserDAO.findById(id).isPresent()) {
			throw new UserNotFoundException("User with id: " + id + " not found.");
		}
		localUserDAO.delete(userOp.get());
	}

	public List<LocalUser> findAll() {
		return localUserDAO.findAll();
	}

	public List<LocalUser> findByBirthDate(LocalDate fromDate, LocalDate toDate) {
		return localUserDAO.findByBirthDateBetween(fromDate, toDate);
	}

}
