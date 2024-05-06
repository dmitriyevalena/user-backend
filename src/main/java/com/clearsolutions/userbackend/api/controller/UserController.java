package com.clearsolutions.userbackend.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clearsolutions.userbackend.model.LocalUser;
import com.clearsolutions.userbackend.service.UserService;
import com.clearsolutions.userbackend.exception.FromDateIsNotBeforeToDate;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<LocalUser>> getUsersByBirthDate(@RequestParam String from, @RequestParam String to) {
		try {
			List<LocalUser> users = userService.getUsers(from, to);
			return ResponseEntity.ok(users);
		} catch (FromDateIsNotBeforeToDate e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
