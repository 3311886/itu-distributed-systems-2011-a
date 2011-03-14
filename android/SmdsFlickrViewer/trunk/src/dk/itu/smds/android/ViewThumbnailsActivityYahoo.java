package dk.itu.smds.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

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
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.aetrion.flickr.photos.SearchParameters;
public class ViewThumbnailsActivityYahoo extends Activity {
	String url = "http://search.yahooapis.com/ImageSearchService/V1/imageSearch";
	static final String YAHOO_APPID = "I7IbSgfV34HOwxppptxEyePcCgrK9Glsv3wzd_go9dndLI8_6ap4XE69kk3CzxI"; 
	static final int DIALOG_INFINITE_PROGRESS = 0;
	static final int RESULTSIZE = 10;
	static final String EXTRA_PHOTO_PAGE_URI = "page_uri";
	static final String EXTRA_PHOTO_URI = "photo_uri";
	int page = 1;

	class PhotoInfo {
		dk.itu.smds.android.Photo photo;
		Bitmap thumbnail;
	}

	class SearchAsyncTask extends AsyncTask<SearchParameters,PhotoInfo,List<PhotoInfo>> {
		@Override
		protected List<PhotoInfo> doInBackground(SearchParameters... params) {

			
			SearchParameters q = params[0];
			

			JSONArray photos = null;
			JSONObject json = null;
			try {
				
				String queryurl = url + "?appid=" + YAHOO_APPID + "&query=" + q.getText() + "&results=" + RESULTSIZE +  "&start=" + (page*RESULTSIZE);
				//search the photos..this method will take some time
				InputStream source = retrieveStream(queryurl);  
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(source));
				
				StringBuilder sb = new StringBuilder();
				String s = "";
				while (( s= reader.readLine())!= null) {
					sb.append(s);
				}
				String input = sb.toString();
				try {
				json = XML.toJSONObject(input);
				JSONObject header =  json.getJSONObject("ResultSet");
				photos = header.getJSONArray("Result");
				} catch (JSONException e){
					Log.e("JSON", Arrays.toString(e.getStackTrace()));
				}
				

			} catch (Exception e) {
				Log.e("SearchAsyncTask", "can't search photos", e);
				e.printStackTrace();
			} finally {
				
				//dismiss the dialog
				dismissDialog(DIALOG_INFINITE_PROGRESS);
			}

			if(photos==null) {return null;}

			List<PhotoInfo> out = new ArrayList<PhotoInfo>();
			Bitmap bitmap;
			JSONObject o = null;
			for(int i  = 0;i< photos.length(); i++) {
				try {
					//get the thumbnail url and download it in a Bitmap object
					o = photos.getJSONObject(i);
					JSONObject tb = o.getJSONObject("Thumbnail");
					String thumbUrl = tb.getString("Url");
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
					//if we got a thumb, create a PhotoInfo object
					PhotoInfo pi = new PhotoInfo();
					try {
						pi.photo = getPhoto(o);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					pi.thumbnail = bitmap;
					//and publish our progresses
					publishProgress(pi);
					out.add(pi);
				}
			}

			return out;
		}
		@Override
		protected void onPreExecute() {
			// while search, show a dialog with infinite progress

			findViewById( R.id.SearcAgainTop).setEnabled(false);
			findViewById( R.id.MoreButton).setEnabled(false);
			findViewById( R.id.SearcAgainBottom).setEnabled(false);
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
				Toast.makeText(ViewThumbnailsActivityYahoo.this, "Sorry, an error occurred!", 10);

			} else {

				findViewById( R.id.SearcAgainTop).setEnabled(true);
				findViewById( R.id.MoreButton).setEnabled(true);
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
				Intent intent = new Intent(ViewThumbnailsActivityYahoo.this,ViewPhotoActivity.class);
				intent.putExtra(EXTRA_PHOTO_URI, pi.photo.Url);
				intent.putExtra(EXTRA_PHOTO_PAGE_URI, pi.photo.ClickUrl);
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
		page=1;
		Intent searchActivity = new Intent(this, SearchActivity.class);
		startActivity(searchActivity);

	}

	private InputStream retrieveStream(String queryurl) {

		InputStream stream = null;
		try {
		URL url = new URL(queryurl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		stream = con.getInputStream();
		} catch (Exception e){
			e.printStackTrace();
		}
	return stream;
	
	}

	private Photo getPhoto(JSONObject o) throws IllegalArgumentException, IllegalAccessException, JSONException {
		Field[] fields =  Photo.class.getFields();
		Photo p = new Photo();
		 try {
		for (Field f : fields) {
			String value = o.getString(f.getName());
			f.set(p, value);
		}
		 } catch (Exception e) {
			 Log.e("getPhoto", p.toString());
		 }
		return p;
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