package com.example.learningcenterapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class SpecificTutorActivity extends Activity implements OnClickListener {

	private SlotSignUpArrayAdapter adapter;
	private ArrayList<StudentSlot> students = null;
	private ListView mTutorScheduleList;
	private String currentUser;
	private ArrayList<String> times;
	private static final String TAG = "LearningCenter";
	private String realName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(this);
		Log.d(TAG, "Launched activity");
		setTitle(R.string.specific_tutor_title);
		setContentView(R.layout.activity_specific_tutor);
		Button backButton = (Button) findViewById(R.id.sign_up_slot_back);
		backButton.setOnClickListener(this);
		students = new ArrayList<StudentSlot>();
		currentUser = getIntent().getStringExtra(LogInActivity.KEY_USERNAME);
		times = getIntent()
				.getStringArrayListExtra(TutorScheduleActivity.TIMES);
		String time, name;
		for (String both : times) {
			String[] split = both.split("/");
			time = split[0];
			name = split[1];
			students.add(new StudentSlot(time, name));
		}
		mTutorScheduleList = (ListView) findViewById(R.id.tutor_schedule_list);

		adapter = new SlotSignUpArrayAdapter(this, students);
		mTutorScheduleList.setAdapter(adapter);
		Firebase getNameFire = new Firebase(
				"https://learning-center-app.firebaseio.com/Users/"
						+ currentUser);

		getNameFire.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapShot) {
				for (DataSnapshot t : snapShot.getChildren()) {
					if (t.getKey().equals("Name")) {
						realName = t.getValue().toString();
					}
				}
			}

			@Override
			public void onCancelled(FirebaseError arg0) {
				// Do Nothing
			}
		});

		mTutorScheduleList
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						showDeleteNotification(position);
						return true;
					}
				});
		mTutorScheduleList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (students.get(position).sName.equals("Available"))
					showAddingNotification(position);
				else
					Toast.makeText(
							getBaseContext(),
							"This slot has already been taken by "
									+ students.get(position).sName,
							Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void showAddingNotification(final int position) {

		final int addedPos = position;

		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle(R.string.new_appointment);
				Resources res = getResources();
				builder.setMessage(String.format(
						res.getString(R.string.add_new_slot_description),
						students.get(addedPos).sTime));
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								students.get(addedPos).sName = realName;
								adapter.notifyDataSetChanged();

							}

						});
				return builder.create();

			}
		};
		df.show(getFragmentManager(), "");

	}

	private void showDeleteNotification(int position) {
		final int deletePos = position;
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				builder.setTitle(R.string.confirm_deletion);

				Resources res = getResources();
				builder.setMessage(String.format(
						res.getString(R.string.delete_message),
						students.get(deletePos).sTime));
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String temp = students.get(deletePos).sName;
								students.get(deletePos).sName = "Available";
								adapter.notifyDataSetChanged();
								if (!temp.equals(realName)) {
									Toast.makeText(
											getBaseContext(),
											"You can only delete your own appointments!",
											Toast.LENGTH_SHORT).show();
									students.get(deletePos).sName = temp;
									adapter.notifyDataSetChanged();
								}
							}
						});
				return builder.create();

			}
		};
		df.show(getFragmentManager(), "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.specific_tutor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.specific_tutor_notification_test) {
			showEvaluationNotification();
		}
		return super.onOptionsItemSelected(item);
	}

	private void showEvaluationNotification() {
		DialogFragment df = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				builder.setTitle(R.string.evaluation);
				builder.setMessage(R.string.evaluation_message);
				builder.setNegativeButton(R.string.student,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent tutorEvalIntent = new Intent(
										getBaseContext(),
										TutorEvalFormActivity.class);
								startActivity(tutorEvalIntent);
							}
						});
				builder.setNeutralButton(R.string.peer_tutor,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent peerTutorEvalIntent = new Intent(
										getBaseContext(),
										PeerTutorEvalForm.class);
								startActivity(peerTutorEvalIntent);
							}
						});
				builder.setPositiveButton(R.string.esl_tutor_eval,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent eslTutorIntent = new Intent(
										getBaseContext(),
										ESLTutorEvalFormActivity.class);
								startActivity(eslTutorIntent);

							}
						});
				return builder.create();

			}
		};
		df.show(getFragmentManager(), "");
	}

	@Override
	public void onClick(View v) {
		Intent returnIntent = new Intent();
		ArrayList<String> newSlots = new ArrayList<String>();
		for (StudentSlot slot : students) {
			newSlots.add(slot.sName + "/" + slot.sTime);
		}
		returnIntent.putExtra(TutorScheduleActivity.TIMES, newSlots);
		setResult(RESULT_OK, returnIntent);
		TutorScheduleActivity.adapter.notifyDataSetChanged();
		this.finish();
	}

}
