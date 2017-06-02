package com.firstaid.uploadMedicalRecords;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.firstaid.oldman.R;

public class CustomProgressDialog extends Dialog {

	public CustomProgressDialog(Context context) {
		super(context, R.style.custom_dialog);
		this.setContentView(R.layout.progress_dialog);
		setCancelable(true);
	}

	public void setMessage(CharSequence message) {
		((TextView) findViewById(R.id.progress_dialog_msg)).setText(message);
	}

}