package com.clearsolutions.userbackend.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clearsolutions.userbackend.api.model.CreateUserDto;
import com.clearsolutions.userbackend.exception.IllegalDateException;
import com.clearsolutions.userbackend.exception.UserAlreadyExistsException;
import com.clearsolutions.userbackend.model.LocalUser;
import com.clearsolutions.userbackend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping()
	public ResponseEntity createUser(@Valid @RequestBody CreateUserDto createUserBody) {
		try {
			LocalUser createdUser = userService.createUser(createUserBody);
		    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//			return ResponseEntity.ok().build();
		} catch (UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} catch (IllegalDateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
    @GetMapping()
	public List<LocalUser> getUsers() {
    	return userService.getUsers();
	}
	  
    @GetMapping(params = { "from", "to" })
	public ResponseEntity<List<LocalUser>> getUsers(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate fromDate, @RequestParam ("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate toDate) {
		try {
			List<LocalUser> users = userService.getUsers(fromDate, toDate);
			return ResponseEntity.ok(users);
		} catch (IllegalDateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
