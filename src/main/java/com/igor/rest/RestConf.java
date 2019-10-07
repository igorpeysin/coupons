package com.igor.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.igor.cleaner.ClientSessionCleanerTask;
import com.igor.cleaner.CouponCleanerTask;

@Configuration
@PropertySource("classpath:admin.properties")
public class RestConf {
	
	@Bean(name = "couponCleaner")
	public Thread couponCleanerThread(CouponCleanerTask task) {
		return new Thread(task);
	}
	
	@Bean(name= "clientSessionCleaner")
	public Thread clientSessionCleanerThread(ClientSessionCleanerTask task) {
		return new Thread(task);
	}
}
