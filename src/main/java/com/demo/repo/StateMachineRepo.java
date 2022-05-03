package com.demo.repo;

import java.util.Optional;

import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachine;

public interface StateMachineRepo extends StateMachineRepository<JpaRepositoryStateMachine>{
	
	Optional<JpaRepositoryStateMachine> findByMachineId(String machine_id);

}
