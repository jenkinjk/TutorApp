package com.example.learningcenterapp;


public class StudentSlot {
	String sName;
	String sTime;
	
	public StudentSlot(String time, String name){
		sName = name;
		sTime = time;
	}
	
	@Override
	public String toString() {
		return sName+"-"+sTime;
	}
	
	public String getName(){
		return sName;
	}
	
	public String getTime(){
		return sTime;
	}
	
}
