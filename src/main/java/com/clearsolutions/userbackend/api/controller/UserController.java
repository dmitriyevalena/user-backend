package com.clearsolutions.userbackend.api.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.clearsolutions.userbackend.api.model.CreateUserDto;
import com.clearsolutions.userbackend.constraints.FromLessThenTo;
import com.clearsolutions.userbackend.exception.UserAlreadyExistsException;
import com.clearsolutions.userbackend.exception.UserNotFoundException;
import com.clearsolutions.userbackend.model.LocalUser;
import com.clearsolutions.userbackend.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping()
	public ResponseEntity<LocalUser> createUser(@Valid @RequestBody CreateUserDto createUserBody)
			throws UserAlreadyExistsException {
		LocalUser savedUser = userService.create(createUserBody.toLocalUser());
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) throws UserNotFoundException {
		userService.deleteById(id);
	}

	@GetMapping()
	public ResponseEntity<List<LocalUser>> getAllUsers() {
		return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
	}

	@FromLessThenTo
	public record FromToDateRequestParameters(
			@NotNull @Past @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@NotNull @Past @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
	}

	@GetMapping(params = { "from", "to" })
	public ResponseEntity<List<LocalUser>> getUsers(@Valid FromToDateRequestParameters parameters) {
		List<LocalUser> users = userService.findByBirthDate(parameters.from, parameters.to);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

}
