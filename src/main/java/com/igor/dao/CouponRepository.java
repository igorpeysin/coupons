package com.igor.dao;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.igor.entity.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>{

	/**
	 * Get all coupons from a specified company.
	 * @param id The id of the company.
	 * @return Coupons of the company.
	 */
	Collection<Coupon> findAllByCompanyId(long id);
	/**
	 * Get all coupons in a given category for a specified company.
	 * @param id The id of the company.
	 * @param category The category.
	 * @return Coupons of the company for a given category.
	 */
	List<Coupon> findAllByCompanyIdAndCategory(long id, int category);
	
	/**
	 * Get all coupons below price for a specified company.
	 * @param id The id of the company.
	 * @param price The price below which all coupons will be returned.
	 * @return Coupons below price for the company.
	 */
	List<Coupon> findAllByCompanyIdAndPriceLessThan(long id, double price);
	
	/**
	 * Get all coupons before a given end date, for a specified company.
	 * @param id The id of the company.
	 * @param date The date.
	 * @return Coupons before the end date.
	 */
	List<Coupon> findAllByCompanyIdAndEndDateBefore(long id, Date date);
	
	/**
	 * Get all coupons from a specified customer.
	 * @param id The id of the customer.
	 * @return Coupons of the customer.
	 */
	@Query("select coupons from Customer customer join customer.coupons coupons where customer.id=:id")
	List<Coupon> findAllByCustomerId(long id);
	
	/**
	 * Get all customer coupons in a given category  .
	 * @param id The id of the customer.
	 * @param category The category.
	 * @return Coupons  for a given category.
	 */
	@Query("select coupons from Customer cust join cust.coupons coupons where cust.id =:id and coupons.category =:category")
	List<Coupon> findAllByCustomerIdAndCategory(long id, int category);
	
	/**
	 * Get all coupons below price for a customer.
	 * @param id The id of the customer.
	 * @param price The price below which all coupons will be returned.
	 * @return Coupons below price for the customer.
	 */
	@Query("select coupons from Customer cust join cust.coupons coupons where cust.id =:id and coupons.price <:price")
	List<Coupon> findAllByCustomerIdAndPriceLessThan(long id, double price);
	/**
	 * 
	 * @return All expired coupons in the system.
	 */
	@Query("from Coupon as c where c.endDate < CURRENT_DATE")
	List<Coupon> findExpiredCoupons();
 }
