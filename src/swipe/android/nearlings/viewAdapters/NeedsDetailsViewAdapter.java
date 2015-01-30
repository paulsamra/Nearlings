package swipe.android.nearlings.viewAdapters;

import java.util.ArrayList;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.ActivityCallbackFromAdapter;
import swipe.android.nearlings.DummyWebTask;
import swipe.android.nearlings.FieldsParsingUtils;
import swipe.android.nearlings.JsonChangeStateResponse;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.R;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.MessagesSync.Needs;
import swipe.android.nearlings.json.needs.comments.Comments;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.greyButton.GreyedOutButton;
import com.edbert.library.network.AsyncTaskCompleteListener;
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
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

public class NeedsDetailsViewAdapter implements
		AsyncTaskCompleteListener<JsonChangeStateResponse>,
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
	public Button getDirections;
	public ActivityCallbackFromAdapter callback;

	public NeedsDetailsViewAdapter(View userDataView, Context context,
			String idOfDetail, Cursor cursor, Bundle savedInstanceState,
			ActivityCallbackFromAdapter callback) {
		this.context = context;
		this.idOfDetail = idOfDetail;
		this.cursor = cursor;
		this.callback = callback;
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
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_PRICE);
		int date_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_DUE_DATE);
		int author_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR);

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
		price.setText("$" + String.valueOf(cursor.getDouble(price_index)));

		date.setText(FieldsParsingUtils.getTime(cursor.getLong(date_index)));

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

		setUpFlowButton();

		setUpRoles();
		// ContentValues retVal = new ContentValues();
		valuesOfNeed = new ContentValues();
		DatabaseUtils.cursorRowToContentValues(cursor, valuesOfNeed);
		//reloadCommentData();

	}

	String state;
	ContentValues valuesOfNeed;

	public void disableFlowButton(String s) {
		disableFlowButton(s,Color.GRAY);
	}
	public void disableFlowButton(String s, int bgColor){
		doActionButton.setText(s);
		doActionButton.setEnabled(false);
		doActionButton.setBackgroundColor(bgColor);
		doActionButton.setTextColor(Color.WHITE);
	}
public void assignedClickToMarkDone(){
	doActionButton.setText("Click to mark need completed.");
	doActionButton.setBackgroundColor(Color.GREEN);
	doActionButton.setTextColor(Color.WHITE);
	doActionButton.setEnabled(true);
}
public void reviewAssignment(){
	doActionButton.setText("Need is done. Click to review.");
	doActionButton.setBackgroundColor(Color.GREEN);
	doActionButton.setTextColor(Color.WHITE);
	doActionButton.setEnabled(true);
}
	boolean isCreator = false, offerIsAvailable = false, madeAnOffer = false;

	public void setUpRoles() {
		int author_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR);
		String authorID = cursor.getString(author_index);
		String userID = SessionManager.getInstance(this.context).getUserID();
		if(authorID.equals(userID)){
			isCreator = true;
		}else{
			isCreator = false;
		}
		int offercount_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_OFFER_COUNT);
		int offerCount = cursor.getInt(offercount_index);
		if(offerCount > 0){
			offerIsAvailable=true;
		}else{
			offerIsAvailable=false;
		}
	///now query offers database
		
		
		//offer is available?
	}

	public void setUpFlowButton() {
		int status_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_STATUS);
		state = cursor.getString(status_index);

		if (state.equals(Needs.AVAILABLE)) {
			// if we are the needer
			if (isCreator && offerIsAvailable) {
				disableFlowButton("An offer is available! Check the offers tab.");
			} else if (isCreator && !offerIsAvailable) {
				disableFlowButton("Waiting for offers");
			} else if (madeAnOffer) {
				disableFlowButton("You've already made an offer.");
			} else {

				// make button avialble
			}
			// if we are the doer
		} else if (state.equals(Needs.ASSIGNED_TO)) {
			if (isCreator) {
				// grey button
				disableFlowButton("Need has been assigned to someone.");
			} else {
				// click when done
				assignedClickToMarkDone();
			}
		} else if (state.equals(Needs.REVIEW)) {
			if (isCreator) {
				// grey button
			} else {
				// click when done
			}
		} else {
			// set as closed
		}

		doActionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// launch the request
				// acceptNeedPurchase();
			}

		});
	}

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
		
	/*	
		commentAdapter = new LazyDetailCommentsAdapter(this.context,
				listofCommentsArrayList, idOfDetail, Integer.MAX_VALUE);
		listOfComments.setAdapter(commentAdapter);
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
	public void onTaskComplete(JsonChangeStateResponse result) {

		// write out
		if (valuesOfNeed != null && valuesOfNeed.size() != 0) {

			valuesOfNeed.put(NeedsDetailsDatabaseHelper.COLUMN_STATUS,
					JsonChangeStateResponse.getStatus(state));
			valuesOfNeed
					.put(DatabaseCommandManager.SQL_INSERT_OR_REPLACE, true);
			context.getContentResolver()
					.insert(NearlingsContentProvider
							.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
							valuesOfNeed);
			((Activity) context).finish();

		}

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

	public void acceptNeedPurchase() {

		double price = cursor.getDouble(cursor
				.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_PRICE));
		String item = cursor.getString(cursor
				.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_TITLE));
		PayPalPayment thingToBuy = NearlingsApplication.generatePayObject(
				price, item, PayPalPayment.PAYMENT_INTENT_SALE);

		Intent intent = new Intent(context, PaymentActivity.class);

		// send the same configuration for restart resiliency
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,
				NearlingsApplication.config);

		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

		callback.startActivityForResultBridge(intent,
				NearlingsApplication.REQUEST_CODE_PAYMENT);
	}
}
