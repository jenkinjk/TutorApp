package com.example.learningcenterapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class TutorScheduleActivity extends Activity {
	private ListView mTutorListView;
	private static final String TAG = "LearningCenter";
	private String tutorString = "";
	public static final String TIMES = "Times";
	protected static final int ResultTag = 1;
	private String currentUser;
	private ArrayList<String> tutors =  new ArrayList<String>();
	protected HashMap<String, ArrayList<String>> times;
	public static ArrayAdapter<String> adapter;
	public ArrayList<String> tutorUserNames = new ArrayList<String>() ;
	private String pickedDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(this);
		
		setTitle(R.string.tutor_schedule_title);
		setContentView(R.layout.activity_tutor_schedule);
		pickedDate = this.getIntent().getStringExtra(
				CalendarActivity.PICKED_DATE);
		
		mTutorListView = (ListView) findViewById(R.id.tutor_list);
		
		currentUser = getIntent().getStringExtra(LogInActivity.KEY_USERNAME);
		setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, tutors));
		mTutorListView.setAdapter(getAdapter());
		getAdapter().notifyDataSetChanged();
		
		// add from database
		final Firebase mFirebase = new Firebase(
				"https://learning-center-app.firebaseio.com/Users/");

		mFirebase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapShot) {
				times = new HashMap<String, ArrayList<String>>();
				Iterator<DataSnapshot> it = snapShot.getChildren().iterator();
				while (it.hasNext()) {
					String userName = it.next().getKey().toString();

					if (userName != "Administrator") {
						try {
							Iterator<DataSnapshot> dateIt = snapShot
									.child(userName + "/IsATutor/Schedule")
									.getChildren().iterator();

							while (dateIt.hasNext()) {
								tutorString = "";
								if (dateIt.next().getKey().equals(pickedDate)) {
									// name of tutor
									String name = snapShot.child(userName+"/Name").getValue().toString();
									// user name
									tutorString += name + "          ";
									tutorUserNames.add(userName);
									// topic
									tutorString += snapShot
											.child(userName + "/IsATutor/Topic")
											.getValue().toString()
											+ "          ";
									// working hour

									Iterator<DataSnapshot> hourIt = snapShot
											.child(userName
													+ "/IsATutor/Schedule/"
													+ pickedDate).getChildren()
											.iterator();
									tutorString += hourIt.next().getKey()
											.toString();
									// TimeSlot assembly
									Iterable<DataSnapshot> kids = snapShot
											.child(userName
													+ "/IsATutor/Schedule/"
													+ pickedDate).getChildren();
									Iterable<DataSnapshot> slots = kids
											.iterator().next().getChildren();
									ArrayList<String> studentSlots = new ArrayList<String>();
									Log.d(TAG, tutorString
											+ " is the tutor string");
									for (DataSnapshot slot : slots) {
										StudentSlot toBeAdded = new StudentSlot(
												slot.getValue().toString(),
												slot.getKey());
										studentSlots.add(toBeAdded.sName + "/"
												+ toBeAdded.sTime);
									}
									Log.d(TAG, studentSlots.toString());

									times.put(userName, studentSlots);
									tutors.add(tutorString);
									getAdapter().notifyDataSetChanged();
									Log.d(TAG, userName + " works on this day.");
								}
							}

						} catch (Exception name) {
							Log.d(TAG, userName + " is not a tutor");
						}
					}
				}
			}

			@Override
			public void onCancelled(FirebaseError arg0) {
				// Nothing here
			}
		});

		
		mTutorListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent specificTutorIntent = new Intent(getBaseContext(),
						SpecificTutorActivity.class);
				String[] thisTutor = tutors.get(position).split(" ");
				specificTutorIntent.putExtra(LogInActivity.KEY_USERNAME,
						currentUser);
				specificTutorIntent.putExtra(TIMES, times.get(tutorUserNames.get(position)));
				Log.d(TAG, "Launching Intent");
				startActivityForResult(specificTutorIntent, position);
				tutors = new ArrayList<String>();
			}

		});
		getAdapter().notifyDataSetChanged();
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			ArrayList<String> stringData = data
					.getStringArrayListExtra(TutorScheduleActivity.TIMES);
			String time, name;
			HashMap<String, String> firebaseData = new HashMap<String, String>();
			// Reassemble data
			for (String both : stringData) {
				String[] split = both.split("/");
				name = split[0];
				time = split[1];
				firebaseData.put(time, name);
			}
			String[] thisTutor = tutors.get(requestCode).split(" ");
			String chosenUserName = this.tutorUserNames.get(requestCode);
			Log.d(TAG, "Jonathan tutor  IS "+ thisTutor[0] + "while i have" + chosenUserName );
			Firebase mFirebase = new Firebase(
					"https://learning-center-app.firebaseio.com/Users/"
							+ chosenUserName  + "/IsATutor/Schedule/" + pickedDate
							+ "/" + thisTutor[2]);
			mFirebase.setValue(firebaseData);
			// Go back to calendar
			finish();
		} else {
			Toast.makeText(
					this,
					"Something went wrong while signing up for a slot. No changes were saved.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public static ArrayAdapter<String> getAdapter() {
		return adapter;
	}

	public static void setAdapter(ArrayAdapter<String> adapterer) {
		adapter = adapterer;
	}
}
