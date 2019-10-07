package com.igor.rest.ex;

@SuppressWarnings("serial")
public class EntityMalformedException extends Exception {
	public EntityMalformedException(String msg) {
		super(msg);
	}
}
