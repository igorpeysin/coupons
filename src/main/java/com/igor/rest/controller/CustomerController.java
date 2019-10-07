package com.igor.rest.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igor.entity.ClientSession;
import com.igor.entity.Coupon;
import com.igor.entity.Customer;
import com.igor.rest.CouponSystem;
import com.igor.rest.ex.CouponAlreadyPurchasedException;
import com.igor.rest.ex.EntityMalformedException;
import com.igor.rest.ex.InvalidLoginException;
import com.igor.rest.ex.NoSuchCouponException;
import com.igor.service.CustomerService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CustomerController {

	private CustomerService service;
	private CouponSystem couponSystem;

	@Autowired
	public CustomerController(CustomerService service, CouponSystem couponSystem) {
		this.service = service;
		this.couponSystem = couponSystem;
	}

	@GetMapping("/{token}/customers")
	public ResponseEntity<Collection<Customer>> getAllCustomers(@PathVariable String token)
			throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.ADMIN)) {
			session.accessed();
			Collection<Customer> customers = service.findAll();
			if (customers != null && !customers.isEmpty()) {
				return ResponseEntity.ok(customers);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@GetMapping("/{token}/customers/{id}")
	public ResponseEntity<Customer> getCustomer(@PathVariable String token, @PathVariable long id)
			throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.ADMIN)) {
			session.accessed();
			Customer customer = service.findById(id);
			if (customer != null) {
				return ResponseEntity.ok(customer);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@PostMapping("/{token}/customers")
	public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer, @PathVariable String token)
			throws InvalidLoginException, EntityMalformedException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.ADMIN)) {
			session.accessed();
			return ResponseEntity.ok(service.create(customer));
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@PutMapping("/{token}/customers")
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable String token)
			throws InvalidLoginException, EntityMalformedException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.ADMIN)) {
			session.accessed();
			return ResponseEntity.ok(service.update(customer));
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@DeleteMapping("/{token}/customers/{id}")
	public ResponseEntity<Customer> deleteCustomer(@PathVariable long id, @PathVariable String token)
			throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.ADMIN)) {
			session.accessed();
			if (service.findById(id) != null) {
				service.deleteById(id);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@GetMapping("/coupons")
	public ResponseEntity<Collection<Coupon>> getAllCoupons() {
		List<Coupon> coupons = service.findAllCoupons();
		if (coupons != null && !coupons.isEmpty()) {
			return ResponseEntity.ok(coupons);
		}
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/coupons/{id}")
	public ResponseEntity<Coupon> getCoupon(@PathVariable long id) {
		Coupon coupon = service.findByIdCoupon(id);
		if (coupon != null) {
			return ResponseEntity.ok(coupon);
		}
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/customers/{token}/coupons")
	public ResponseEntity<Collection<Coupon>> getAllCustomerCoupons(@PathVariable String token)
			throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.CUSTOMER)) {
			session.accessed();
			List<Coupon> coupons = service.findCustomerCoupons(session.getUserId());
			if (coupons != null && !coupons.isEmpty()) {
				return ResponseEntity.ok(coupons);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@GetMapping("/customers/{token}/coupons/byCategory/{category}")
	public ResponseEntity<Collection<Coupon>> getAllCustomerCouponsByCategory(@PathVariable String token,
			@PathVariable int category) throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.CUSTOMER)) {
			session.accessed();
			List<Coupon> coupons = service.findCustomerCouponsByCategory(session.getUserId(), category);
			if (coupons != null && !coupons.isEmpty()) {
				return ResponseEntity.ok(coupons);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@GetMapping("/customers/{token}/coupons/byPrice/{price}")
	public ResponseEntity<Collection<Coupon>> getAllCustomerCouponsByPrice(@PathVariable String token,
			@PathVariable double price) throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.CUSTOMER)) {
			session.accessed();
			List<Coupon> coupons = service.findCustomerCouponsLessThan(session.getUserId(), price);
			if (coupons != null && !coupons.isEmpty()) {
				return ResponseEntity.ok(coupons);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@PutMapping("/customers/{token}/coupons/{id}")
	public ResponseEntity<Coupon> purchaseCoupon(@PathVariable String token, @PathVariable long id)
			throws InvalidLoginException, CouponAlreadyPurchasedException, EntityMalformedException, NoSuchCouponException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.CUSTOMER)) {
			session.accessed();
			Customer customer = service.findById(session.getUserId());
			Coupon coupon = service.findByIdCoupon(id);

			if (coupon != null) {
				if (coupon.getAmount() > 0 && customer.add(coupon)) {
					coupon.setAmount(coupon.getAmount() - 1);
					service.update(customer);
					return ResponseEntity.ok(coupon);
				}
				throw new CouponAlreadyPurchasedException("You already purchased this coupon!");
			}
			throw new NoSuchCouponException("No such coupon exists!");
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}
}
