package com.igor.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.igor.entity.ClientSession;
import com.igor.rest.CouponSystem;
import com.igor.rest.ex.InvalidLoginException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class LoginController {
	
	private CouponSystem couponSystem;

	@Autowired
	public LoginController(CouponSystem couponSystem) {
		this.couponSystem = couponSystem;
	}
	
	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) throws InvalidLoginException {
		
			ClientSession session = couponSystem.login(email, password);
			return ResponseEntity.ok(session.getToken());
	}
	
	@DeleteMapping("/logout/{token}")
	public ResponseEntity<String> logout(@PathVariable String token) {
		couponSystem.logout(token);
		return ResponseEntity.noContent().build();
	}

}
