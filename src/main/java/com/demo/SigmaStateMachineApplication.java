package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
public class SigmaStateMachineApplication implements CommandLineRunner{
	
	
	@Autowired
	private StateMachine<String, String> stateMachine;
	
	
	public static void main(String[] args) {
		SpringApplication.run(SigmaStateMachineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		//stateMachine.start();
		//stateMachine.sendEvent("DOC_TASK_COMPLETE_E1");
		stateMachine.sendEvent("PROG_TASK_COMPLETE_E2");
	}

}
