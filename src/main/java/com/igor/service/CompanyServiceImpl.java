package com.igor.service;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.igor.dao.ClientSessionRepository;
import com.igor.dao.CompanyRepository;
import com.igor.dao.CouponRepository;
import com.igor.entity.ClientSession;
import com.igor.entity.Company;
import com.igor.entity.Coupon;
import com.igor.rest.ex.EntityMalformedException;

@Service
public class CompanyServiceImpl implements CompanyService {

	private CompanyRepository companyRepo;
	private CouponRepository couponRepo;
	private ClientSessionRepository sessionRepo;

	@Autowired
	public CompanyServiceImpl(CompanyRepository companyRepo, CouponRepository couponRepo,
			ClientSessionRepository sessionRepo) {
		this.companyRepo = companyRepo;
		this.couponRepo = couponRepo;
		this.sessionRepo = sessionRepo;
	}

	@Override
	public Collection<Company> findAll() {
		return companyRepo.findAll();
	}

	@Override
	public Company findById(long id) {
		return companyRepo.findById(id).orElse(null);
	}

	@Override
	public Company create(Company company) throws EntityMalformedException {
		if (company != null && company.getEmail() != null && company.getName() != null
				&& company.getPassword() != null) {
			company.setId(0);
			return companyRepo.save(company);
		}
		throw new EntityMalformedException("Company data is not full!");
	}

	@Override
	public Company update(Company company) throws EntityMalformedException {
		if (company != null && company.getEmail() != null && company.getName() != null
				&& company.getPassword() != null) {
			return companyRepo.save(company);
		}
		throw new EntityMalformedException("Company data is not full!");
	}

	@Override
	public void deleteById(long id) {
		companyRepo.deleteById(id);
		ClientSession session = sessionRepo.findByUserId(id);
		if (session != null) {
			sessionRepo.delete(session);
		}
	}

	@Override
	public Collection<Coupon> findCompanyCoupons(long id) {
		return couponRepo.findAllByCompanyId(id);
	}

	@Override
	public List<Coupon> findCompanyCouponsByCategory(long id, int category) {
		return couponRepo.findAllByCompanyIdAndCategory(id, category);
	}

	@Override
	public List<Coupon> findCompanyCouponsLessThan(long id, double price) {
		return couponRepo.findAllByCompanyIdAndPriceLessThan(id, price);
	}

	@Override
	public List<Coupon> findCompanyCouponsBeforeDate(long id, Date date) {
		return couponRepo.findAllByCompanyIdAndEndDateBefore(id, date);
	}

	@Override
	public Coupon findCouponById(long id) {
		return couponRepo.findById(id).orElse(null);
	}

	@Override
	public Coupon createCoupon(Coupon coupon, Company company) throws EntityMalformedException {
		if (isCouponCorrect(coupon)) {
			coupon.setId(0);
			coupon.setCompany(company);
			couponRepo.save(coupon);
			return coupon;
		}
		throw new EntityMalformedException("Coupon data is not full!");
	}

	private boolean isCouponCorrect(Coupon coupon) {
		return coupon != null && coupon.getTitle() != null && coupon.getStartDate() != null
				&& coupon.getEndDate() != null && coupon.getCategory() >= 0 && coupon.getCategory() < 8
				&& coupon.getPrice() > 0;
	}

	@Override
	public Coupon updateCoupon(Coupon coupon, Company company) throws EntityMalformedException {
		if (isCouponCorrect(coupon) && company.getCoupons().contains(coupon)) {
			coupon.setCompany(company);
			return couponRepo.save(coupon);
		}
		throw new EntityMalformedException("Coupon data is not full!");
	}

	@Override
	public void deleteCouponById(long id) {
		couponRepo.deleteById(id);
	}

}
