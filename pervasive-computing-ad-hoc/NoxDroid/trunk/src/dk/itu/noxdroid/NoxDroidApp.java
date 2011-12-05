package dk.itu.noxdroid;

import java.util.UUID;

import android.app.Application;
import android.util.Log;
import dk.itu.noxdroid.database.NoxDroidDbAdapter;

public class NoxDroidApp extends Application {
	//private DbAdapter dbAdapter;
	private NoxDroidDbAdapter dbAdapter;
	private String TAG;
	private UUID currentTrack = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		TAG = getString(R.string.LOGCAT_TAG, getString(R.string.app_name), this
				.getClass().getSimpleName());
		Log.i(TAG, "Created NoxdroidApp");
	}
	
	public NoxDroidDbAdapter getDbAdapter() {
		if (dbAdapter == null) {
			NoxDroidDbAdapter.initInstance(this);
			dbAdapter = NoxDroidDbAdapter.getInstance();
			Log.i(TAG, "Init DbAdapter");
		}
		
		return dbAdapter;
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		if (dbAdapter != null)  {
			dbAdapter.close();
		}
	}
	
	public void setCurrentTrack(UUID uuid) {
		this.currentTrack = uuid;
	}
	public UUID getCurrentTrack() {
		return this.currentTrack;
	}
}
