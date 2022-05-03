package com.demo;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachine;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.TaskInput;
import com.demo.repo.StateMachineRepo;

import reactor.core.publisher.Mono;

@RestController
public class TransitionController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	StateMachineFactory<String, String> stateMachineFactory;
	
	@Autowired
	TaskRepo taskRepo;
	
	@Autowired
	JpaStateMachineRepository jpaStateMachineRepository;
	
	@Autowired
	StateMachineRepo stateMachineRepo;
	
	@Autowired
	JpaRepositoryStateMachine jpaRepositoryStateMachine; 
	
	private final StateMachineLogListener listener = new StateMachineLogListener();
	
	private StateMachine<String, String> currentStateMachine;
	
	@Autowired
	private StateMachineService<String, String> stateMachineService;
	
	@PostMapping("/setTask")
	@Transactional
	public String task(
			@RequestBody TaskInput request
			) throws Exception {
		
		String machineId = "machine_id2";
		String taskName = request.getTaskName();
		List<String> events = request.getEvent();
		String taskState = "";		
		
		logger.info("TransitionController :: task :: Checking if given task : {} already exist.",taskName);
		Optional<Tasks> taskOptional =taskRepo.findByTasknameAndMachineid(taskName, machineId);
		/*
		if(taskOptional.isPresent()) {			
			
			logger.info("Task : {} already present.",taskName);
			Tasks task = taskOptional.get();
			taskState = task.getCurrentstate();			
			
			logger.info("TransitionController :: task :: Updating the state machine table with the current state of the task");
			Optional<JpaRepositoryStateMachine> optionalStateMachine = stateMachineRepo.findByMachineId(machineId);
			
			
			if(optionalStateMachine.isPresent()) {
				
				JpaRepositoryStateMachine jpaRepositoryStateMachine = optionalStateMachine.get();
				jpaRepositoryStateMachine.setState(taskState);
				
				stateMachineRepo.save(jpaRepositoryStateMachine);
				logger.info(" TransitionController :: task :: State Machine table updated for task : {}  as State : {} ",taskName,taskState);
			}			
		}
		
		*/
		logger.info("TransitionController :: task :: Triggering events : {}  for machineId : {}",events.toString(),machineId);		
		triggerEvent(events, machineId);		
		logger.info("TransitionController :: task :: Getting updated state from the state machine table");		
		
		String updatedState = "";
		Optional<JpaRepositoryStateMachine> optionalStateMachine = stateMachineRepo.findByMachineId(machineId);		
		
		if(optionalStateMachine.isPresent()) {			
			JpaRepositoryStateMachine jpaRepositoryStateMachine = optionalStateMachine.get();
			updatedState = jpaRepositoryStateMachine.getState();								
		}
		
		logger.info("TransitionController :: task :: Current State : {} ",updatedState);		
		logger.info("TransitionController :: task :: Updating Task table with updated state : {}",updatedState);
		
		Tasks taskEntity;
		if(taskOptional.isPresent()) {
			taskEntity = taskOptional.get();
			taskEntity.setCurrentstate(updatedState);			
		}else {
			taskEntity = new Tasks();
			taskEntity.setCurrentstate(updatedState);
			taskEntity.setMachineid(machineId);
			taskEntity.setTaskname(taskName);
		}
		
		taskRepo.save(taskEntity);
		logger.info("TransitionController :: task :: task table updated");
		
		//delete all entries from state machine table
		/*
		Optional<JpaRepositoryStateMachine> StateMachineFinalOptional = stateMachineRepo.findByMachineId(machineId);		
		
		if(StateMachineFinalOptional.isPresent()) {			
			JpaRepositoryStateMachine jpaRepositoryStateMachine = StateMachineFinalOptional.get();
			//jpaRepositoryStateMachine.setState("");
			stateMachineRepo.save(jpaRepositoryStateMachine);
			//stateMachineRepo.delete(jpaRepositoryStateMachine);
		}
		*/
		return "State changed to "+updatedState;
	}
	
	public void triggerEvent(List<String> events, String machineId) throws Exception {		
		logger.info("TransitionController :: triggerEvent :: Start");
		logger.info("TransitionController :: triggerEvent :: Triggering event :: {} ",events.toString());		
		
		StateMachine<String, String> stateMachine = getStateMachine(machineId);		
		
		if (events != null) {
			for (String event : events) {
				stateMachine
					.sendEvent(Mono.just(MessageBuilder
						.withPayload(event).build()))
					.blockLast();
			}
		}		
		logger.info("TransitionController :: triggerEvent :: End");
	}
	
	private synchronized StateMachine<String, String> getStateMachine(String machineId) throws Exception {
		logger.info("TransitionController :: getStateMachine :: Start");
		logger.info("TransitionController :: getStateMachine :: Getting state machine instance for machineId :: {}",machineId);
		listener.resetMessages();
		if (currentStateMachine == null) {
			currentStateMachine = stateMachineService.acquireStateMachine(machineId, false);	
			currentStateMachine.addStateListener(listener);
			currentStateMachine.startReactively().block();
		} else if (!ObjectUtils.nullSafeEquals(currentStateMachine.getId(), machineId)) {
			stateMachineService.releaseStateMachine(currentStateMachine.getId());
			currentStateMachine.stopReactively().block();
			currentStateMachine = stateMachineService.acquireStateMachine(machineId, false);
			currentStateMachine.addStateListener(listener);
			currentStateMachine.startReactively().block();
		}
		logger.info("TransitionController :: getStateMachine :: End");
		return currentStateMachine;
	}

}
