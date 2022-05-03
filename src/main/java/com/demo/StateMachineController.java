package com.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineContextRepository;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.RepositoryState;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.jpa.JpaRepositoryState;
import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachine;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.TaskInput;
import com.demo.repo.StateMachineRepo;

import reactor.core.publisher.Mono;

@RestController
public class StateMachineController {
	
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
	private StateRepository<? extends RepositoryState> stateRepository;

	@Autowired
	private TransitionRepository<? extends RepositoryTransition> transitionRepository;
	
	//state machine table entity
	@Autowired
	JpaRepositoryStateMachine jpaRepositoryStateMachine; 
	
	private final StateMachineLogListener listener = new StateMachineLogListener();
	
	private StateMachine<String, String> currentStateMachine;
	
	@Autowired
	private StateMachineService<String, String> stateMachineService;
	
	
	
	
	
	
	@GetMapping("/state")
	public void state() {
		
		StateMachine<String, String> stateMachine = stateMachineFactory.getStateMachine("machine_id2");
		StateMachineLogListener listener = new StateMachineLogListener();
		stateMachine.addStateListener(listener);
		stateMachine.startReactively().block();

		List<String> events = new ArrayList<>();
		events.add("DOC_TASK_COMPLETE_E1");
		//events.add("PROG_TASK_COMPLETE_E2");

		if (events != null) {
			for (String event : events) {
				stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(event).build())).blockLast();
			}
		}

		stateMachine.stopReactively().block();
		
	}
	
	@PostMapping("/tasked")
	public void tasks(
			@RequestParam String task,
			@RequestParam String event
			) {
		
		
		String currentState = "";
		String machineId = "machine_id2";
		String taskName = task;
		
		logger.info("trigger the event");
		state();
		
		//get state for that machine
		List<JpaRepositoryState> stateList = (List<JpaRepositoryState>) stateRepository.findByMachineId("machine_id2");
		
		for(JpaRepositoryState stateEntity : stateList) {
			currentState = stateEntity.getState();
		}
		
		//update the task table
		Tasks taskobj = new Tasks();
		taskobj.setCurrentstate(currentState);
		taskobj.setMachineid(machineId);
		taskobj.setTaskname(taskName);
		
		taskRepo.save(taskobj);
		
		//reset that state machine table
		
		List<JpaRepositoryStateMachine> jpaRepoStateMachhineList = (List<JpaRepositoryStateMachine>)jpaStateMachineRepository.findAll();
		for(JpaRepositoryStateMachine jpaRepoState: jpaRepoStateMachhineList) {
			System.out.println(jpaRepoState.getMachineId());
		}
		
		//System.out.println("======="+task+event+machineId);
	}
	
	@PostMapping("/task")
	//@Transactional
	public void getTasks(
			@RequestBody TaskInput request) throws Exception {
		
		String machineId = "machine_id2";
		String taskName = request.getTaskName();
		List<String> events = request.getEvent();
		String taskState = "";

		//checking if any entry present in the task table for that particular task.
		
		//Optional<Tasks> taskOptional =taskRepo.findByTasknameAndMachineid(taskName, machineId);
		/*
		if(taskOptional.isPresent()) {
			
			//task with the current name already present			
			Tasks task = taskOptional.get();
			taskState = task.getCurrentstate();
			
			//then update the taskstate in the state machine table against the machineid
			//as machine id is unique only one entry would be present there.
			
			Optional<JpaRepositoryStateMachine> optionalStateMachine = stateMachineRepo.findByMachineId(machineId);
			
			
			if(optionalStateMachine.isPresent()) {
				
				JpaRepositoryStateMachine jpaRepositoryStateMachine = optionalStateMachine.get();
				jpaRepositoryStateMachine.setState(taskState);
				
				stateMachineRepo.save(jpaRepositoryStateMachine);			
			}			
		}
		*/
		//trigger the events			
		triggerEvent(events, machineId);
		
		//getting changed state from the state machine table
		
		/*
		String updatedState = "";
		Optional<JpaRepositoryStateMachine> optionalStateMachine = stateMachineRepo.findByMachineId(machineId);		
		
		if(optionalStateMachine.isPresent()) {			
			JpaRepositoryStateMachine jpaRepositoryStateMachine = optionalStateMachine.get();
			updatedState = jpaRepositoryStateMachine.getState();								
		}
		
		//Adding entry in task table
		
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
		
		//delete all entries from state machine table
		
		*/
		
		//stateMachineService.releaseStateMachine(machineId, false);
		
	}
	
	public void triggerEvent(List<String> events, String machineId) throws Exception {
		logger.info("--events---  {} ",events.toString());
		
		//StateMachine<String, String> stateMachine = stateMachineFactory.getStateMachine(machineId);
		StateMachine<String, String> stateMachine = getStateMachine(machineId);
		System.out.println("----State machine state---"+stateMachine.getId());
		
		
		/*
		StateMachineLogListener listener = new StateMachineLogListener();
		stateMachine.addStateListener(listener);
		//stateMachine.start();
		stateMachine.startReactively().block();		

		if (events != null) {
			for (String event : events) {
				stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(event).build())).blockLast();				
			}
		}
		
		//stateMachine.stopReactively().block();	
		 * 	
		 */
		
		
		if (events != null) {
			for (String event : events) {
				stateMachine
					.sendEvent(Mono.just(MessageBuilder
						.withPayload(event).build()))
					.blockLast();
			}
		}
	}
	
	private synchronized StateMachine<String, String> getStateMachine(String machineId) throws Exception {
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
		return currentStateMachine;
	}


}
