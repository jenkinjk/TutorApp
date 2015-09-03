package com.example.learningcenterapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MakeTutorActivity extends Activity {
	private static final String TAG = "LearningCenter";
	private ArrayList<String> nodes = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_tutor);
		Firebase.setAndroidContext(this);
		ListView allUserView = (ListView) findViewById(R.id.make_tutor_list_view);

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

						if (!snapShot.hasChild(userName + "/IsATutor")) {
							nodes.add(userName);
							adapter.notifyDataSetChanged();
						}
					}
				}
				nodes.remove("Administrator");
				adapter.notifyDataSetChanged();
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
				showMakeTutor(nodes.get(position));

			}

		});
	}

	void showMakeTutor(final String chosenPerson) {
		DialogFragment df = new DialogFragment() {
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				final View dialogView = LayoutInflater.from(getActivity())
						.inflate(R.layout.dialog_make_tutor, null);
				builder.setView(dialogView)
						.setTitle(R.string.make_tutor)
						.setPositiveButton(R.string.make_tutor,
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										EditText TopicGetter = (EditText) dialogView
												.findViewById(R.id.topic);
										if (TopicGetter != null) {
											String Topic = TopicGetter
													.getText().toString();
											Firebase mFirebase = new Firebase(
													"https://learning-center-app.firebaseio.com/Users/"
															+ chosenPerson);

											HashMap<String, Object> topData = new HashMap<String, Object>();
											HashMap<String, String> data = new HashMap<String, String>();
											topData.put("IsATutor", data);
											data.put("Topic", Topic);
											data.put("Schedule", "None");
											data.put("Evaulations", "None");
											mFirebase.updateChildren(topData);
										} else {
											Toast.makeText(getBaseContext(),
													"Failed",
													Toast.LENGTH_SHORT).show();
										}
										dialog.dismiss();
										nodes = new ArrayList<String>();
									}

								})
						.setNegativeButton(android.R.string.cancel,
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();

									}
								});
				return builder.create();
			}
		};
		df.show(getFragmentManager(), "");
	}
	
}
