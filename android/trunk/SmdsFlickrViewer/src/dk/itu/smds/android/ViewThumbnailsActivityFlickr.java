package dk.itu.smds.android;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.Toast;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.SearchParameters;

public class ViewThumbnailsActivityFlickr extends Activity {

	static final int DIALOG_INFINITE_PROGRESS = 0;
	static final String EXTRA_PHOTO_PAGE_URI = "page_uri";
	static final String EXTRA_PHOTO_URI = "photo_uri";
	int page = 1;

	class PhotoInfo {
		Photo photo;
		Bitmap thumbnail;
	}

	class SearchAsyncTask extends AsyncTask<SearchParameters,PhotoInfo,List<PhotoInfo>> {
		@SuppressWarnings("unchecked")
		@Override
		protected List<PhotoInfo> doInBackground(SearchParameters... params) {
			SearchParameters q = params[0];

			Flickr f = new Flickr("24468596eb57bb533167e1846584ea6c");
			List<Photo> photos = null;
			try {
				//search the photos..this method will take some time
				photos = f.getPhotosInterface().search(q, 3, page);
			} catch (Exception e) {
				Log.e("SearchAsyncTask", "can't search photos", e);
			} finally {
				// dismiss the dialog
				dismissDialog(DIALOG_INFINITE_PROGRESS);
			}

			if(photos==null) {return null;}

			List<PhotoInfo> out = new ArrayList<PhotoInfo>();
			Bitmap bitmap;
			for(Photo p : photos) {
				try {
					// get the thumbnail url and download it in a Bitmap object
					String thumbUrl = p.getSmallUrl();
					URL url = new URL(thumbUrl);
					HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
					bitmap = BitmapFactory.decodeStream(urlConnection
							.getInputStream());
				} catch(Exception e) {
					e.printStackTrace();
					bitmap = null;
				}
				if(bitmap != null) {
					// if we got a thumb, create a PhotoInfo object
					PhotoInfo pi = new PhotoInfo();
					pi.photo = p;
					pi.thumbnail = bitmap;
					// and publish our progresses
					publishProgress(pi);
					out.add(pi);
				}
			}

			return out;
		}
		@Override
		protected void onPreExecute() {
			// while search, show a dialog with infinite progress

			findViewById( R.id.MoreButton).setEnabled(false);
			findViewById( R.id.SearcAgainBottom).setEnabled(false);
			findViewById( R.id.SearcAgainTop).setEnabled(false);
			findViewById( R.id.GotoTopButton).setEnabled(false);
			showDialog(DIALOG_INFINITE_PROGRESS);
		}
		@Override
		protected void onProgressUpdate(PhotoInfo... values) {
			// call the addPhoto method
			addPhoto(values[0]);
		}
		@Override
		protected void onPostExecute(List<PhotoInfo> result) {
			if(result==null) {
				// I guess show some dialog to the user
				Toast.makeText(ViewThumbnailsActivityFlickr.this, "Sorry, an error occurred!", 10);

			} else {

				findViewById( R.id.MoreButton).setEnabled(true);
				findViewById( R.id.SearcAgainTop).setEnabled(true);
				findViewById( R.id.SearcAgainBottom).setEnabled(true);
				findViewById( R.id.GotoTopButton).setEnabled(true);
			}
		}
	}

	// table layout variables
	TableLayout table;
	int photosPerRow;
	int currentColumn;
	TableRow currentRow = null;
	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature( Window.FEATURE_NO_TITLE );

		setContentView( R.layout.view_thumbs );

		table = (TableLayout)findViewById( R.id.ThumbsTableLayout );
		setupSizes();

		// get the intent that started the activity
		Intent startIntent = getIntent();
		// and then the string the user inputted
		String searchText = startIntent.getStringExtra( SearchActivity.EXTRA_SEARCHTEXT );

		// configure the search object
		SearchParameters searchParameters = new SearchParameters();
		searchParameters.setText(searchText);



		// start the SearchAsyncTask
		new SearchAsyncTask().execute(searchParameters);
	}

	private void setupSizes() {
		// get the width of the screen
		int w = getResources().getDisplayMetrics().widthPixels;
		// set up an invalid column
		currentColumn = -1;
		// the images will be max 200 width
		photosPerRow = w / 200;
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		if(DIALOG_INFINITE_PROGRESS == id) {
			return ProgressDialog.show(this, "", "Searching photos, please wait...", true);
		}
		return super.onCreateDialog(id);
	}

	protected void addPhoto( final PhotoInfo pi ) {

		// if we reached the end of the row, or we still need to start the first row
		if( currentColumn==photosPerRow || currentColumn<0 ){
			// create a new row
			currentRow = new TableRow(this);
			// add it the the table
			table.addView(currentRow, 
					new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// reset the currentColumn index
			currentColumn=0;
		}

		// create the image button
		ImageButton img = new ImageButton(this);
		// set the bitmap
		img.setImageBitmap( pi.thumbnail );
		// scale it if needed
		img.setScaleType(ScaleType.FIT_XY);
		// set the onClick listener
		img.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// go to the page
				//Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(pi.photo.getUrl()));
				Intent intent = new Intent(ViewThumbnailsActivityFlickr.this,ViewPhotoActivity.class);
				intent.putExtra(EXTRA_PHOTO_URI, pi.photo.getMediumUrl());
				intent.putExtra(EXTRA_PHOTO_PAGE_URI, pi.photo.getUrl());
				startActivity(intent);
			}
		});

		currentRow.addView(img, 200, 200);
		currentColumn++;
	}


	/***
	 * OnclickHandler for MoreButton
	 * @param view
	 */
	public void onLoadMore(View view) {	
		// get the intent that started the activity
		Intent startIntent = getIntent();
		// and then the string the user inputted
		String searchText = startIntent.getStringExtra( SearchActivity.EXTRA_SEARCHTEXT );

		// configure the search object
		SearchParameters searchParameters =  new SearchParameters();
		searchParameters.setText(searchText);

		// start the SearchAsyncTask
		page++;
		new SearchAsyncTask().execute(searchParameters);
	}

	public void onSearchAgain(View view) {
		Intent searchActivity = new Intent(this, SearchActivity.class);
		startActivity(searchActivity);

	}
	
	public ScrollView getScrollView() {
		return (ScrollView) findViewById(R.id.scrollView1);
	}
	

	public void onGotoTop(View view) {
		view.post(new Runnable() {
			public void run() {
				ScrollView sv =(ScrollView) findViewById(R.id.mainScrollView);				
				sv.fullScroll(ScrollView.FOCUS_UP);
				
			}
		}
		);
	} 


}
