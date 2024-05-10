package com.clearsolutions.userbackend.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.clearsolutions.userbackend.api.model.UserDto;
import com.clearsolutions.userbackend.exception.UserAlreadyExistsException;
import com.clearsolutions.userbackend.exception.UserNotFoundException;
import com.clearsolutions.userbackend.model.User;

import jakarta.transaction.Transactional;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Test
	@Transactional
	public void testCreateUser() {
		UserDto userDto = new UserDto();
		userDto.setEmail("UserA@junit.com");
		userDto.setFirstName("FirstName");
		userDto.setLastName("LastName");
		userDto.setBirthDate(LocalDate.of(2004, 9, 1));
		userDto.setAddress(null);

		Assertions.assertThrows(com.clearsolutions.userbackend.exception.UserAlreadyExistsException.class,
				() -> userService.create(userDto.toUser()), "Email should already be in use.");
		userDto.setEmail("UserServiceTest$testCreateUser@junit.com");
		Assertions.assertDoesNotThrow(() -> userService.create(userDto.toUser()),
				"User should be created successfully.");
	}

	@Test
	@Transactional
	public void whenUserIsCreated_thenUserCanBeFound() throws UserAlreadyExistsException, UserNotFoundException {
		UserDto userDto = new UserDto();
		userDto.setEmail("UserServiceTest$testCreateUser@junit.com");
		userDto.setFirstName("FirstName");
		userDto.setLastName("LastName");
		userDto.setBirthDate(LocalDate.of(2004, 9, 1));
		userDto.setAddress(null);
		User createdUser = userService.create(userDto.toUser());

		User lookedUpUser = userService.findById(createdUser.getId());

		assertThat(lookedUpUser.getEmail()).isEqualTo("UserServiceTest$testCreateUser@junit.com");
		assertThat(lookedUpUser.getFirstName()).isEqualTo("FirstName");
		assertThat(lookedUpUser.getLastName()).isEqualTo("LastName");
		assertThat(lookedUpUser.getBirthDate()).isEqualTo(LocalDate.of(2004, 9, 1));
		assertThat(lookedUpUser.getAddress()).isNull();

//		User user = userService.findByBirthDate((LocalDate.of(2004, 9, 1), (LocalDate.of(2004, 9, 1));

	}
}
