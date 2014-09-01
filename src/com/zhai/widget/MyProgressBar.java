package com.zhai.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhai.updater.R;

public class MyProgressBar {

	private Context mContext;
	private LayoutInflater mInflater;
	private ProgressBar mProgressBar;
	private ImageView logo;
	private TextView text;
	private ViewGroup rootView;
	private LinearLayout mFrameLayout;

	public MyProgressBar(Context context, int id) {
		this(context, null);
	}

	public MyProgressBar(Context context) {
		this(context, null);
	}

	public MyProgressBar(Context context, ViewGroup rootView) {
		this.mContext = context;
		this.rootView = rootView;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (rootView == null) {
			Activity activity = (Activity) this.mContext;
			rootView = (ViewGroup) activity.getWindow().getDecorView();
		}
		mFrameLayout = (LinearLayout) mInflater.inflate(R.layout.progress_bar,
				null);

		mProgressBar = (ProgressBar) mFrameLayout
				.findViewById(R.id.progress_bar);
		logo = (ImageView) mFrameLayout.findViewById(R.id.logo);
		text = (TextView) mFrameLayout.findViewById(R.id.text);
		rootView.addView(mFrameLayout);
	}

	public void removeSelf() {
		if (mFrameLayout != null && rootView != null) {
			rootView.removeView(mFrameLayout);
		}
	}

	public void show() {
		mProgressBar.setVisibility(View.VISIBLE);
		logo.setVisibility(View.GONE);
		text.setVisibility(View.GONE);
	}

	public void hide() {
		mProgressBar.setVisibility(View.GONE);
		logo.setVisibility(View.GONE);
		text.setVisibility(View.GONE);
	}

	public void dismiss() {
		mProgressBar.setVisibility(View.GONE);
		text.setVisibility(View.GONE);
		logo.setVisibility(View.GONE);
	}

	public void showEmptyText(String emptyText) {
		mProgressBar.setVisibility(View.GONE);
		logo.setVisibility(View.GONE);
		text.setVisibility(View.VISIBLE);
		text.setText(emptyText);
	}

}