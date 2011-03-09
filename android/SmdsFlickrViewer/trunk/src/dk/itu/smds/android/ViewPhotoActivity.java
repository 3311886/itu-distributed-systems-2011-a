package dk.itu.smds.android;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class ViewPhotoActivity extends Activity implements View.OnClickListener{
	String photoUri;
	String pageUri;
	static final int DIALOG_DOWNLOADING = 0;

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.photo);
		Intent startIntent = getIntent();
		
		photoUri = startIntent.getStringExtra(ViewThumbnailsActivityFlickr.EXTRA_PHOTO_URI);
		pageUri = startIntent.getStringExtra(ViewThumbnailsActivityFlickr.EXTRA_PHOTO_PAGE_URI);
		new PhotoDownloadAsyncTask().execute(photoUri);
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if(DIALOG_DOWNLOADING == id) {
		    return ProgressDialog.show(this, "", "Downloading photo, please wait...", true);
		}
		return super.onCreateDialog(id);
	}
	


	class PhotoDownloadAsyncTask extends AsyncTask<String,Void,Bitmap> 
	{
		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap out = null;

			try {
				// like for the thumbnails, download the image in a Bitmap object
				URL url = new URL( params[0] );
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				out = BitmapFactory.decodeStream(urlConnection.getInputStream());
			} catch(Exception e) {
				Log.e("ViewPhotoActivity", "can't download the photo!", e);
			}
			return out;
		}

		@Override
		protected void onPreExecute() {
			showDialog(DIALOG_DOWNLOADING);
			
		}

		@Override 
		protected void onPostExecute(Bitmap result) {
			// dismiss the dialog
			dismissDialog(DIALOG_DOWNLOADING);
			if(result == null) {
				// an error occurred
				Toast.makeText(ViewPhotoActivity.this, "Can't download the photo!", 10);
			} else {
				// set the photo
				setPhoto(result);
			}
		}		

	}

	
	public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(pageUri));
		startActivity(intent);
		
		
	}
	
	protected void setPhoto(Bitmap bitmap) {
		ViewGroup parentView = (ViewGroup)findViewById( R.id.PhotoScrollView );
	
		// create the image button
		ImageButton img = new ImageButton(this);
		img.setImageBitmap(bitmap);
		img.setOnClickListener(this);

		// add the image button to the parent group
		parentView.addView(img, bitmap.getWidth(), bitmap.getHeight());
	
	}


}


