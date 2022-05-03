package com.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tasks")
public class Tasks {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long taskid;
	private String taskname;
	private String machineid;
	private String currentstate;	
	
	public Tasks() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tasks(Long taskid, String taskname, String machineid, String currentstate) {
		super();
		this.taskid = taskid;
		this.taskname = taskname;
		this.machineid = machineid;
		this.currentstate = currentstate;
	}
	
	public Long getTaskid() {
		return taskid;
	}
	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public String getMachineid() {
		return machineid;
	}
	public void setMachineid(String machineid) {
		this.machineid = machineid;
	}
	public String getCurrentstate() {
		return currentstate;
	}
	public void setCurrentstate(String currentstate) {
		this.currentstate = currentstate;
	}

	@Override
	public String toString() {
		return "Tasks [taskid=" + taskid + ", taskname=" + taskname + ", machineid=" + machineid + ", currentstate="
				+ currentstate + "]";
	}
	

}
