package com.clearsolutions.userbackend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.clearsolutions.userbackend.model.LocalUser;
import com.clearsolutions.userbackend.model.dao.LocalUserDAO;
import com.clearsolutions.userbackend.exception.FromDateIsNotBeforeToDate;

@Service
public class UserService {

	private LocalUserDAO localUserDAO;

	public UserService(LocalUserDAO localUserDAO) {
		this.localUserDAO = localUserDAO;
	}

	public List<LocalUser> getUsers(String fromDateStr, String toDateStr) throws FromDateIsNotBeforeToDate {
		LocalDate fromDate = LocalDate.parse(fromDateStr);
		LocalDate toDate = LocalDate.parse(toDateStr);
		if (!fromDate.isBefore(toDate)) {
			throw new FromDateIsNotBeforeToDate();
		}
		return localUserDAO.findByBirthDateBetween(fromDate, toDate);
	}

}
