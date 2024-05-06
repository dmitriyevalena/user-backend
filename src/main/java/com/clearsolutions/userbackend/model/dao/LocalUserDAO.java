package com.clearsolutions.userbackend.model.dao;

import com.clearsolutions.userbackend.model.LocalUser;
import org.springframework.data.repository.ListCrudRepository;
import java.time.LocalDate;
import java.util.List;

public interface LocalUserDAO extends ListCrudRepository<LocalUser, Long> {
	List<LocalUser> findByBirthDateBetween(LocalDate fromDate, LocalDate toDate);
}
