package com.igor.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.igor.entity.ClientSession;
@Repository
public interface ClientSessionRepository extends JpaRepository<ClientSession, String> {
	ClientSession findByToken(String token);
	
	@Query("FROM ClientSession AS s WHERE s.lastAccessedMillis/1000 + 30*60 < unix_timestamp()")
	List<ClientSession> findExpiredClientSessions();
	
	ClientSession findByUserId(long id);
}
