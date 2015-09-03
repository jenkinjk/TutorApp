package com.example.learningcenterapp;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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

public class MakeScheduleActivity extends Activity {
	private static final String TAG = "LearningCenter";
	private ArrayList<String> nodes = new ArrayList<String>();
	public static final String TUTOR_NAME = "TUTOR_NAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_schedule);
		Firebase.setAndroidContext(this);
		ListView allUserView = (ListView) findViewById(R.id.make_schedule_list_view);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, nodes);

		allUserView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		// add from database
		Firebase mFirebase = new Firebase(
				"https://learning-center-app.firebaseio.com/Users/");

		mFirebase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapShot) {
				Iterator<DataSnapshot> it = snapShot.getChildren().iterator();
				while (it.hasNext()) {
					String userName = it.next().getKey().toString();
					if (userName != "Administrator") {
						try {
							if (!(snapShot.child(userName + "/IsATutor")
									.getValue()).equals(null)) {
								nodes.add(userName);
								adapter.notifyDataSetChanged();
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

		allUserView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(getBaseContext(),
				// "You chose: " + nodes.get(position), Toast.LENGTH_LONG)
				// .show();
				showMakeSchedule(nodes.get(position));
			}

		});
	}

	void showMakeSchedule(final String chosenPerson) {
		DialogFragment df = new DialogFragment() {
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				AlertDialog.Builder makeTutor = new AlertDialog.Builder(
						getActivity());
				makeTutor.setMessage(R.string.make_schedule_description);
				makeTutor.setPositiveButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				makeTutor.setNegativeButton(R.string.make_schedule,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent makeScheduleIntent = new Intent(
										getBaseContext(),
										MakeTutorScheduleActivity.class);
								makeScheduleIntent.putExtra(TUTOR_NAME,
										chosenPerson);
								startActivity(makeScheduleIntent);
								nodes = new ArrayList<String>();
							}
						});

				return makeTutor.create();
			};
		};
		df.show(getFragmentManager(), "");
	}
}
