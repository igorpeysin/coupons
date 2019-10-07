package com.igor.service;

import java.sql.Date;
import java.util.Collection;

import com.igor.entity.Company;
import com.igor.entity.Coupon;
import com.igor.rest.ex.EntityMalformedException;

public interface CompanyService {
	Collection<Company> findAll();
	
	Company findById(long id);
	
	Company create(Company company) throws EntityMalformedException;
	
	Company update(Company company) throws EntityMalformedException;
	
	void deleteById(long id);
	
	Coupon findCouponById(long id);
	
	Coupon createCoupon(Coupon coupon, Company company) throws EntityMalformedException;
	
	Coupon updateCoupon(Coupon coupon, Company company) throws EntityMalformedException;
	
	void deleteCouponById(long id);
	
	Collection<Coupon> findCompanyCoupons(long id);
	
	Collection<Coupon> findCompanyCouponsByCategory(long id, int category);
	
	Collection<Coupon> findCompanyCouponsLessThan(long id, double price);
	
	Collection<Coupon> findCompanyCouponsBeforeDate(long id, Date date);
}
