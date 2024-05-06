package com.clearsolutions.userbackend.model.dao;

import com.clearsolutions.userbackend.model.LocalUser;
import org.springframework.data.repository.ListCrudRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LocalUserDAO extends ListCrudRepository<LocalUser, Long> {
    Optional<LocalUser> findByEmailIgnoreCase(String email);
	List<LocalUser> findByBirthDateBetween(LocalDate fromDate, LocalDate toDate);
}
