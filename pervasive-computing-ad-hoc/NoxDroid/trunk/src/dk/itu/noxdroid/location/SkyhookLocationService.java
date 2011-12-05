/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.itu.noxdroid.location;

import java.util.List;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.skyhookwireless.wps.WPSAuthentication;
import com.skyhookwireless.wps.XPS;

import dk.itu.noxdroid.R;
import dk.itu.noxdroid.database.DbAdapter;



// Need the following import to get access to the app resources, since this
// class is in a sub-package.

/**
 * This is an example of implementing an application service that runs locally
 * in the same process as the application.  The {@link LocalServiceActivities.Controller}
 * and {@link LocalServiceActivities.Binding} classes show how to interact with the
 * service.
 *
 * <p>Notice the use of the {@link NotificationManager} when interesting things
 * happen in the service.  This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */

public class SkyhookLocationService extends Service {

	private static final String TAG = "SkyhookLocationService";
	
	// final WPSAuthentication auth = new WPSAuthentication(, _realm);
	final WPSAuthentication auth = new WPSAuthentication("bokchan", "itu.dk");


	private XPS _xps;
	// some debug counters
	private int countOnProviderEnabled = 0;
	private int countOnStatusChanged = 0;	
	private int countOnProviderDisabled = 0;
	private int countOnLocationChanged = 0;
	
	private LocationManager lm;
	private LocationListener locListenD;

	private Double latitude;
	private Double longitude;    

	private DbAdapter mDbHelper;

	private String locationProvider;
	
	
	
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
    	
    	public SkyhookLocationService getService() {
    		Log.d(TAG, "LocalBinder called");
    		return SkyhookLocationService.this;
        }
    	
    }
    
    @Override
    public void onCreate() {
    	
    	Log.d(TAG, "onCreate called");
    	
//        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//
//        // Display a notification about us starting.  We put an icon in the status bar.
//        showNotification();
//
//        String msg = R.string.location_service_started;
    	Log.d(TAG, "location service started");
    	// toast really makes no sense any more
    	//Toast.makeText(this, R.string.location_service_started, Toast.LENGTH_SHORT).show();
    	
        /**
         *  Set up the location stuff
         *  for a stand alone example with an activity look here xxxx
         *  (TODO:pase url to svn / dk.itu.spvc.androidlocationsimple)
         */

		// get handle for LocationManager
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		
		// Note: we might 'wanna' enable other then gps? 
		//
		// possible criteria's:
		// power requirement
		// accuracy
		// bearing
		// speed
		// altitude
		//
		// enabledOnly set to true
		Boolean enabledOnly = true;
		List<String> allLocationProviders = lm.getAllProviders();
		Log.d(TAG, "allLocationProviders available: " + allLocationProviders);
		Criteria criteria = new Criteria();
		
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM); 
//		criteria.setAccuracy(accuracy);
		criteria.setAltitudeRequired(false);
		criteria.setSpeedRequired(false);
		String bestProvider = lm.getBestProvider(criteria, enabledOnly);
		Log.d(TAG, "bestProvider - based on criteria given (): " + bestProvider);
		
		// try out end
		

		
		
		// Set up data base 
        mDbHelper = new DbAdapter(this);
        mDbHelper.open();

        Log.d(TAG, "after db open: " + bestProvider);
        
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);	
		if (loc != null) {
			//
			// connect to the GPS location service  - get and print 
			// 
			locationProvider = loc.getProvider();
	        
			Log.d(TAG, "lm: " + lm);
			Log.d(TAG, "loc: " + loc);

			
			latitude = loc.getLatitude();
			longitude =  loc.getLongitude();
			
			Log.d(TAG, "Latitude is " + Double.toString(latitude));
			Log.d(TAG, "Longitude is " + Double.toString(longitude));
			Log.d(TAG, "locationProvider: " + locationProvider);
			/*
			 *  Add to database
			 */
			mDbHelper.createLocationPoint(latitude, longitude, locationProvider);
		
		} else {
			Log.d(TAG, "lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) is " + loc);
		}

		// ask the Location Manager to send us location updates
		locListenD = new DispLocListener();        
		// bind to location manager - TODO: fine tune the variables
		// 30000L / minTime	= the minimum time interval for notifications, in milliseconds.
		// 10.0f / minDistance - the minimum distance interval for notifications
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListenD);
		
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

    	Log.d(TAG, "onDestroy called");

        // Location: close down / unsubscribe  the location updates
        lm.removeUpdates(locListenD);
        
        // close database
        mDbHelper.close();
        
        // Tell the user we stopped
        Toast.makeText(this, R.string.location_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    
    
    /*
     * Location listener
     * 
     * - could also have been implemented directly on the
     * LocationService class but its convenient to split it out.
     *  
     */
	private class DispLocListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {

			countOnLocationChanged++;
			
			Log.d(TAG, "onLocationChanged called");
			Log.d(TAG, "countOnLocationChanged: " + countOnLocationChanged);
						
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			Log.d(TAG, "latitude: " + latitude + "longitude: " + longitude);
			Log.d(TAG, "locationProvider: " + locationProvider);

			/**
			 * Add to database
			 */
			mDbHelper.createLocationPoint(latitude, longitude, locationProvider);
			
		}


		@Override
		public void onProviderDisabled(String provider) {

			countOnProviderDisabled++;
			
			Log.d(TAG, "onProviderDisabled called");
			Log.d(TAG, "countOnProviderDisabled: " + countOnProviderDisabled);
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			
			countOnProviderEnabled++;
			
			Log.d(TAG, "onProviderEnabled called");
			Log.d(TAG, "countOnProviderEnabled: " + countOnProviderEnabled);
		
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

			countOnStatusChanged++;
			
			Log.d(TAG, "onStatusChanged called");
			Log.d(TAG, "countOnStatusChanged: " + countOnStatusChanged);
			
		}	
	
	}
    
}
