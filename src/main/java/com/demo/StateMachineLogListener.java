package com.demo;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateContext.Stage;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

public class StateMachineLogListener extends StateMachineListenerAdapter<String, String> {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final LinkedList<String> messages = new LinkedList<String>();

	public List<String> getMessages() {
		return messages;
	}

	public void resetMessages() {
		messages.clear();
	}
	
	
	@Override
	public void stateContext(StateContext<String, String> stateContext) {
		
		if (stateContext.getStage() == Stage.STATE_ENTRY) {
			System.out.println("Enter " + stateContext.getTarget().getId());
		} 
		else if (stateContext.getStage() == Stage.STATE_EXIT) {
			System.out.println("Exit " + stateContext.getSource().getId());
		} 
		else if (stateContext.getStage() == Stage.STATEMACHINE_START) {
			System.out.println("Machine started");
		} 
		else if (stateContext.getStage() == Stage.STATEMACHINE_STOP) {
			System.out.println("Machine stopped");
		}
		
	}	
	
	
	@Override
    public void stateChanged(State from, State to) {
		logger.info("StateMachineLogListener :: stateChanged :: Start");		
		//System.out.printf("Transitioned from %s to %s%n", from == null ? "none" : from.getId(), to.getId());
		logger.info("Transitioned from {} to {}", from == null ? "none" : from.getId(), to.getId());		
		logger.info("StateMachineLogListener :: stateChanged :: End");
    }
}