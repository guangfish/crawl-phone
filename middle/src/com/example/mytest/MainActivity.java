package com.example.mytest;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView copyValue;
	TextView pasteValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		copyValue = (TextView) findViewById(R.id.copyValue);
		pasteValue = (TextView) findViewById(R.id.pasteValue);
	}

	public void copyData(View v) {
        ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(null,copyValue.getText()));
        copyValue.setText("");
	}
	
	public void pasteData(View v) {
		pasteValue.setText("");
		CharSequence primaryClip = ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).getText();
		pasteValue.setText(primaryClip);
	}
	
}