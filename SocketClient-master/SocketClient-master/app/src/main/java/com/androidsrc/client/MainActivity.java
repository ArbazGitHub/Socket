package com.androidsrc.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends Activity {

	TextView tvResponse;
	EditText editTextAddress, editTextPort;
	Button buttonConnect, buttonClear;

	private Socket mSocket;

	{
		try {
			//mSocket = IO.socket("http://192.168.1.101:3000");//old port
			mSocket = IO.socket("http://192.168.1.101:3002");
		} catch (URISyntaxException e) {}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editTextAddress = (EditText) findViewById(R.id.addressEditText);
		editTextPort = (EditText) findViewById(R.id.portEditText);
		buttonConnect = (Button) findViewById(R.id.connectButton);
		buttonClear = (Button) findViewById(R.id.clearButton);
		tvResponse = (TextView) findViewById(R.id.responseTextView);

		buttonConnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mSocket.disconnect();
				mSocket.on("get_geolocation_119", onNewMessage);
				mSocket.connect();
			}
		});

		buttonClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvResponse.setText("");
			}
		});
	}
	private Emitter.Listener onNewMessage = new Emitter.Listener() {
		@Override
		public void call(final Object... args) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					JSONObject data = (JSONObject) args[0];
					Gson gson = new Gson();
					try {
						GeoLocation response = gson.fromJson(data.toString(),GeoLocation.class);
						if(response!=null){
//							tvResponse.setText("User ID : "+response.getUserId());
									tvResponse.setText("Latitude : "+response.getGeoLat()+ "\n"+"Longitude : "+response.getGeoLon());
						}
					} catch (Exception e) {
						return;
					}

					// add the message to view

				}
			});
		}
	};
}
