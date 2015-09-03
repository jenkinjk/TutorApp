package com.example.learningcenterapp;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class PeerTutorEvalForm extends Activity implements OnClickListener {
	private static final String TAG = "LearningCenter";
	private HashMap<String, String> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.peer_tutor_evaluation_form);
		setContentView(R.layout.activity_peer_tutor_eval_form);
		Firebase.setAndroidContext(this);
		Button commit = (Button) findViewById(R.id.peer_tutor_commit);
		commit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		try{
		
		
		EditText peerTutorStudent = (EditText) findViewById(R.id.peer_tutor_student);
		EditText peerTutorClassYear = (EditText) findViewById(R.id.peer_tutor_class_year);
		EditText peerTutorDate = (EditText) findViewById(R.id.peer_tutor_date);
		EditText peerTutorTime = (EditText) findViewById(R.id.peer_tutor_time);
		EditText peerTutorProf = (EditText) findViewById(R.id.peer_tutor_professor);
		EditText peerTutorCourse = (EditText) findViewById(R.id.peer_tutor_cour);
		EditText peerTutorHelpNeeded = (EditText) findViewById(R.id.peer_tutor_help_needed);
		EditText peerTutorAttitude = (EditText) findViewById(R.id.peer_tutor_attitude_of_tutee);
		EditText peerTutorTutorID = (EditText) findViewById(R.id.peer_tutor_tutor_username);
		
		if(peerTutorStudent.getText().toString().isEmpty()
				||peerTutorClassYear.getText().toString().isEmpty()
				||peerTutorDate.getText().toString().isEmpty()
				||peerTutorTime.getText().toString().isEmpty()
				||peerTutorProf.getText().toString().isEmpty()
				||peerTutorCourse.getText().toString().isEmpty()
				||peerTutorHelpNeeded.getText().toString().isEmpty()
				||peerTutorAttitude.getText().toString().isEmpty()
				||peerTutorTutorID.getText().toString().isEmpty()){
			Toast.makeText(getBaseContext(), "Input all required fields", Toast.LENGTH_SHORT).show();
		}
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Student", peerTutorStudent.getText().toString());
		data.put("Year", peerTutorClassYear.getText().toString());
		data.put("Date", peerTutorDate.getText().toString());
		data.put("Time", peerTutorTime.getText().toString());
		data.put("Professor", peerTutorProf.getText().toString());
		data.put("Course", peerTutorCourse.getText().toString());
		data.put("Help Needed", peerTutorHelpNeeded.getText().toString());
		data.put("Attitude", peerTutorAttitude.getText().toString());
		data.put("Tutor", peerTutorTutorID.getText().toString());
		this.data = data;
		Firebase mFirebase = new Firebase(
				"https://learning-center-app.firebaseio.com/Users/"
						+ peerTutorTutorID.getText().toString() + "/IsATutor/Evaluations");
		mFirebase = mFirebase.push();
		mFirebase.setValue(data);
		Toast.makeText(this, "You have submitted a evaluation", Toast.LENGTH_LONG);
		}catch(Exception e){
			Toast.makeText(this, "Improper username", Toast.LENGTH_LONG).show();
		}

	}
}