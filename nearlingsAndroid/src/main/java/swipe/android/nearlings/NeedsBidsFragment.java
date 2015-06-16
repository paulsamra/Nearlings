package swipe.android.nearlings;

import java.util.Map;

import org.json.JSONObject;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsOfferDatabaseHelper;
import swipe.android.nearlings.MessagesSync.Needs;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.MessagesSync.NeedsOffersRequest;
import swipe.android.nearlings.MessagesSync.UserReviewsRequest;
import swipe.android.nearlings.json.changeStateResponse.LockPaymentResponse;
import swipe.android.nearlings.viewAdapters.NeedsOffersListAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PutDataWebTask;
import com.edbert.library.utils.MapUtils;

//TODO: Probably want to abstract this
public class NeedsBidsFragment extends NearlingsSwipeToRefreshFragment
		implements Refreshable, AsyncTaskCompleteListener<LockPaymentResponse> {

	ListView lView;
	/*
	 * String MESSAGES_START_FLAG = NeedsBidsFragment.class.getCanonicalName() +
	 * "_MESSAGES_START_FLAG"; String MESSAGES_FINISH_FLAG =
	 * NeedsBidsFragment.class.getCanonicalName() + "_MESSAGES_FINISH_FLAG";
	 */
	public static final String MESSAGES_START_FLAG = NeedsDetailsActivity.MESSAGES_START_FLAG;
	public static final String MESSAGES_FINISH_FLAG = NeedsDetailsActivity.MESSAGES_FINISH_FLAG;

	String id;

	@Override
	public CursorLoader generateCursorLoader() {

		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(NeedsOfferDatabaseHelper.TABLE_NAME),
				NeedsOfferDatabaseHelper.COLUMNS, null, null,
				NeedsOfferDatabaseHelper.COLUMN_ID + " DESC");
		return cursorLoader;
	}

	@Override
	public void reloadData() {
		// onRefresh();
		reloadAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.pull_to_refresh_single_list,
				container, false);
		Bundle b = getActivity().getIntent().getExtras();
		id = b.getString("id");
		((ActionBarActivity)this.getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
		((ActionBarActivity)this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((ActionBarActivity)this.getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
		((ActionBarActivity)this.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
		// this.getActivity().getSupportActionBar().setTitle("Messages");
		swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
		swipeView.setColorScheme(android.R.color.holo_blue_dark,
				android.R.color.holo_blue_light,
				android.R.color.holo_green_light,
				android.R.color.holo_green_light);
		lView = (ListView) rootView.findViewById(R.id.list);

		swipeView.setOnRefreshListener(this);
		lView.setOnItemClickListener(this);

		// reloadData();
		reloadAdapter();
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public String syncStartedFlag() {
		return MESSAGES_START_FLAG;
	}

	@Override
	public String syncFinishedFlag() {
		return MESSAGES_FINISH_FLAG;
	}

	@Override
	public void requestSync(Bundle b) {
		b.putString(NeedsDetailsRequest.BUNDLE_ID, this.id);
		b.putString(NeedsOffersRequest.BUNDLE_ID, this.id);

	//	Cursor c = generateCursor();
		
				String selectionClause = NeedsDetailsDatabaseHelper.COLUMN_ID + " = ?";
		String[] mSelectionArgs = { "" };
		mSelectionArgs[0] = id;
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
				NeedsDetailsDatabaseHelper.COLUMNS, selectionClause,
				mSelectionArgs, NeedsDetailsDatabaseHelper.COLUMN_DUE_DATE
						+ " DESC");
		Cursor c =cursorLoader.loadInBackground();
		c.moveToFirst();
		String user_id = c.getString(c.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_CREATED_BY));
		b.putString(UserReviewsRequest.BUNDLE_ID, user_id);
		
		super.requestSync(b);
	}

	@Override
	public void setSourceRequestHelper() {
		helpers.add(new NeedsOffersRequest(this.getActivity()));
	}

	@Override
	public void onResume() {
		super.onResume();

		onRefresh();
	}

	@Override
	public void reloadAdapter() {
		getLoaderManager().initLoader(0, null, this);

		Cursor c = generateCursor();

		this.mAdapter = new NeedsOffersListAdapter(this.getActivity(), c);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);

	}
public void launchConfirm(){
	String selectionClause = NeedsDetailsDatabaseHelper.COLUMN_ID + " = ?";
	String[] selectionArgs = { "" };
	selectionArgs[0] = this.id;
	Cursor cursor = this
			.getActivity()
			.getContentResolver()
			.query(NearlingsContentProvider
							.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
					NeedsDetailsDatabaseHelper.COLUMNS, selectionClause,
					selectionArgs, null);
	cursor.moveToFirst();
	int status_index = cursor
			.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_STATUS);

	String state = cursor.getString(status_index);

	if (!state.equals(Needs.AVAILABLE)) {
		return;
	}
	Cursor cursorOfBids = generateCursor();
	cursorOfBids.moveToFirst();

	double price = Double.valueOf(cursorOfBids.getString(cursorOfBids
			.getColumnIndex(NeedsOfferDatabaseHelper.COLUMN_OFFER_PRICE)));
	String id_of_doer = cursorOfBids.getString(cursorOfBids
			.getColumnIndex(NeedsOfferDatabaseHelper.COLUMN_CREATED_BY));

	String item = cursor.getString(cursor
			.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_TITLE));
	//
	Map<String, String> headers = SessionManager.getInstance(
			this.getActivity()).defaultSessionHeaders();
	try {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", "paynow");
		jsonObject.put("doer_id", id_of_doer);
		String body = jsonObject.toString();

		new PutDataWebTask<LockPaymentResponse>(

				this.getActivity(), 	(AsyncTaskCompleteListener<LockPaymentResponse>) this,LockPaymentResponse.class, true)
				.execute(SessionManager.getInstance(this.getActivity())
						.changeStateURL(this.id), MapUtils
						.mapToString(headers), body);

	} catch (Exception e) {
		e.printStackTrace();
	}
}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity());
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							dialog.cancel();
							launchConfirm();
						}
					});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				dialog.cancel();
			}
		});
			builder.setTitle("Accept Offer");
		Cursor cursorOfBids = generateCursor();
		cursorOfBids.moveToFirst();

		double price = Double.valueOf(cursorOfBids.getString(cursorOfBids
				.getColumnIndex(NeedsOfferDatabaseHelper.COLUMN_OFFER_PRICE)));
			builder.setMessage("Are you sure you want to accept the offer for $"+price+"?");


		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onTaskComplete(LockPaymentResponse result) {
		if (result == null) {
			NearlingsApplication.displayNetworkNotAvailableDialog(this.getActivity());
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity());
		if (result.isValid()) {

			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							dialog.cancel();
							((NeedsDetailsActivity) NeedsBidsFragment.this
									.getActivity()).onRefresh();
							// MakeOfferActivity.this.finish();
						}
					});
			builder.setTitle("Success!");
			builder.setMessage("You have successfully chosen an offer. Please wait for the refresh to pay.");
		} else {

			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							dialog.cancel();
							// MakeOfferActivity.this.finish();
						}
					});
			builder.setTitle("Error");
			builder.setMessage(result.getError());
		}

		AlertDialog alert = builder.create();
		alert.show();

	}

}