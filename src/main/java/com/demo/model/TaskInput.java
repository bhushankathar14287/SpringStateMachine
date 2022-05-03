package com.demo.model;

import java.util.List;

public class TaskInput {
	
	String taskName;
	List<String> event;
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public List<String> getEvent() {
		return event;
	}
	public void setEvent(List<String> event) {
		this.event = event;
	}
	
	@Override
	public String toString() {
		return "TaskInput [taskName=" + taskName + ", event=" + event + "]";
	}
	
	
	
	

}
