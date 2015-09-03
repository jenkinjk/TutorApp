package com.example.learningcenterapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TutorScheduleRow extends LinearLayout{
	
	private TextView mMajor;
	private TextView mName;
	private TextView mTime;
	
	
	public TutorScheduleRow(Context context){
		super(context);
		LayoutInflater.from(context).inflate(R.layout.tutor_schedule_row_view, this);
		mName = (TextView) findViewById(R.id.tutor_scheduel_name_text_view);
		mMajor  = (TextView) findViewById(R.id.tutor_schedule_major_text_view);
		mTime = (TextView) findViewById(R.id.tutor_schedule_time_text_view);
		
	}
	
	public void setNameText(String name){
		mName.setText(name);
	}
	
	public void setMajorText(String major){
		mMajor.setText(major);
	}
	
	public void setTimeText(String time){
		mTime.setText(time);
	}
}
