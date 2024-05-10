package com.clearsolutions.userbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.clearsolutions.userbackend.api.model.AddressDto;
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

		Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.create(userDto.toUser()),
				"Email should already be in use.");
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
		AddressDto addressDto = new AddressDto();
		addressDto.setCountry("Ukraine");
		addressDto.setCity("Dnipro");
		addressDto.setZipCode("12345");
		addressDto.setStreet("Darvina");
		userDto.setAddress(addressDto);
		User createdUser = userService.create(userDto.toUser());

		User lookedUpUser = userService.findById(createdUser.getId()).get();

		assertThat(lookedUpUser.getEmail()).isEqualTo("UserServiceTest$testCreateUser@junit.com");
		assertThat(lookedUpUser.getFirstName()).isEqualTo("FirstName");
		assertThat(lookedUpUser.getLastName()).isEqualTo("LastName");
		assertThat(lookedUpUser.getBirthDate()).isEqualTo(LocalDate.of(2004, 9, 1));
		assertNotNull(lookedUpUser.getAddress());
		assertThat(lookedUpUser.getAddress().getCountry()).isEqualTo("Ukraine");
		assertThat(lookedUpUser.getAddress().getCity()).isEqualTo("Dnipro");
		assertThat(lookedUpUser.getAddress().getZipCode()).isEqualTo("12345");
		assertThat(lookedUpUser.getAddress().getStreet()).isEqualTo("Darvina");
	}

	@Test
	@Transactional
	public void testDeleteByIdUser() throws UserNotFoundException {
		assertEquals(userService.findAll().size(), 3);
		Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteById(5L),
				"User with this id should not be found.");
		userService.deleteById(1L);
		assertEquals(userService.findAll().size(), 2);
	}

	@Test
	@Transactional
	public void testUpdateUser() throws UserNotFoundException {

		UserDto userDto = new UserDto();
		userDto.setEmail("ChangedUserA@junit.com");
		userDto.setFirstName("ChangedUserA-FirstName");
		userDto.setLastName("ChangedUserA-LastName");
		userDto.setBirthDate(LocalDate.of(2005, 9, 1));
		AddressDto addressDto = new AddressDto();
		addressDto.setCountry("Ukraine");
		addressDto.setCity("Dnipro");
		addressDto.setZipCode("67890");
		addressDto.setStreet("Darvina");
		userDto.setAddress(addressDto);

		userService.updateUser(1L, userDto.toUser());

		Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(5L, userDto.toUser()),
				"User with this id should not be updated because it can't be found.");

		User updatedUser = userService.updateUser(1L, userDto.toUser());

		assertThat(updatedUser.getEmail()).isEqualTo("ChangedUserA@junit.com");
		assertThat(updatedUser.getFirstName()).isEqualTo("ChangedUserA-FirstName");
		assertThat(updatedUser.getLastName()).isEqualTo("ChangedUserA-LastName");
		assertThat(updatedUser.getBirthDate()).isEqualTo(LocalDate.of(2005, 9, 1));
		assertNotNull(updatedUser.getAddress());
		assertThat(updatedUser.getAddress().getCountry()).isEqualTo("Ukraine");
		assertThat(updatedUser.getAddress().getCity()).isEqualTo("Dnipro");
		assertThat(updatedUser.getAddress().getZipCode()).isEqualTo("67890");
		assertThat(updatedUser.getAddress().getStreet()).isEqualTo("Darvina");
	}

	@Test
	@Transactional
	public void testfindByBirthDate() {
		assertEquals(userService.findByBirthDate(LocalDate.of(2004, 9, 1), LocalDate.of(2004, 9, 1)).size(), 1);
		assertEquals(userService.findByBirthDate(LocalDate.of(2004, 9, 1), LocalDate.of(2004, 11, 1)).size(), 3);
		assertEquals(userService.findByBirthDate(LocalDate.of(2000, 9, 1), LocalDate.of(2000, 11, 1)).size(), 0);
	}
}
