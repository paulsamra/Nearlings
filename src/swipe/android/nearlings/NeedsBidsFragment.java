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
import swipe.android.nearlings.MessagesSync.MessagesRequest;
import swipe.android.nearlings.MessagesSync.Needs;
import swipe.android.nearlings.MessagesSync.NeedsOffersRequest;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

//TODO: Probably want to abstract this
public class NeedsBidsFragment extends NearlingsSwipeToRefreshFragment
		implements Refreshable {

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

		b.putString(NeedsOffersRequest.BUNDLE_ID, this.id);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String selectionClause = NeedsDetailsDatabaseHelper.COLUMN_ID + " = ?";
		String[] selectionArgs = { "" };
		selectionArgs[0] = this.id;
		Cursor cursor = this.getActivity()
				.getContentResolver()
				.query(NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
						NeedsDetailsDatabaseHelper.COLUMNS, selectionClause,
						selectionArgs, null);
		cursor.moveToFirst();
		int status_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_STATUS);

		String state = cursor.getString(status_index);

		if(!state.equals(Needs.AVAILABLE) ){
			return;
		}
		Cursor cursorOfBids = generateCursor();
		cursorOfBids.moveToFirst();
		
		double price = Double.valueOf(cursorOfBids.getString(cursorOfBids.getColumnIndex(NeedsOfferDatabaseHelper.COLUMN_OFFER_PRICE)));
		String id_of_doer = cursorOfBids.getString(cursorOfBids.getColumnIndex(NeedsOfferDatabaseHelper.COLUMN_CREATED_BY));
		
		
		
		String item = cursor.getString(cursor.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_TITLE));

		PayPalPayment thingToBuy = NearlingsApplication.generatePayObject(
				price, item, PayPalPayment.PAYMENT_INTENT_SALE);

		Intent intent = new Intent(this.getActivity(), PaymentActivity.class);

		// send the same configuration for restart resiliency
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,
				NearlingsApplication.paypalConfig);

		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

		intent.putExtra(NearlingsApplication.DOER_ID, "TESTING");

		//Log.d("HI",this.getActivity().getClass().getSimpleName());
		((NeedsDetailsActivity) this.getActivity()).setDoerID(id_of_doer);
		this.getActivity().startActivityForResult(intent,
				NearlingsApplication.REQUEST_CODE_PAYMENT);

	}
	
}