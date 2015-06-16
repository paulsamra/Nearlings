package swipe.android.nearlings.viewAdapters;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.FieldsParsingUtils;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.R;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.json.addCommentsResponse.JsonAddCommentsResponse;
import swipe.android.nearlings.json.needs.comments.Comments;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.edbert.library.greyButton.GreyedOutButton;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NeedsDetailsViewAdapter implements
		AsyncTaskCompleteListener<JsonAddCommentsResponse>,
		LoaderCallbacks<Cursor> {

	private Context context;

	private Cursor cr;
	public TextView title, price, date, personRequesting, description,
			location;

	// this needs to have a live adapter attached.
	public ScrollView commentView, fullScrollView;
	public MapFragment mapFragment;
	public ImageView personRequestingImage;
	public ListView listOfComments;
	private String idOfDetail;
	private Cursor cursor;
	private Cursor commentCursor;
	public GreyedOutButton doActionButton;
	public Button getDirections, add_comments;

	public NeedsDetailsViewAdapter(View userDataView, Context context,
			String idOfDetail, Cursor cursor, Bundle savedInstanceState) {
		this.context = context;
		this.idOfDetail = idOfDetail;
		this.cursor = cursor;
		MapsInitializer.initialize(context);
		initializeView(userDataView, savedInstanceState);

		reloadData();
	}

	ArrayList<Comments> listofCommentsArrayList;

	public View initializeView(View view, Bundle savedInstanceState) {
		fullScrollView = (ScrollView) view.findViewById(R.id.scroll_frame);
		doActionButton = (GreyedOutButton) view
				.findViewById(R.id.needs_change_state);
		title = (TextView) view.findViewById(R.id.needs_details_title);
		price = (TextView) view.findViewById(R.id.needs_details_price);
		date = (TextView) view.findViewById(R.id.needs_details_date);
		personRequesting = (TextView) view
				.findViewById(R.id.needs_details_author);
		description = (TextView) view
				.findViewById(R.id.needs_details_description);
		location = (TextView) view.findViewById(R.id.needs_details_location);

		getDirections = (Button) view.findViewById(R.id.getDirectionsButton);
		add_comments = (Button) view
				.findViewById(R.id.needs_view_more_comments);
		add_comments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// addComment();
				createCommentDialog();
			}
		});

		MapsInitializer.initialize(((Activity) context));

		mapFragment = (MapFragment) ((Activity) this.context)
				.getFragmentManager().findFragmentById(R.id.mapview);

		ImageView transparentImageView = (ImageView) view
				.findViewById(R.id.transparent_image);

		transparentImageView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow ScrollView to intercept touch events.
					fullScrollView.requestDisallowInterceptTouchEvent(true);
					// Disable touch on transparent view
					return false;

				case MotionEvent.ACTION_UP:
					// Allow ScrollView to intercept touch events.
					fullScrollView.requestDisallowInterceptTouchEvent(false);
					return true;

				case MotionEvent.ACTION_MOVE:
					fullScrollView.requestDisallowInterceptTouchEvent(true);
					return false;

				default:
					return true;
				}
			}
		});
		// setUpMapIfNeeded(view);
		personRequestingImage = (ImageView) view
				.findViewById(R.id.needs_details_author_image_preview);
		listOfComments = (ListView) view
				.findViewById(R.id.needs_details_comments_list);
		listOfComments.setOnTouchListener(new OnTouchListener() {
			// Setting on Touch Listener for handling the touch inside
			// ScrollView
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Disallow the touch request for parent scroll on touch of
				// child view
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		/*
		 * commentCursor = context .getContentResolver()
		 * .query(NearlingsContentProvider
		 * .contentURIbyTableName(NeedsCommentsDatabaseHelper.TABLE_NAME),
		 * NeedsCommentsDatabaseHelper.COLUMNS, null, null, null);
		 */
		// commentAdapter = new LazyDetailCommentsAdapter(context);
		// commentAdapter.notifyDataSetChanged();
		// listOfComments.setAdapter(commentAdapter);

		// listOfComments.invalidate();
		/*
		 * listOfComments.setClickable(false);
		 * listOfComments.requestDisallowInterceptTouchEvent(false);
		 * listOfComments.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) {
		 * if(event.getAction()==MotionEvent.ACTION_MOVE) { return true; }
		 * return false; } });
		 */
		listofCommentsArrayList = new ArrayList<Comments>();

		commentAdapter = new LazyDetailCommentsAdapter(this.context,
				listofCommentsArrayList, idOfDetail, Integer.MAX_VALUE);
		listOfComments.setAdapter(commentAdapter);

		// view.setTag(holder);*/
		return null;
	}

	LazyDetailCommentsAdapter commentAdapter;
	GoogleMap mMap;

	public void reloadData() {

		cursor.requery();

		cursor.moveToFirst();
		int title_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_TITLE);
		int price_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_REWARD);
		int date_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_DUE_DATE);
		int author_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_USER);

		int description_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_DESCRIPTION);
		int latitude_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LATITUDE);
		int longitude_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_GEOPOINT_LONGITUDE);
		int location_name_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_NAME);
		int needs_id_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_ID);
		int status_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_STATUS);

		int personRequestImage_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR_IMAGE_PREVIEW_URL);
		String titleString = cursor.getString(title_index);
		title.setText(titleString);
		price.setText("$" + String.valueOf(cursor.getFloat(price_index)));

		date.setText(FieldsParsingUtils.getTime(this.context,cursor.getLong(date_index)));

		personRequesting.setText(cursor.getString(author_index));
		description.setText(cursor.getString(description_index));
		location.setText(cursor.getString(location_name_index));
		// for now
		location.setVisibility(View.GONE);
		final double latitude = cursor.getDouble(latitude_index);
		final double longitude = cursor.getDouble(longitude_index);
		LatLng locationOfRequest = new LatLng(latitude, longitude);

		if (mapFragment != null) {
			mMap = mapFragment.getMap();
			if (mMap != null) {

				mMap.getUiSettings().setMyLocationButtonEnabled(false);

				mMap.setMyLocationEnabled(true);

				mMap.getUiSettings().setZoomControlsEnabled(true);
				addUpMapMarker(latitude, longitude, titleString);
			}

		}
		getDirections.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Location l = ((NearlingsApplication) NeedsDetailsViewAdapter.this.context
						.getApplicationContext()).getLastLocation();

				String url = "http://maps.google.com/maps?saddr="
						+ l.getLatitude() + "," + l.getLongitude() + "&daddr="
						+ latitude + "," + longitude;
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse(url));
				NeedsDetailsViewAdapter.this.context.startActivity(intent);
			}

		});
		ImageLoader.getInstance()
				.displayImage(cursor.getString(personRequestImage_index),
						personRequestingImage,
						NearlingsApplication.getDefaultOptions());
		state = cursor.getString(status_index);

		// ContentValues retVal = new ContentValues();
		valuesOfNeed = new ContentValues();
		DatabaseUtils.cursorRowToContentValues(cursor, valuesOfNeed);
		// reloadCommentData();

	}

	String state;
	ContentValues valuesOfNeed;

	public void reloadCommentData() {
		/*
		 * new PostDataWebTask<JsonNeedsCommentsResponse>(this,
		 * JsonNeedsCommentsResponse.class).execute(SessionManager
		 * .getInstance(this).(), MapUtils .mapToString(headers));
		 */

		/*
		 * commentAdapter = new NeedsCommentsAdapter(this.context,
		 * listofCommentsArrayList); listOfComments.setAdapter(commentAdapter);
		 * //commentAdapter = new LazyDetailCommentsAdapter(context,
		 * commentCursor); // commentAdapter.requestUpdate();
		 * commentAdapter.notifyDataSetChanged();
		 * listOfComments.setAdapter(commentAdapter);
		 */
		commentAdapter.requestUpdate();
		/*
		 * commentAdapter = new LazyDetailCommentsAdapter(this.context,
		 * listofCommentsArrayList, idOfDetail, Integer.MAX_VALUE);
		 * listOfComments.setAdapter(commentAdapter);
		 */
	}

	private void setUpMapIfNeeded(View inflatedView) {
		if (mMap == null) {
			/*
			 * mMap = ((MapView) inflatedView.findViewById(R.id.mapview))
			 * .getMap();
			 */
			mMap = ((MapFragment) ((Activity) this.context)
					.getFragmentManager().findFragmentById(R.id.mapview))
					.getMap();

		}
	}

	// this one should only have 1.
	private void addUpMapMarker(double lat, double log, String title) {
		Marker m = mMap.addMarker(new MarkerOptions().position(
				new LatLng(lat, log)).title(title));
		LatLngBounds.Builder b = new LatLngBounds.Builder();

		b.include(m.getPosition());

		LatLngBounds bounds = b.build();
		// Change the padding as per needed
		CameraUpdate cu = CameraUpdateFactory
				.newLatLngBounds(bounds, 25, 25, 5);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
		mMap.moveCamera(cu);
		mMap.animateCamera(zoom);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	public void executeAddComment() {
		String message = addCommentText.getText().toString();
		String id = SessionManager.getInstance(context).getUserID();
		try {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put("commenter_id", id);

			jsonObject.put("message", message);
			Map<String, String> headers = SessionManager.getInstance(
					this.context).defaultSessionHeaders();

			
			new PostDataWebTask<JsonAddCommentsResponse>(
				 this.context,	(AsyncTaskCompleteListener) NeedsDetailsViewAdapter.this,
					JsonAddCommentsResponse.class, true).execute(SessionManager
					.getInstance(this.context).makeCommentURL(this.idOfDetail),
					MapUtils.mapToString(headers), jsonObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onTaskComplete(JsonAddCommentsResponse result) {

		if (result.isValid()) {

			reloadCommentData();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							dialog.cancel();
						}
					});
			builder.setTitle("Error");
			builder.setMessage(result.getError());
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	EditText addCommentText;

	public void createCommentDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Add Comment");

		// Set up the input
		addCommentText = new EditText(context);
		// Specify the type of input expected; this, for example, sets the input
		// as a password, and will mask the text
		addCommentText.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(addCommentText);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				executeAddComment();

			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		builder.show();
	}
}
