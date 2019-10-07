package com.igor.rest.ex;

@SuppressWarnings("serial")
public class CouponAlreadyPurchasedException extends Exception {
	public CouponAlreadyPurchasedException(String msg) {
		super(msg);
	}
}
