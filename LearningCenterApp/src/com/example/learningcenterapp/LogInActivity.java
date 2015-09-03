package com.example.learningcenterapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LogInActivity extends Activity implements OnClickListener {
	private String mUserName;
	private String mPassword;
	private EditText mUserEditTextView;
	private EditText mPasswordEditTextView;
	private Button mSignInButton;
	private Button mSignUpButton;
	public DataSnapshot mSnapShot;
	public static final String KEY_USERNAME = "KEY_USERNAME";
	public static final String KEY_PASSWORD = "KEY_PASSWORD";
	private static final int REQUEST_CODE_USER_PASSWORD = 1;
	private static final String TAG = "LearningCenter";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(this);
		setContentView(R.layout.activity_log_in);
		mUserEditTextView = (EditText) findViewById(R.id.edittext_log_in_username);
		mPasswordEditTextView = (EditText) findViewById(R.id.edittext_log_in_password);
		mUserName = mUserEditTextView.getText().toString();
		mPassword = mPasswordEditTextView.getText().toString();

		mSignInButton = (Button) findViewById(R.id.button_login_sign_in);
		mSignUpButton = (Button) findViewById(R.id.button_login_sign_up);
		mSignInButton.setOnClickListener(this);
		mSignUpButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_login_sign_in:
			mUserName = mUserEditTextView.getText().toString();
			mPassword = mPasswordEditTextView.getText().toString();
			// TODO:
			// need to add case to check if the database contain the student or
			// not
			if (mUserName.isEmpty() || mPassword.isEmpty()) {
				Toast.makeText(this, "Please Enter Information",
						Toast.LENGTH_LONG).show();
			} else {
				Firebase mFirebase = new Firebase(
						"https://learning-center-app.firebaseio.com/Users/"
								+ mUserName + "/Password");
				mFirebase.addValueEventListener(new ValueEventListener() {

					@Override
					public void onDataChange(DataSnapshot snapShot) {
						// mSnapShot = snapShot;

						if (snapShot.exists()) {
							String checkPass = (String) snapShot.getValue()
									.toString();

							if (checkPass.equals(mPassword)) {
								if (mUserName.equals("Administrator")) {
									Intent adminiIntent = new Intent(
											getBaseContext(),
											AdministratorActivity.class);
									startActivity(adminiIntent);

								} else {
									Intent calenderIntent = new Intent(
											getBaseContext(),
											CalendarActivity.class);
									calenderIntent.putExtra(KEY_USERNAME, mUserName);
									startActivity(calenderIntent);
								}
							} else {
								Toast.makeText(getBaseContext(),
										"Incorrect Password", Toast.LENGTH_LONG)
										.show();

							}
						} else
							Toast.makeText(
									getBaseContext(),
									"You have entered an unrecognized username.",
									Toast.LENGTH_LONG).show();
					}

					@Override
					public void onCancelled(FirebaseError arg0) {
						// NOTHING HERE
					}
				});
				break;
			}

		case R.id.button_login_sign_up:
			Intent signUpIntent = new Intent(this, SignUpActivity.class);
			startActivityForResult(signUpIntent, REQUEST_CODE_USER_PASSWORD);
			break;

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_USER_PASSWORD
				&& resultCode == RESULT_OK) {
			mUserName = data.getStringExtra(KEY_USERNAME);
			mPassword = data.getStringExtra(KEY_PASSWORD);
			mUserEditTextView.setText(mUserName);

		}
	}
}
