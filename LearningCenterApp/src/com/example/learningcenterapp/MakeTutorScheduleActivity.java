package com.example.learningcenterapp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.firebase.client.Firebase;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class MakeTutorScheduleActivity extends Activity implements
		OnClickListener {
	private static final String TAG = "LearningCenter";
	private String tutorName;
	private DatePicker chosenDate;
	private TimePicker startTime;
	private TimePicker endTime;
	private Button commitButton;
	private String tutorScheDate;
	private String tutorScheTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_tutor_schedule);

		tutorName = this.getIntent().getStringExtra(
				MakeScheduleActivity.TUTOR_NAME);

		chosenDate = (DatePicker) findViewById(R.id.make_tutor_schedule_date_picker);
		startTime = (TimePicker) findViewById(R.id.make_tutor_schedule_start_time);
		endTime = (TimePicker) findViewById(R.id.make_tutor_schedule_end_time);
		commitButton = (Button) findViewById(R.id.make_tutor_schedule_button);

		// Toast.makeText(this, chosenDate.toString(),
		// Toast.LENGTH_LONG).show();

		commitButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		//Date
		StringBuilder dateSb = new StringBuilder();
		dateSb.append(chosenDate.getYear()).append("-")
				.append(chosenDate.getMonth() + 1).append("-")
				.append(chosenDate.getDayOfMonth());
		tutorScheDate = dateSb.toString();
		
		//Time
		StringBuilder timeSb = new StringBuilder();
		timeSb.append(
				String.format("%02d:%02d", startTime.getCurrentHour(),
						startTime.getCurrentMinute()))
				.append("-")
				.append(String.format("%02d:%02d", endTime.getCurrentHour(),
						endTime.getCurrentMinute()));

		tutorScheTime = timeSb.toString();
		
		Firebase mFirebase = new Firebase(
				"https://learning-center-app.firebaseio.com/Users/" + tutorName
						+ "/IsATutor/Schedule");
		
		
		
		//Data construction
		Map<String, Object> newDate = new HashMap<String, Object>();
		Map<String, Object> newTime = new HashMap<String, Object>();
		Map<String,String> timeSlot= new HashMap<String, String>();
		
		int index = 0;
		int counter = startTime.getCurrentHour();
		int run = endTime.getCurrentHour()-counter;
		String[] minutes = new String[]{"00", "15", "30", "45"};
		//Algorithm for assembling time slots
		if(startTime.getCurrentMinute()!= 0){
			index = 2;
		}
		for(int i =0; i< run; i++){
			for(int j=index;j<4;j++){
				timeSlot.put(counter+":"+minutes[j], "Available");
			}
			index = 0;
			counter++;
		}
		if(startTime.getCurrentMinute()!= 0){
			timeSlot.put(counter+":00", "Available");
			timeSlot.put(counter+":15", "Available");
		}
		
		
		newTime.put(tutorScheTime, timeSlot);
		
		
		newDate.put(tutorScheDate, newTime);
		mFirebase.updateChildren(newDate);

		
		
		
		Toast.makeText(this, "commited", Toast.LENGTH_LONG).show();

		// Toast.makeText(this, chosenDate.getYear(), Toast.LENGTH_LONG).show();

	}
}
