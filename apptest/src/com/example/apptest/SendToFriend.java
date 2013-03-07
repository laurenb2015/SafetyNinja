package com.example.apptest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SendToFriend extends Activity implements LocationListener{

	public static String message;
	private LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_to_friend);

	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
	    Intent intent = getIntent();
	  	String phoneNo = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

	  	//create the text-view widget
	  	TextView textView = (TextView) findViewById(R.id.confirmed);
	  	textView.setTextSize(40);
	    
		//Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//show the up button in the action bar
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
    	//check last known location -- received on resume of activity
    	Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	if (location != null) {
    	  	
    		onLocationChanged(location);
  //  		String mess2 = message + phoneNo;
    		textView.setText(message);
    		sendSMS(phoneNo, message);
    	}
    	else {
    		textView.setText("Location not available.");
    	}
    		
	}

	private void sendSMS(String phoneNo, String myMessage) {
	
	//	PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, SendToFriend.class), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNo, null, myMessage, null, null);
	}
	
	//activity immediately requests location when (re-)started
	protected void onResume() {
		    super.onResume();
		    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 1, this);
		  }

	//logs confirmation values to screen 
   	public void onLocationChanged(Location location) {
   			
   		double lat = location.getLatitude();
   	    double lon = location.getLongitude();
   	    message = "( " + String.valueOf(lat) + " , " + String.valueOf(lon) + " )";
   	}
   	   	
   	//stops gps updates when paused or exited
   	@Override
    protected void onPause() {
      super.onPause();
   	  locationManager.removeUpdates(this);
    }
	
   	//empty method
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    //empty method
    @Override
    public void onProviderEnabled(String provider) {}

    //empty method
    @Override
    public void onProviderDisabled(String provider) {}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_send_to_friend, menu);
		return true;
	}
*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
