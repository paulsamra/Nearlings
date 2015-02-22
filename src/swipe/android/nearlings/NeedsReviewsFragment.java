package swipe.android.nearlings;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;

import com.edbert.library.network.GetDataWebTask;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsOfferDatabaseHelper;
import swipe.android.DatabaseHelpers.UserReviewDatabaseHelper;
import swipe.android.nearlings.MessagesSync.MessagesRequest;
import swipe.android.nearlings.MessagesSync.NeedsOffersRequest;
import swipe.android.nearlings.MessagesSync.UserReviewsRequest;
import swipe.android.nearlings.events.EventsContainerFragment;
import swipe.android.nearlings.json.needs.needsdetailsoffersresponse.JsonNeedsOffersResponse;
import swipe.android.nearlings.json.needs.needsdetailsoffersresponse.NeedsOffers;
import swipe.android.nearlings.jsonResponses.login.JsonBidsResponse;
import swipe.android.nearlings.jsonResponses.login.JsonLoginResponse;
import swipe.android.nearlings.jsonResponses.register.Bids;
import swipe.android.nearlings.jsonResponses.register.Users;
import swipe.android.nearlings.viewAdapters.BidsViewAdapter;
import swipe.android.nearlings.viewAdapters.EventsListAdapter;
import swipe.android.nearlings.viewAdapters.MessagesViewAdapter;
import swipe.android.nearlings.viewAdapters.NeedsOffersListAdapter;
import swipe.android.nearlings.viewAdapters.NeedsReviewsAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

//TODO: Probably want to abstract this
public class NeedsReviewsFragment extends NearlingsSwipeToRefreshFragment
		implements Refreshable {

	ListView lView;
	String MESSAGES_START_FLAG = NeedsReviewsFragment.class.getCanonicalName()
			+ "_MESSAGES_START_FLAG";
	String MESSAGES_FINISH_FLAG = NeedsReviewsFragment.class.getCanonicalName()
			+ "_MESSAGES_FINISH_FLAG";
	String id;

	@Override
	public CursorLoader generateCursorLoader() {
		CursorLoader cursorLoader = new CursorLoader(
				this.getActivity(),
				NearlingsContentProvider
						.contentURIbyTableName(UserReviewDatabaseHelper.TABLE_NAME),
				UserReviewDatabaseHelper.COLUMNS, null, null,
				UserReviewDatabaseHelper.COLUMN_ID + " DESC");
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
		this.getActivity().getActionBar().setHomeButtonEnabled(true);
		this.getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActivity().getActionBar().setDisplayShowHomeEnabled(true);
		this.getActivity().getActionBar().setDisplayShowTitleEnabled(true);
		// this.getActivity().getActionBar().setTitle("Messages");
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
		String selectionClause = NeedsDetailsDatabaseHelper.COLUMN_ID + " = ?";
		String[] mSelectionArgs = { "" };
		mSelectionArgs[0] = id;
		Cursor cursor = this.getActivity()
				.getContentResolver()
				.query(NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
						NeedsDetailsDatabaseHelper.COLUMNS, selectionClause,
						mSelectionArgs, null);
		cursor.moveToFirst();
		String user_id = cursor.getString(cursor.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_CREATED_BY));
		 b.putString(UserReviewsRequest.BUNDLE_ID, user_id);
		super.requestSync(b);
	}

	@Override
	public void setSourceRequestHelper() {
		 helpers.add(new UserReviewsRequest(this.getActivity()));
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

		this.mAdapter = new NeedsReviewsAdapter(this.getActivity(), c);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);
	}

	String TAG = "NeedBidFragment";
	String TAG2 = "NeedBidFragment";

}