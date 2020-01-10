package com.thasmai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thasmai.jpa.entities.Eligibility;

@Repository
public interface EligibilityRepository extends JpaRepository<Eligibility, Long> {
	
	List<Eligibility> findBy();
}
