package com.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.TransitionRepository;

import reactor.core.publisher.Mono;

@EntityScan
@EnableJpaRepositories
@SpringBootApplication
public class SigmaStateMachineApplication implements CommandLineRunner {

	@Autowired
	StateMachineFactory<String, String> stateMachineFactory;

	@Autowired
	private TransitionRepository<? extends RepositoryTransition> transitionRepository;

	public static void main(String[] args) {
		SpringApplication.run(SigmaStateMachineApplication.class, args);
	}
	
	
	@Override
	public void run(String... args) throws Exception {
		/*
		StateMachine<String, String> stateMachine = stateMachineFactory.getStateMachine("machine_id2");
		StateMachineLogListener listener = new StateMachineLogListener();
		stateMachine.addStateListener(listener);
		stateMachine.startReactively().block();

		List<String> events = new ArrayList<>();
		//events.add("DOC_TASK_COMPLETE_E1");
		events.add("PROG_TASK_COMPLETE_E2");

		if (events != null) {
			for (String event : events) {
				stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(event).build())).blockLast();
			}
		}

		stateMachine.stopReactively().block();
		*/
	}
	
}
