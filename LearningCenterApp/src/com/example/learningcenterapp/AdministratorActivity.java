package com.example.learningcenterapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AdministratorActivity extends Activity implements OnClickListener {
	private Button mMakeTutorButton;
	private Button mMakeScheduleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_administrator);

		mMakeScheduleButton = (Button) findViewById(R.id.make_schedule_button);
		mMakeTutorButton = (Button) findViewById(R.id.make_tutor_button);
		mMakeScheduleButton.setOnClickListener(this);
		mMakeTutorButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.make_tutor_button:
			Intent makeTutorIntent = new Intent(this, MakeTutorActivity.class);
			startActivity(makeTutorIntent);
			break;
		case R.id.make_schedule_button:
			Intent makeScheduleIntent = new Intent(this,
					MakeScheduleActivity.class);
			startActivity(makeScheduleIntent);
			break;
		}
	}

}
