package com.example.learningcenterapp;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class SlotSignUpArrayAdapter extends ArrayAdapter<StudentSlot>{
	
	public SlotSignUpArrayAdapter(Context context, List<StudentSlot> students ){
		super(context, android.R.layout.simple_expandable_list_item_2,
				android.R.id.text1, students);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		StudentSlot student = getItem(position);
		TextView studentTextView = (TextView) view.findViewById(android.R.id.text1);
		studentTextView.setText(student.getName());
		TextView time = (TextView) view.findViewById(android.R.id.text2);
		time.setText(student.getTime());
		return view;
	}
}
