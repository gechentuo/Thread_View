package com.example.thread_view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView textview;
	Dialog dialog;
	Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn = (Button)findViewById(R.id.button);
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		textview = new TextView(MainActivity.this);
		dialog = builder.setTitle("www.baidu.com").setView(textview).create();
	}

	public void startProcess(View view) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				final byte[] content = getContent(); 
				btn.post(new Runnable() {
					@Override
					public void run() { 
						textview.setText(String.valueOf(content.length));
						dialog.show();
					}
				});
			}

		};
		new Thread(runnable).start();
	}

	private byte[] getContent() {
		URL url;
		String content = "";
		try {
			url = new URL("http://baidu.com");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			content = readStream(con.getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.getBytes();
	}

	private String readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuffer all = new StringBuffer();

		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String data = "";
			while ((data = reader.readLine()) != null) {
				all.append(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return all.toString();

	}
}
