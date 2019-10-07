package com.igor.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.igor.dao.ClientSessionRepository;
import com.igor.dao.CouponRepository;
import com.igor.dao.CustomerRepository;
import com.igor.entity.ClientSession;
import com.igor.entity.Coupon;
import com.igor.entity.Customer;
import com.igor.rest.ex.EntityMalformedException;

@Service
public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository customerRepo;
	private CouponRepository couponRepo;
	private ClientSessionRepository sessionRepo;
	
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepo, CouponRepository couponRepo, ClientSessionRepository sessionRepo) {
		this.customerRepo = customerRepo;
		this.couponRepo = couponRepo;
		this.sessionRepo = sessionRepo;
	}


	@Override
	public Collection<Customer> findAll() {
		return customerRepo.findAll();
	}


	@Override
	public Customer findById(long id) {
		return customerRepo.findById(id).orElse(null);
	}

	@Override
	public Customer create(Customer customer) throws EntityMalformedException {
		if (isCustomerCorrect(customer)) {
			customer.setId(0);
			return customerRepo.save(customer);
		}
		throw new EntityMalformedException("Customer data is not full!");
	}


	private boolean isCustomerCorrect(Customer customer) {
		return customer != null && customer.getFirstName()!=null && customer.getEmail()!=null && customer.getPassword()!=null;
	}	
	
	@Override
	public Customer update(Customer customer) throws EntityMalformedException {
		if (isCustomerCorrect(customer)) {
			return customerRepo.save(customer);
		}
		throw new EntityMalformedException("Customer data is not full!");
	}

	@Override
	public void deleteById(long id) {
			customerRepo.deleteById(id);	
			ClientSession session = sessionRepo.findByUserId(id);
			if (session != null) {
				sessionRepo.delete(session);
			}
	}


	@Override
	public List<Coupon> findCustomerCoupons(long id) {
		return couponRepo.findAllByCustomerId(id);
	}


	@Override
	public List<Coupon> findCustomerCouponsByCategory(long id, int category) {
		return couponRepo.findAllByCustomerIdAndCategory(id, category);
	}


	@Override
	public List<Coupon> findCustomerCouponsLessThan(long id, double price) {
		return couponRepo.findAllByCustomerIdAndPriceLessThan(id, price);
	}


	@Override
	public List<Coupon> findAllCoupons() {
		return couponRepo.findAll();
	}


	@Override
	public Coupon findByIdCoupon(long id) {
		return couponRepo.findById(id).orElse(null);
	}
}
