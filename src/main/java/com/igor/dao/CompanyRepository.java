package com.igor.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.igor.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{
	Company findByEmailAndPassword(String email, String password);
}
