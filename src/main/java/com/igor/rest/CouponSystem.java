package com.igor.rest;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.igor.dao.ClientSessionRepository;
import com.igor.dao.CompanyRepository;
import com.igor.dao.CustomerRepository;
import com.igor.entity.ClientSession;
import com.igor.entity.Company;
import com.igor.entity.Customer;
import com.igor.rest.ex.InvalidLoginException;

@Service
public class CouponSystem {

	private static final int LENGTH_TOKEN = 16;

	private ApplicationContext context;
	private ClientSessionRepository clientSessionRepository;
	private CompanyRepository companyRepository;
	private CustomerRepository customerRepository;
	private Thread couponCleaner;
	private Thread clientSessionCleaner;

	@Autowired
	public CouponSystem(ApplicationContext context, ClientSessionRepository clientSessionRepository,
			CompanyRepository companyRepository, CustomerRepository customerRepository,
			@Qualifier("couponCleaner") Thread couponCleaner, @Qualifier("clientSessionCleaner") Thread clientSessionCleaner) {
		this.context = context;
		this.clientSessionRepository = clientSessionRepository;
		this.companyRepository = companyRepository;
		this.customerRepository = customerRepository;
		this.couponCleaner = couponCleaner;
		this.clientSessionCleaner = clientSessionCleaner;
	}

	@Value("${admin.email}")
	private String adminEmail;
	@Value("${admin.password}")
	private String adminPassword;

	@PostConstruct
	private void onPostConstruct() {
		couponCleaner.start();
		clientSessionCleaner.start();
	}

	@PreDestroy
	private void onPreDestroy() {
		couponCleaner.interrupt();
		clientSessionCleaner.interrupt();
	}

	public ClientSession login(String email, String password) throws InvalidLoginException {
		ClientSession session = context.getBean(ClientSession.class);
		if (adminLogin(email, password, session) || companyLogin(email, password, session) || customerLogin(email, password, session)) {
			clientSessionRepository.save(session);
			return session;	
		}
		throw new InvalidLoginException("Invalid email or password!");
	}
	
	public ClientSession getClientSession(String token) {
		return clientSessionRepository.findByToken(token);
	}
	
	public void logout(String token) {
		Optional<ClientSession> session = clientSessionRepository.findById(token);
		if (session.isPresent()) {
			clientSessionRepository.delete(session.get());
		}
	}

	private boolean customerLogin(String email, String password, ClientSession session) {
		Customer customer = customerRepository.findByEmailAndPassword(email, password);
		if (customer == null) {
			return false;
		}
		session.setToken(generateToken());
		session.setUserId(customer.getId());
		session.setUserType(ClientSession.CUSTOMER);
		session.accessed();
		return true;
	}

	private boolean companyLogin(String email, String password, ClientSession session) {
		Company company = companyRepository.findByEmailAndPassword(email, password);
		if (company == null) {
			return false;
		}
		session.setToken(generateToken());
		session.setUserId(company.getId());
		session.setUserType(ClientSession.COMPANY);
		session.accessed();
		return true;
	}

	private boolean adminLogin(String email, String password, ClientSession session) {
		if (adminEmail.equals(email) && adminPassword.equals(password)) {
			session.setToken(generateToken());
			session.setUserType(ClientSession.ADMIN);
			session.accessed();
			session.setUserId(0);
			return true;
		} else {
			return false;
		}
	}

	private String generateToken() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, LENGTH_TOKEN);
	}
}
