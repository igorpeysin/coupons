package com.igor.service;

import java.util.Collection;
import java.util.List;

import com.igor.entity.Coupon;
import com.igor.entity.Customer;
import com.igor.rest.ex.EntityMalformedException;

public interface CustomerService {
	Collection<Customer> findAll();

	Customer findById(long id);
	
	Customer create(Customer customer) throws EntityMalformedException;

	Customer update(Customer customer) throws EntityMalformedException;
	
	void deleteById(long id);
	
	List<Coupon> findAllCoupons();
	
	List<Coupon> findCustomerCoupons(long id);
	
	List<Coupon> findCustomerCouponsByCategory(long id, int category);
	
	List<Coupon> findCustomerCouponsLessThan(long id, double price);
	
	Coupon findByIdCoupon(long id);
}
