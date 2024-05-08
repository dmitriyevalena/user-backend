package com.clearsolutions.userbackend.model.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.clearsolutions.userbackend.model.Address;

public interface AddressDAO extends ListCrudRepository<Address, Integer> {

}
