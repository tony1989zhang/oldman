package com.firstaid.uploadMedicalRecords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firstaid.oldman.App;
import com.firstaid.oldman.R;

public class ToastManager {
	private static ToastManager manager;
	private View toastView;
	private Context context;
	private Toast toast;
	private TextView tvToast;

	public ToastManager() {
		init();
	}

	public static ToastManager getManager() {
		if (manager == null)
			synchronized (ToastManager.class) {
				manager = new ToastManager();
			}
		return manager;
	}

	private void init() {
		context = App.getInstance();
		toastView = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
		tvToast = (TextView) toastView.findViewById(R.id.toaskMessage);
		toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
		toast.setView(toastView);
	}

	public void show(int resId) {
		show(resId, Toast.LENGTH_SHORT);
	}

	public void show(String hint) {
		show(hint, Toast.LENGTH_SHORT);
	}

	public void show(int resId, int duration) {
		toast.setDuration(duration);
		tvToast.setText(resId);
		toast.show();
	}

	public void show(String hint, int duration) {
		toast.setDuration(duration);
		tvToast.setText(hint);
		toast.show();
	}

	public void onDestroy() {
		toast.cancel();
		toast = null;
		manager = null;
	}
}
