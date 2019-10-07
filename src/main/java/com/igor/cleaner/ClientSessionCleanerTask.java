package com.igor.cleaner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.igor.dao.ClientSessionRepository;
import com.igor.entity.ClientSession;

@Component
public class ClientSessionCleanerTask implements Runnable {

	private ClientSessionRepository repository;
	
	@Autowired
	public ClientSessionCleanerTask(ClientSessionRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run() {
		Thread cur = Thread.currentThread();
		while (!cur.isInterrupted()) {
			List<ClientSession> sessions = repository.findExpiredClientSessions();
			
			for (ClientSession clientSession : sessions) {
					repository.delete(clientSession);
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	

}
