package dk.itu.smds.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class SearchActivity extends Activity {
	
	public static final String EXTRA_SEARCHTEXT = "dk.itu.smds.android.SearchActivity.SEARCHTEXT";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        
        setContentView(R.layout.main);
    }
    
    public void doStartSearch( View view ) {
    	
    	// get the EditSearch view
    	EditText searchEditText = (EditText)findViewById( R.id.SearchEditText );
    	// get the string inputted by the user
    	String q = searchEditText.getText().toString();
    	
    	if(q==null || q.length()==0) {
    		//uops..empty string
    		return;
    	
    	}
    	// create an intent to start the ViewThumbnailsActivity
    	Intent startThumbnailsActIntent = new Intent(this, ViewThumbnailsActivity.class);
    	// put in the intent the search string too
    	startThumbnailsActIntent.putExtra(EXTRA_SEARCHTEXT, q);
    	
    	// fire the intent
    	startActivity(startThumbnailsActIntent);
    }
}