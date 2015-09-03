package com.example.learningcenterapp;

import java.util.HashMap;

import com.firebase.client.Firebase;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class TutorEvalFormActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.tutor_eval_form);
		Firebase.setAndroidContext(this);
		setContentView(R.layout.activity_tutor_eval_form);
		Button commit = (Button)findViewById(R.id.tutor_eval_commit_button);
		commit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		EditText classYearET = (EditText)findViewById(R.id.edittext_tutor_eval_classyear);
		String clsyr = classYearET.getText().toString();
		CheckBox ESL = (CheckBox)findViewById(R.id.checkBox_esl);
		boolean esl = ESL.isChecked();
		EditText purposeET = (EditText)findViewById(R.id.edittext_tutor_eval_purpose);
		String purpose = purposeET.getText().toString();
		//Note, radio groups should be refactored into a helper method.
		RadioGroup attitudeRG = (RadioGroup)findViewById(R.id.tutor_eval_attitue_radiogroup);
		int attitude = -1;
		//Attempt at refactoring
		//attitude=radioButtonSelection("attitude", attitudeRG.getCheckedRadioButtonId());
		switch (attitudeRG.getCheckedRadioButtonId()){
		case R.id.tutor_eval_attitue_radiogroup_radio0:
			attitude = 1;
			break;
		case R.id.tutor_eval_attitue_radiogroup_radio1:
			attitude = 2;
			break;
		case R.id.tutor_eval_attitue_radiogroup_radio2:
			attitude = 3;
			break;
		case R.id.tutor_eval_attitue_radiogroup_radio3:
			attitude = 4;
			break;
		case R.id.tutor_eval_attitue_radiogroup_radio4:
			attitude =5;
			break;
		}
		RadioGroup generalRG = (RadioGroup)findViewById(R.id.tutor_eval_general_radiogroup);
		int general = -1;
		switch (generalRG.getCheckedRadioButtonId()){
		case R.id.tutor_eval_general_radiogroup_radio0:
			general = 1;
			break;
		case R.id.tutor_eval_general_radiogroup_radio1:
			general = 2;
			break;
		case R.id.tutor_eval_general_radiogroup_radio2:
			general = 3;
			break;
		case R.id.tutor_eval_general_radiogroup_radio3:
			general = 4;
			break;
		case R.id.tutor_eval_general_radiogroup_radio4:
			general =5;
			break;
		}
		RadioGroup atmosphereRG = (RadioGroup)findViewById(R.id.tutor_eval_atmosphere_radiogroup);
		int atmosphere = -1;
		switch (atmosphereRG.getCheckedRadioButtonId()){
		case R.id.tutor_eval_atmosphere_radiogroup_radio0:
			atmosphere = 1;
			break;
		case R.id.tutor_eval_atmosphere_radiogroup_radio1:
			atmosphere = 2;
			break;
		case R.id.tutor_eval_atmosphere_radiogroup_radio2:
			atmosphere = 3;
			break;
		case R.id.tutor_eval_atmosphere_radiogroup_radio3:
			atmosphere = 4;
			break;
		case R.id.tutor_eval_atmosphere_radiogroup_radio4:
			atmosphere =5;
			break;
		}
		RadioGroup understandingRG = (RadioGroup)findViewById(R.id.tutor_eval_understanding_radiogroup);
		int understanding = -1;
		switch (understandingRG.getCheckedRadioButtonId()){
		case R.id.tutor_eval_understanding_radiogroup_radio0:
			understanding = 1;
			break;
		case R.id.tutor_eval_understanding_radiogroup_radio1:
			understanding = 2;
			break;
		case R.id.tutor_eval_understanding_radiogroup_radio2:
			understanding = 3;
			break;
		case R.id.tutor_eval_understanding_radiogroup_radio3:
			understanding = 4;
			break;
		case R.id.tutor_eval_understanding_radiogroup_radio4:
			understanding =5;
			break;
		}
		RadioGroup knowledgeRG = (RadioGroup)findViewById(R.id.tutor_eval_knowledge_radiogroup);
		int knowledge = -1;
		switch (knowledgeRG.getCheckedRadioButtonId()){
		case R.id.tutor_eval_knowledge_radiogroup_radio0:
			knowledge = 1;
			break;
		case R.id.tutor_eval_knowledge_radiogroup_radio1:
			knowledge = 2;
			break;
		case R.id.tutor_eval_knowledge_radiogroup_radio2:
			knowledge = 3;
			break;
		case R.id.tutor_eval_knowledge_radiogroup_radio3:
			knowledge = 4;
			break;
		case R.id.tutor_eval_knowledge_radiogroup_radio4:
			knowledge =5;
			break;
		}
		EditText commentET = (EditText)findViewById(R.id.edittext_tutor_eval_comment);
		String comment = commentET.getText().toString();
		EditText tNameET = (EditText)findViewById(R.id.edittext_tutor_eval_tutor_name);
		String tName = tNameET.getText().toString();
		EditText nameET = (EditText)findViewById(R.id.edittext_tutor_eval_your_name);
		String name = nameET.getText().toString();
		Firebase mFirebase = new Firebase("https://learning-center-app.firebaseio.com/Evaluations");
		HashMap<String, Object> data = new HashMap<String, Object>();
		if(!name.equals(null)){
			data.put("Evaluator", name);
		}else{
			data.put("Evaulator", "Anonymous");
		}
		if(!tName.equals(null)){
			data.put("Tutor Evaluated", tName);
		}else{
			data.put("Tutor Evaluated", "Anonymous");
		}
		if(!comment.equals(null)){
			data.put("Comment", comment);
		}else data.put("Comment", "None");
		if(!clsyr.equals("")&&!purpose.equals("")&&attitude!=-1&&knowledge!=-1&&understanding!=-1&&general!=-1&&atmosphere!=-1){
			data.put("Overall Knowledge", knowledge);
			data.put("Attitude", attitude);
			data.put("Understanding", understanding);
			data.put("General", general);
			data.put("Atmosphere", atmosphere);
			data.put("Class Year", clsyr);
			data.put("Purpose", purpose);
			data.put("ESL", esl);
			mFirebase =mFirebase.push();
			mFirebase.setValue(data);
			finish();
		}else Toast.makeText(getBaseContext(), "Input all required fields", Toast.LENGTH_SHORT).show();
		
	}

	private int radioButtonSelection(String name, int checkedButtonId) {
		int result = -1;
		switch (checkedButtonId){
		//case R.id.(tutor_eval_+"name"+_radiogroup_radio0):
			//return result = 1;
		case R.id.tutor_eval_knowledge_radiogroup_radio1:
			return result = 2;
		case R.id.tutor_eval_knowledge_radiogroup_radio2:
			return result = 3;
		case R.id.tutor_eval_knowledge_radiogroup_radio3:
			return result = 4;
		case R.id.tutor_eval_knowledge_radiogroup_radio4:
			return result =5;
		}
		return result;
	}
}
