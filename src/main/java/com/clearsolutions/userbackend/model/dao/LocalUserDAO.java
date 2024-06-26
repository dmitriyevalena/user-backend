package com.clearsolutions.userbackend.model.dao;

import com.clearsolutions.userbackend.model.User;
import org.springframework.data.repository.ListCrudRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LocalUserDAO extends ListCrudRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
	List<User> findByBirthDateBetween(LocalDate fromDate, LocalDate toDate);
}
