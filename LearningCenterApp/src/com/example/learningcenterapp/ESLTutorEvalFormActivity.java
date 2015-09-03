package com.example.learningcenterapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class ESLTutorEvalFormActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.esl_tutor_evaluation_form);
		setContentView(R.layout.activity_esltutor_eval_form);
		WebView myWebView = (WebView) findViewById(R.id.esl_tutor_webview);
		myWebView
				.loadUrl("https://docs.google.com/forms/d/1YBtkRkxVzjqGt-7dBC5hA5qoND66-o_gfG__iuSQsMo/viewform");
	}
}
