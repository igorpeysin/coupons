package com.igor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Entity
@Table(name = "client_session")
public class ClientSession {
	public static final String ADMIN = "admin";
	public static final String COMPANY = "company";
	public static final String CUSTOMER = "customer";
	
	@Id
	@Column(name = "token", length = 16)
	private String token;
	@Column(name = "last_accessed_millis")
	private long lastAccessedMillis;
	@Column(name = "user_type")
	private String userType;
	@Column(name = "user_id")
	private long userId;
	
	public ClientSession() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getLastAccessedMillis() {
		return lastAccessedMillis;
	}

	public void accessed() {
		this.lastAccessedMillis = System.currentTimeMillis();
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	
}
