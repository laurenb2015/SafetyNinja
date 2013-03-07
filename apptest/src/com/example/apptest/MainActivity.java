/**-------------------------
 * MainActivity.java
 * main activity. called first
--------------------------*/

package com.example.apptest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.apptest.MESSAGE";
	public final static String GPS_NEEDS_ENABLING = "Your GPS is not enabled. Please enable it so your location can be sent to P-SAFE.";
	public final static String YES = "OK";
	public final static String HELP_ON_WAY = "Your help signal has been sent.";
	
	
	//LIFE-CYCLE
	//Following methods deal with activity life-cycle 
	
	//called when started for first time or restarted
	//(re-)instantiates resources: verifies GPS enabled
	@Override
	protected void onStart() {
		super.onStart(); //Always call the superclass method first
		
		//ensure that GPS is enabled
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		if (!gpsEnabled) {
			//create a dialog to request user to enable GPS
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(GPS_NEEDS_ENABLING);
			builder.setCancelable(false);
			//if user clicks ok, bring them to Location Settings
			builder.setPositiveButton(YES, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// send user to Location settings
					enableLocationSettings();
				}
			} );
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	//use an intent with: android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS action
	//to take user to settings screen and enable GPS
	private void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart(); //always call the superclass method first
		//activity being restarted from stopped state
	}
	
	//create activity with layout specified in activity_main.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
   	public void onDestroy() {
   		super.onDestroy(); //always call the superclass
   		//stop method tracing that activity started during onCreate()
   		android.os.Debug.stopMethodTracing();
   	}
    
   	//FUNCTIONALITY
    //Following methods deal with functionality of app

    public void friendSend(View view) {
    	
    	EditText txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
    	String phoneNo = txtPhoneNo.getText().toString();
    	
    	//call new activity which confirms signal has been sent
    	Intent intent = new Intent(this, SendToFriend.class);
    	intent.putExtra(EXTRA_MESSAGE, phoneNo);
    	startActivity(intent);

    }
    
   public void playSound() {
    	
    	//call new activity which confirms signal has been sent
    	Intent intent = new Intent(this, PlayAlarmSound.class);
    	startActivity(intent);

    }
   	
   	
    //Called when the user clicks the Send button
    //requests current user location and calls method to send location to P-SAFE
    //creates intent to call activity which prints message sending confirmation to screen
    public void sendMessage(View view) {
    	
    	//call new activity which confirms signal has been sent
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	intent.putExtra(EXTRA_MESSAGE, HELP_ON_WAY);
    	startActivity(intent);
    }

}
