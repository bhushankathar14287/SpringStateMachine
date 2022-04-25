package com.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigBuilder;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.common.annotation.AnnotationBuilder;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.config.model.TransitionData;
import org.springframework.statemachine.data.RepositoryState;
import org.springframework.statemachine.data.RepositoryStateMachineModelFactory;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.support.StateMachineJackson2RepositoryPopulatorFactoryBean;

@Configuration
//@EnableStateMachineFactory
@EnableStateMachine

public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String>{
	
	
	@Autowired
	private StateRepository<? extends RepositoryState> stateRepository;

	@Autowired
	private TransitionRepository<? extends RepositoryTransition> transitionRepository;
	
	@Override
	public void configure(StateMachineModelConfigurer<String, String> model) throws Exception {
		model
			.withModel()
				.factory(modelFactory());
	}
	
	@Override
	public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
		// TODO Auto-generated method stub
		super.configure(config);
		config
			.withConfiguration()
			.autoStartup(true)
			.listener(new StateMachineListener());
	}

	@Bean
	public StateMachineModelFactory<String, String> modelFactory() {
		return new RepositoryStateMachineModelFactory(stateRepository, transitionRepository);
		
	}
	
	@Bean
	public StateMachineJackson2RepositoryPopulatorFactoryBean jackson2RepositoryPopulatorFactoryBean() {
	    StateMachineJackson2RepositoryPopulatorFactoryBean factoryBean = new StateMachineJackson2RepositoryPopulatorFactoryBean();
	    factoryBean.setResources(new Resource[]{new ClassPathResource("sigma_data.json")});
	    return factoryBean;
	}

	@Override
	public void configure(StateMachineConfigBuilder<String, String> config) throws Exception {
		// TODO Auto-generated method stub
		super.configure(config);
	}

	@Override
	public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
		// TODO Auto-generated method stub
		super.configure(states);
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
		// TODO Auto-generated method stub
		super.configure(transitions);
	}

	@Override
	public boolean isAssignable(
			AnnotationBuilder<org.springframework.statemachine.config.StateMachineConfig<String, String>> builder) {
		// TODO Auto-generated method stub
		return super.isAssignable(builder);
	}
	
	

}
