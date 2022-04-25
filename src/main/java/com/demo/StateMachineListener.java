package com.demo;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

public class StateMachineListener extends StateMachineListenerAdapter<String, String>{

	@Override
	public void stateChanged(State<String, String> from, State<String, String> to) {
		// TODO Auto-generated method stub
		super.stateChanged(from, to);
	
			System.out.println("------ stateChanged ------from :  -----to : "+to.getIds());
	
		
	}

	@Override
	public void stateEntered(State<String, String> state) {
		// TODO Auto-generated method stub
		super.stateEntered(state);
		System.out.println("------ stateEntered ------");
	}

	@Override
	public void stateExited(State<String, String> state) {
		// TODO Auto-generated method stub
		super.stateExited(state);
		System.out.println("------ stateExited ------");
	}

	@Override
	public void eventNotAccepted(Message<String> event) {
		// TODO Auto-generated method stub
		super.eventNotAccepted(event);
		System.out.println("------ eventNotAccepted ------");
	}

	@Override
	public void transition(Transition<String, String> transition) {
		// TODO Auto-generated method stub
		super.transition(transition);
		System.out.println("------ transition ------");
	}

	@Override
	public void transitionStarted(Transition<String, String> transition) {
		// TODO Auto-generated method stub
		super.transitionStarted(transition);
		System.out.println("------ transitionStarted ------");
	}

	@Override
	public void transitionEnded(Transition<String, String> transition) {
		// TODO Auto-generated method stub
		super.transitionEnded(transition);
		System.out.println("------ transitionEnded ------");
	}

	@Override
	public void stateMachineStarted(StateMachine<String, String> stateMachine) {
		// TODO Auto-generated method stub
		super.stateMachineStarted(stateMachine);
		System.out.println("------ stateMachineStarted ------");
	}

	@Override
	public void stateMachineStopped(StateMachine<String, String> stateMachine) {
		// TODO Auto-generated method stub
		super.stateMachineStopped(stateMachine);
		System.out.println("------ stateMachineStopped ------");
	}

	@Override
	public void stateMachineError(StateMachine<String, String> stateMachine, Exception exception) {
		// TODO Auto-generated method stub
		super.stateMachineError(stateMachine, exception);
		System.out.println("------ stateMachineError ------");
	}

	@Override
	public void extendedStateChanged(Object key, Object value) {
		// TODO Auto-generated method stub
		super.extendedStateChanged(key, value);
		System.out.println("------ extendedStateChanged ------");
	}

	@Override
	public void stateContext(StateContext<String, String> stateContext) {
		// TODO Auto-generated method stub
		super.stateContext(stateContext);
		System.out.println("------ stateContext ------");
	}
	
	

}
