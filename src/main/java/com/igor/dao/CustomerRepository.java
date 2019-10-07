package com.igor.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.igor.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Customer findByEmailAndPassword(String email, String password);
}
