package com.igor.cleaner;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.igor.dao.CouponRepository;
import com.igor.entity.Coupon;

@Component
public class CouponCleanerTask implements Runnable {

	private CouponRepository repository;
	
	@Autowired
	public CouponCleanerTask(CouponRepository repository) {
		this.repository = repository;
	}


	@Override
	public void run() {	
		Thread cur = Thread.currentThread();
		while(!cur.isInterrupted()) {
			if (LocalTime.now().isAfter(LocalTime.MIDNIGHT) && LocalTime.now().isBefore(LocalTime.of(0, 0, 5))) {
				List<Coupon> coupons = repository.findExpiredCoupons();
				repository.deleteAll(coupons);
				try {
					Thread.sleep((23*60*60+59*60)*1000);
				} catch (InterruptedException e) {
					break;
				}
			}

		}

	}

}
