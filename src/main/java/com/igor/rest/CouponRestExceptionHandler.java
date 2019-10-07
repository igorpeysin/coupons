package com.igor.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.igor.rest.controller.CompanyController;
import com.igor.rest.controller.CustomerController;
import com.igor.rest.controller.LoginController;
import com.igor.rest.ex.CouponAlreadyPurchasedException;
import com.igor.rest.ex.EntityMalformedException;
import com.igor.rest.ex.InvalidLoginException;
import com.igor.rest.ex.NoSuchCouponException;

@ControllerAdvice(assignableTypes = {CompanyController.class, CustomerController.class, LoginController.class})
public class CouponRestExceptionHandler {

	@ExceptionHandler(InvalidLoginException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public CouponErrorResponse handleUnauthorized(InvalidLoginException ex) {
		return CouponErrorResponse.now(HttpStatus.UNAUTHORIZED, String.format("Unauthorized: %s", ex.getMessage()));
	}
	
	@ExceptionHandler(EntityMalformedException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public CouponErrorResponse handleEntityMalformed(EntityMalformedException ex) {
		return CouponErrorResponse.now(HttpStatus.BAD_REQUEST, ex.getMessage());
	}
	
	@ExceptionHandler(CouponAlreadyPurchasedException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public CouponErrorResponse handlePurchasedCoupon(CouponAlreadyPurchasedException ex) {
		return CouponErrorResponse.now(HttpStatus.CONFLICT, ex.getMessage());
	}
	
	@ExceptionHandler(NoSuchCouponException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public CouponErrorResponse handleNoSuchCoupon(NoSuchCouponException ex) {
		return CouponErrorResponse.now(HttpStatus.BAD_REQUEST, ex.getMessage());
	}
}
