/**--------------------------------
 * DisplayMessageActivity.java
 * activity that is called to echo 
 * user input to screen
 ---------------------------------*/

package com.example.apptest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity implements LocationListener{

	private TextView latituteField;
	private TextView longitudeField;
	private LocationManager locationManager;
	private String phoneNo = "5556";
	private String NUMBER = "3182352965";
	
	//called when activity being created
	//gets location and prints out coordinates on screen for confirmation that it is working
	//prints confirmation of sent message
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set user interface layout
		setContentView(R.layout.activity_display_message);

		latituteField = (TextView) findViewById(R.id.LatField);
	    longitudeField = (TextView) findViewById(R.id.LonField);
	    
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    
	    //get the message from the intent (run-time binding between activity that called this activity and this)
	  	Intent intent = getIntent();
	  	String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
	  	//create the text-view widget
	  	TextView textView = (TextView) findViewById(R.id.Confirmation);
	  	textView.setTextSize(40);
	  	textView.setText(message);
	    
		//Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//show the up button in the action bar
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
    	//check last known location -- received on resume of activity
    	Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	if (location != null) {
    		onLocationChanged(location);
    	}
    	else {
    		latituteField.setText("Location not available");
    	    longitudeField.setText("Location not available");	
    	}
    	
    	Button btnCall = (Button) findViewById(R.id.btnCall);
    	
    	btnCall.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View arg0) {
    			callPsafe();
    		}
    	});
    	
    	
    	//sendSMS(location);
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
   	    latituteField.setText(String.valueOf(lat));
   	    longitudeField.setText(String.valueOf(lon));
   	}
   	
   	//public void sendLocation(Location location) {
   	//
   	//}

   	public void callPsafe() {
//    	Uri number = Uri.parse(NUMBER);
    	Intent callIntent = new Intent(Intent.ACTION_CALL);
    	callIntent.setData(Uri.parse("tel:" + NUMBER));
    	startActivity(callIntent);
   	}
   	
    public void sendSMS(Location location) {
   	  String lat = String.valueOf(location.getLatitude());
	  String lon = String.valueOf(location.getLongitude());
   	  String helpMessage = "( " + lat + " , " + lon + " )";
   	  PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, DisplayMessageActivity.class), PendingIntent.FLAG_ONE_SHOT);
   	  SmsManager sms = SmsManager.getDefault();
   	  sms.sendTextMessage(phoneNo, null, helpMessage, pi, null);
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

    //FOLLOWING DEALS WITH LIFECYCLE AND MENU STRUCTURE OF APP
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
	
	//called when activity is being destroyed
	public void onDestroy() {
		super.onDestroy(); //always call the superclass
		//stop method tracing that activity started during onCreate()
		android.os.Debug.stopMethodTracing();
	}

}
