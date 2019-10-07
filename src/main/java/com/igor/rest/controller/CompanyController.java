package com.igor.rest.controller;

import java.sql.Date;
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
import com.igor.entity.Company;
import com.igor.entity.Coupon;
import com.igor.rest.CouponSystem;
import com.igor.rest.ex.EntityMalformedException;
import com.igor.rest.ex.InvalidLoginException;
import com.igor.service.CompanyService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CompanyController {

	private CompanyService service;
	private CouponSystem couponSystem;

	@Autowired
	public CompanyController(CompanyService service, CouponSystem couponSystem) {
		this.service = service;
		this.couponSystem = couponSystem;
	}

	@GetMapping("/{token}/companies")
	public ResponseEntity<Collection<Company>> getAllCompanies(@PathVariable String token) throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.ADMIN)) {
			session.accessed();
			Collection<Company> companies = service.findAll();
			if (companies != null && !companies.isEmpty()) {
				return ResponseEntity.ok(companies);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@GetMapping("/{token}/companies/{id}")
	public ResponseEntity<Company> getCompany(@PathVariable long id, @PathVariable String token) throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.ADMIN)) {
			session.accessed();
			Company company = service.findById(id);
			if (company != null) {
				return ResponseEntity.ok(company);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@PostMapping("/{token}/companies")
	public ResponseEntity<Company> addCompany(@RequestBody Company company, @PathVariable String token) throws InvalidLoginException, EntityMalformedException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.ADMIN)) {
			session.accessed();
			return ResponseEntity.ok(service.create(company));
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@PutMapping("/{token}/companies")
	public ResponseEntity<Company> updateCompany(@RequestBody Company company, @PathVariable String token) throws InvalidLoginException, EntityMalformedException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.ADMIN)) {
			session.accessed();
			return ResponseEntity.ok(service.update(company));
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@DeleteMapping("/{token}/companies/{id}")
	public ResponseEntity<Company> deleteCompany(@PathVariable long id, @PathVariable String token) throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.ADMIN)) {
			session.accessed();
			Company company = service.findById(id);
			if (company != null) {
				service.deleteById(id);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@GetMapping("/companies/{token}/coupons")
	public ResponseEntity<Collection<Coupon>> getAllCompanyCoupons(@PathVariable String token) throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.COMPANY)) {
			session.accessed();
			Collection<Coupon> coupons = service.findCompanyCoupons(session.getUserId());
			if (coupons != null && !coupons.isEmpty()) {
				return ResponseEntity.ok(coupons);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@GetMapping("/companies/{token}/coupons/byCategory/{category}")
	public ResponseEntity<Collection<Coupon>> getAllCompanyCouponsByCategory(@PathVariable String token,
			@PathVariable int category) throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.COMPANY)) {
			session.accessed();
			Collection<Coupon> coupons = service.findCompanyCouponsByCategory(session.getUserId(), category);
			if (coupons != null && !coupons.isEmpty()) {
				return ResponseEntity.ok(coupons);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@GetMapping("/companies/{token}/coupons/byPrice/{price}")
	public ResponseEntity<Collection<Coupon>> getAllCompanyCouponsLessThanPrice(@PathVariable String token,
			@PathVariable double price) throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.COMPANY)) {
			session.accessed();
			Collection<Coupon> coupons = service.findCompanyCouponsLessThan(session.getUserId(), price);
			if (coupons != null && !coupons.isEmpty()) {
				return ResponseEntity.ok(coupons);
			}
			return ResponseEntity.noContent().build();			
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@GetMapping("/companies/{token}/coupons/byDate/{date}")
	public ResponseEntity<Collection<Coupon>> getAllCompanyCouponsBeforeDate(@PathVariable String token,
			@PathVariable Date date) throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.COMPANY)) {
			session.accessed();
			Collection<Coupon> coupons = service.findCompanyCouponsBeforeDate(session.getUserId(), date);
			if (coupons != null && !coupons.isEmpty()) {
				return ResponseEntity.ok(coupons);
			}
			return ResponseEntity.noContent().build();			
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@PostMapping("/companies/{token}/coupons")
	public ResponseEntity<Coupon> createCoupon(@PathVariable String token, @RequestBody Coupon coupon) throws InvalidLoginException, EntityMalformedException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.COMPANY)) {
			session.accessed();
			return ResponseEntity.ok(service.createCoupon(coupon, service.findById(session.getUserId())));
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@PutMapping("/companies/{token}/coupons")
	public ResponseEntity<Coupon> updateCoupon(@PathVariable String token, @RequestBody Coupon coupon) throws InvalidLoginException, EntityMalformedException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.COMPANY)) {
			session.accessed();
			return ResponseEntity.ok(service.updateCoupon(coupon, service.findById(session.getUserId())));
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}

	@DeleteMapping("/companies/{token}/coupons/{couponId}")
	public ResponseEntity<Coupon> deleteCoupon(@PathVariable String token, @PathVariable long couponId) throws InvalidLoginException {
		ClientSession session = couponSystem.getClientSession(token);
		if (session != null && session.getUserType().equals(ClientSession.COMPANY)) {
			session.accessed();
			List<Coupon> coupons = service.findById(session.getUserId()).getCoupons();
			Coupon coupon = service.findCouponById(couponId);
			if (coupon != null && coupons.contains(coupon)) {
				coupons.remove(coupon);
				service.deleteCouponById(couponId);
			}
			return ResponseEntity.noContent().build();
		}
		throw new InvalidLoginException("Session expired or does not exist!");
	}
}
