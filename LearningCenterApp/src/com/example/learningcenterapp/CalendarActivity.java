package com.example.learningcenterapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;
//import android.widget.Toast;

public class CalendarActivity extends Activity {
	private static final String TAG = "LearningCenter";
	public static final String PICKED_DATE = "PICKED_DATE";
	private CalendarView mCalendar;
	private String currentUser;
	private long date;
	private String pickedDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		setTitle(R.string.calendar_title);
		currentUser = getIntent().getStringExtra(LogInActivity.KEY_USERNAME);
		mCalendar = (CalendarView) findViewById(R.id.calendarView);
		date = mCalendar.getDate();
		mCalendar.setOnDateChangeListener(new OnDateChangeListener() {
			
			

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				if (mCalendar.getDate()!=date) {
					date = mCalendar.getDate();
					pickedDate = "" + year + "-" + (month+1) + "-" + dayOfMonth;
					Intent intent = new Intent(getBaseContext(),
							TutorScheduleActivity.class);
					intent.putExtra(LogInActivity.KEY_USERNAME, currentUser);
					
					intent.putExtra(PICKED_DATE, pickedDate);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.calender_search) {
			// lunch search option
			Intent intent = new Intent(getBaseContext(),
					TutorScheduleActivity.class);
			intent.putExtra(LogInActivity.KEY_USERNAME, currentUser);
			
			intent.putExtra(PICKED_DATE, pickedDate);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
