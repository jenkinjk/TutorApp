package com.example.learningcenterapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TutorScheduelListAdapter extends BaseAdapter {

	private Context mContext;
	private int mNumRows;

	// for testing
	private ArrayList<String> mTutors;
	private ArrayList<String> mTimes;
	private ArrayList<String> mMajors;

	public TutorScheduelListAdapter(Context context) {
		mContext = context;

		// for testing
		mNumRows = 2;
		mTutors.add("Coco");
		mTutors.add("Jonathan");
		mTimes.add("7pm");
		mTimes.add("9pm");
		mMajors.add("CS");
		mMajors.add("CSSEMA");

	}


	@Override
	public int getCount() {
		return mNumRows;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TutorScheduleRow view;
		if (convertView == null) {
			view = new TutorScheduleRow(this.mContext);
		} else {
			view = (TutorScheduleRow) convertView;
		}
		view.setNameText(mTutors.get(0));
		view.setTimeText(mTimes.get(0));
		view.setMajorText(mMajors.get(0));

		return view;
	}

}
