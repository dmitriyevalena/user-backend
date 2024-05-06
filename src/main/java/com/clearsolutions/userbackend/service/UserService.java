package com.clearsolutions.userbackend.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.clearsolutions.userbackend.api.model.CreateUserDto;
import com.clearsolutions.userbackend.exception.IllegalDateException;
import com.clearsolutions.userbackend.exception.UserAlreadyExistsException;
import com.clearsolutions.userbackend.model.LocalUser;
import com.clearsolutions.userbackend.model.dao.LocalUserDAO;

@Service
public class UserService {
	
    @Value("${user.age}")
    private String userAge;

	private LocalUserDAO localUserDAO;

	public UserService(LocalUserDAO localUserDAO) {
		this.localUserDAO = localUserDAO;
	}

	public LocalUser createUser(CreateUserDto createUserBody) throws UserAlreadyExistsException, IllegalDateException{
        if(localUserDAO.findByEmailIgnoreCase(createUserBody.getEmail()).isPresent()){
            throw new UserAlreadyExistsException();
        }
		LocalDate curDate = LocalDate.now();  
		if(Period.between(createUserBody.getBirthDate(), curDate).getYears()<18) {
			throw new IllegalDateException();
		}
        return localUserDAO.save(createUserBody.toLocalUser());
	}
	
    public List<LocalUser> getUsers(){
        return localUserDAO.findAll();
    }
	
	public List<LocalUser> getUsers(LocalDate fromDate, LocalDate toDate) throws IllegalDateException{
			if (!fromDate.isBefore(toDate)) {
				throw new IllegalDateException();
			}
			return localUserDAO.findByBirthDateBetween(fromDate, toDate);		
	}

}
