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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.clearsolutions.userbackend.api.model.UserDto;
import com.clearsolutions.userbackend.constraints.FromLessThenTo;
import com.clearsolutions.userbackend.exception.UserAlreadyExistsException;
import com.clearsolutions.userbackend.exception.UserNotFoundException;
import com.clearsolutions.userbackend.model.User;
import com.clearsolutions.userbackend.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

	private UserService userService;
	private ObjectMapper objectMapper;

	public UserController(UserService userService) {
		this.userService = userService;
		objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
	}

	@PostMapping()
	public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) throws UserAlreadyExistsException {
		User savedUser = userService.create(userDto.toUser());
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto)
			throws UserNotFoundException {
		User updatedUser = userService.updateUser(id, userDto.toUser());
		return ResponseEntity.ok(updatedUser);
	}

	@PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
	public ResponseEntity<User> partialUpdateUser(@PathVariable("id") Long id, @RequestBody JsonPatch patch)
			throws UserNotFoundException {
		try {
			User user = userService.findById(id);
			User userPatched = applyPatchToUser(patch, user);
			userService.save(userPatched);
			return ResponseEntity.ok(userPatched);
		} catch (JsonPatchException | JsonProcessingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private User applyPatchToUser(JsonPatch patch, User targetUser)
			throws JsonPatchException, JsonProcessingException {
		JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
		return objectMapper.treeToValue(patched, User.class);
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
