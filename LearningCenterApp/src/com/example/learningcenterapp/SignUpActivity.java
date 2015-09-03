package com.example.learningcenterapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class SignUpActivity extends Activity implements OnClickListener {
	private String mNewUserName;
	private String mNewPassword;
	private String mConfirmPassword;
	private String mName;
	private Button mNewUserSignUpButton;
	private Button mGoBackButton;
	private Firebase mFirebase;
	private ArrayList<String> takenUsers = new ArrayList<String>();
	private DataSnapshot values;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(this);
		this.mFirebase = new Firebase(
				"https://learning-center-app.firebaseio.com/Users");
		setContentView(R.layout.activity_sign_up);
		mNewUserName = ((EditText) findViewById(R.id.edittext_sign_up_username))
				.getText().toString();
		mName = ((EditText) findViewById(R.id.edittext_sign_up_justname))
				.getText().toString();
		mNewPassword = ((EditText) findViewById(R.id.edittext_sign_up_createPass))
				.getText().toString();
		mConfirmPassword = ((EditText) findViewById(R.id.edittext_sign_up_confirmPass))
				.getText().toString();
		mNewUserSignUpButton = (Button) findViewById(R.id.button_signup_sign_up);
		mGoBackButton = (Button) findViewById(R.id.button_signup_go_back);
		mNewUserSignUpButton.setOnClickListener(this);
		mGoBackButton.setOnClickListener(this);
		mFirebase.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapShot) {
				for (DataSnapshot t : snapShot.getChildren()) {
					String username = t.getKey().toString();
					if (!takenUsers.contains(username)) {
						takenUsers.add(username);
						Log.d("SignUp", username);
					}
				}

			}

			@Override
			public void onCancelled(FirebaseError arg0) {
				// Nothing here
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent returnIntent = new Intent();
		switch (v.getId()) {
		case R.id.button_signup_sign_up:
			// log user into database
			mNewUserName = ((EditText) findViewById(R.id.edittext_sign_up_username))
					.getText().toString();
			mName = ((EditText) findViewById(R.id.edittext_sign_up_justname))
					.getText().toString();
			mNewPassword = ((EditText) findViewById(R.id.edittext_sign_up_createPass))
					.getText().toString();
			mConfirmPassword = ((EditText) findViewById(R.id.edittext_sign_up_confirmPass))
					.getText().toString();
			if (!mNewPassword.equals(mConfirmPassword))
				Toast.makeText(this, "Password didn't match", Toast.LENGTH_LONG)
						.show();
			else if (mNewUserName.equals("") || mNewPassword.equals("")
					|| mName.equals("")) {
				Toast.makeText(this, "You must fill out all the fields.",
						Toast.LENGTH_LONG).show();

			} else if (takenUsers.contains(mNewUserName)) {
				Toast.makeText(this,
						"The username you selected has been taken already.",
						Toast.LENGTH_LONG).show();
			} else {

				// password confirmed and update firebase

				Firebase mFirebase = new Firebase(
						"https://learning-center-app.firebaseio.com/Users/"
								+ mNewUserName);

				Map<String, String> newID = new HashMap<String, String>();
				newID.put("Name", mName);
				newID.put("Password", mNewPassword);
				mFirebase.setValue(newID);
				Toast.makeText(this, "You have been signed up sucessfully.",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.button_signup_go_back:
			// intent need to put extra for username / password
			mNewUserName = ((EditText) findViewById(R.id.edittext_sign_up_username))
					.getText().toString();
			mNewPassword = ((EditText) findViewById(R.id.edittext_sign_up_createPass))
					.getText().toString();
			mConfirmPassword = ((EditText) findViewById(R.id.edittext_sign_up_confirmPass))
					.getText().toString();
			returnIntent.putExtra(LogInActivity.KEY_USERNAME, mNewUserName);
			if (mNewPassword.equals(mConfirmPassword)) {
				returnIntent.putExtra(LogInActivity.KEY_PASSWORD, mNewPassword);
				setResult(RESULT_OK, returnIntent);
				finish();
			} else {
				Toast.makeText(this, "Password didn't match", Toast.LENGTH_LONG)
						.show();
			}
			break;
		}

	}
}
