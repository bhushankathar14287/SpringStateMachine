package com.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Tasks, Long> {
	
	Optional<Tasks> findByTasknameAndMachineid(String taskname, String machineId);

}
