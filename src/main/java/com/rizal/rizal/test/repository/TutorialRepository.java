package com.rizal.rizal.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rizal.rizal.test.model.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
	
}
