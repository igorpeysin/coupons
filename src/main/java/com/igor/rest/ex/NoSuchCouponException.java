package com.igor.rest.ex;

@SuppressWarnings("serial")
public class NoSuchCouponException extends Exception {
	public NoSuchCouponException(String msg) {
		super(msg);
	}
}
