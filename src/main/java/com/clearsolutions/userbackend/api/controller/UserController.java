package com.clearsolutions.userbackend.api.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.clearsolutions.userbackend.api.model.AddressDto;
import com.clearsolutions.userbackend.api.model.UserDto;
import com.clearsolutions.userbackend.constraints.FromLessThenTo;
import com.clearsolutions.userbackend.exception.UserAlreadyExistsException;
import com.clearsolutions.userbackend.exception.UserNotFoundException;
import com.clearsolutions.userbackend.model.Address;
import com.clearsolutions.userbackend.model.User;
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
	public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) throws UserAlreadyExistsException {
		User savedUser = userService.create(userDto.toUser());
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Address> updateUserAddress(@PathVariable("id") Long id,
			@Valid @RequestBody AddressDto addressDto) throws UserNotFoundException {

		Address uupdatedAddress = userService.updateUserAddress(id, addressDto.toAddress());
		return ResponseEntity.ok(uupdatedAddress);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> partialUpdateGeneric(@RequestBody Map<String, Object> updates,
			@PathVariable("id") String id) {

//	    userService.save(updates, id);
		return ResponseEntity.ok("resource updated");
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto)
			throws UserNotFoundException {
		User updatedUser = userService.updateUser(id, userDto.toUser());
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) throws UserNotFoundException {
		userService.deleteById(id);
		return ResponseEntity.ok("User Deleted Successfully");
	}

	@GetMapping()
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.findAll();
		return ResponseEntity.ok(users);
	}

	@FromLessThenTo
	public record FromToDateRequestParameters(
			@NotNull @Past @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@NotNull @Past @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
	}

	@GetMapping(params = { "from", "to" })
	public ResponseEntity<List<User>> getUsers(@Valid FromToDateRequestParameters parameters) {
		List<User> users = userService.findByBirthDate(parameters.from, parameters.to);
		return ResponseEntity.ok(users);
	}

}
