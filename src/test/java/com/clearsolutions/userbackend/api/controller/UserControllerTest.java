package com.clearsolutions.userbackend.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.clearsolutions.userbackend.api.model.AddressDto;
import com.clearsolutions.userbackend.api.model.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

/**
 * Tests the endpoints in the UserController.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	
    private static final String APPLICATION_JSON_PATCH_JSON = "application/json-patch+json";

	/** The Mocked MVC. */
	@Autowired
	private MockMvc mvc;

	/**
	 * Tests the create endpoint.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testCreateUser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();

		UserDto userDto = new UserDto();
		userDto.setFirstName("FirstName");
		userDto.setLastName("LastName");
		userDto.setBirthDate(LocalDate.of(2004, 9, 1));
		AddressDto addressDto = new AddressDto();
		addressDto.setCountry("Ukraine");
		addressDto.setCity("Dnipro");
		addressDto.setZipCode("12345");
		addressDto.setStreet("Darvina");
		userDto.setAddress(addressDto);

		// Null or blank email.
		userDto.setEmail(null);
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());
		userDto.setEmail("");
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());

		// Email pattern.
		userDto.setEmail("1234567");
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());

		userDto.setEmail("UserServiceTest$testCreateUser@junit.com");

		// Null or blank first name.
		userDto.setFirstName(null);
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());
		userDto.setFirstName("");
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());

		userDto.setFirstName("FirstName");

		// Null or blank last name.
		userDto.setLastName(null);
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());
		userDto.setLastName("");
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());

		userDto.setLastName("LastName");

		// Null birth date
		userDto.setBirthDate(null);
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());

		// Birth date isn't earlier than current date
		userDto.setBirthDate(LocalDate.of(2050, 9, 1));
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());

		// User is not more than [18] years old.
		userDto.setBirthDate(LocalDate.of(2020, 9, 1));
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());

		userDto.setBirthDate(LocalDate.of(2004, 9, 1));

		// Valid user create.
		mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isCreated())
				.andExpect(redirectedUrlPattern("http://*/users/4"));
	}

	/**
	 * Tests the update endpoint.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testUpdateUser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();

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

		// User doesnt' exist.
		mvc.perform(put("/users/6").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isNotFound());

		mvc.perform(put("/users/1").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isOk());
	}

	/**
	 * Tests partial update endpoint.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testPartialUpdateUser() throws Exception {
		String patchInstructions = "[{\"op\":\"replace\",\"path\": \"/phoneNumber\",\"value\":\"001-555-5678\"}, {\"op\":\"replace\",\"path\": \"/birthDate\",\"value\":\"2005-09-01\"}]";
		
		// User doesnt' exist.
		mvc.perform(patch("/users/6").contentType(APPLICATION_JSON_PATCH_JSON).content(patchInstructions))
		.andExpect(status().isNotFound());
		
		mvc.perform(patch("/users/1").contentType(APPLICATION_JSON_PATCH_JSON).content(patchInstructions))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.phoneNumber", is("001-555-5678")))
				.andExpect(jsonPath("$.birthDate", is("2005-09-01")))
				.andExpect(jsonPath("$.email", is("UserA@junit.com")))
				.andExpect(jsonPath("$.firstName", is("UserA-FirstName")))
				.andExpect(jsonPath("$.lastName", is("UserA-LastName")));
	}

	/**
	 * Tests the delete endpoint.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testDeleteUser() throws Exception {
		// User doesnt' exist.
		mvc.perform(delete("/users/6")).andExpect(status().isNotFound());

		mvc.perform(delete("/users/1")).andExpect(status().isOk());
	}
	
	/**
	 * Tests the search endpoint.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testGetUsersByBirthDateRange() throws Exception {		
		// From “From” date is less than “To”.  
		mvc.perform(get("/users?from=2005-01-01&to=2004-01-01")).andExpect(status().isBadRequest());
		mvc.perform(get("/users?from=2004-01-01&to=2005-01-01")).andExpect(status().isOk());
	}
}
