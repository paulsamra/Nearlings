package swipe.android.nearlings;

import org.json.JSONException;

import com.edbert.library.greyButton.GreyedOutButton;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsOfferDatabaseHelper;
import swipe.android.nearlings.MessagesSync.Needs;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.MessagesSync.NeedsExploreRequest;
import swipe.android.nearlings.MessagesSync.NeedsOffersRequest;
import swipe.android.nearlings.events.EventsContainerFragment;
import swipe.android.nearlings.sync.NearlingsSyncAdapter;
import swipe.android.nearlings.viewAdapters.NeedsDetailsViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.widget.Toast;

//need to check whether parent clas has sync. In fact, we just need to know how toa ccess it.
public class NeedsDetailsFragment extends NearlingsSwipeToRefreshFragment
		implements Refreshable {
	String id;
	View view;
	NeedsDetailsViewAdapter adapter;
	GreyedOutButton doActionButton;
	public static final String MESSAGES_START_FLAG = NeedsDetailsFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	public static final String MESSAGES_FINISH_FLAG = NeedsDetailsFragment.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";
	FragmentManager fm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		fm = this.getFragmentManager();
	}

	Bundle savedInstanceState;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to refrence our container class. For now, we should just
		// return a text message
		super.onCreateView(inflater, container, savedInstanceState);
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.needs_details, container, false);

		} catch (InflateException e) {
			e.printStackTrace();
		}
		doActionButton = (GreyedOutButton) view
				.findViewById(R.id.needs_change_state);
		doActionButton.setEnabled(false);
		this.savedInstanceState = savedInstanceState;
		Bundle b = getActivity().getIntent().getExtras();
		id = b.getString("id");

		reloadData();

		return view;

	}

	@Override
	public void onRefresh() {
		// resync data?
		super.onRefresh();
		// reloadAdapter();

	}

	@Override
	public void setSourceRequestHelper() {
		helpers.add(new NeedsDetailsRequest(this.getActivity()));
		helpers.add(new NeedsOffersRequest(this.getActivity()));
	}

	@Override
	public CursorLoader generateCursorLoader() {
		Log.i("CURSOR_ID_GEN", id);
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
		return cursorLoader;

	}

	Cursor c;

	@Override
	public void onResume() {
		super.onResume();
		// adapter = new NeedsDetailViewAdapter(view, this.getActivity(), id, c,
		// savedInstanceState);
		super.onRefresh();
		reloadAdapter();

	}

	@Override
	public void reloadData() {
		// TODO Auto-generated method stub

		// getLoaderManager().initLoader(0, null, this);
		reloadAdapter();
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
	public void reloadAdapter() {
		getLoaderManager().initLoader(0, null, this);
		c = generateCursor();
		if (adapter == null)
			adapter = new NeedsDetailsViewAdapter(view, this.getActivity(), id,
					c, savedInstanceState);

		adapter.reloadData();
	}

	@Override
	protected void updateRefresh(boolean isSyncing) {
		super.updateRefresh(isSyncing);
		if (isSyncing) {
			setButtonSyncing();
		} else {
			refreshStateButton();
		}

	}

	String TAG = "Details Fragment";
	String TAG2 = "Details Fragment Next";

	@Override
	public void requestSync(Bundle b) {
		b.putString(NeedsDetailsRequest.BUNDLE_ID, this.id);
		b.putString(NeedsOffersRequest.BUNDLE_ID, this.id);
		super.requestSync(b);
	}

	// BUTTON STUFF. Can move this section around
	// State button control
	public void disableFlowButton(String s) {
		disableFlowButton(s, Color.GRAY);
	}

	public void setButtonSyncing() {
		disableFlowButton("Retrieving info...");
	}

	public void refreshStateButton() {
		// disableFlowButton("Retrieving info...");
		c.requery();
		this.setUpRoles();
		setUpFlowButton();
	}

	public void disableFlowButton(String s, int bgColor) {
		doActionButton.setText(s);
		doActionButton.setEnabled(false);
		doActionButton.setBackgroundColor(bgColor);
		doActionButton.setTextColor(Color.WHITE);
	}

	public void assignedClickToMarkDone() {
		doActionButton.setText("Click to mark need completed.");
		doActionButton.setBackgroundColor(Color.GREEN);
		doActionButton.setTextColor(Color.WHITE);
		doActionButton.setEnabled(true);
	}

	public void flowButtonMakeOffer() {
		doActionButton.setText("Click to make an offer.");
		doActionButton.setBackgroundColor(this.getActivity().getResources()
				.getColor(R.color.nearlings_theme));
		doActionButton.setTextColor(Color.WHITE);
		doActionButton.setEnabled(true);
		doActionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(NeedsDetailsFragment.this
						.getActivity(), MakeOfferActivity.class);
				Bundle extras = intent.getExtras();
				int title_index = c
						.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_TITLE);
				String title = c.getString(title_index);
				extras.putString("title", title); 
				intent.putExtras(extras);
				startActivity(intent);
				//dont finish off
			}

		});
	}

	public void reviewAssignment() {
		doActionButton.setText("Need is done. Click to review.");
		doActionButton.setBackgroundColor(Color.GREEN);
		doActionButton.setTextColor(Color.WHITE);
		doActionButton.setEnabled(true);
	}

	boolean isCreator = false, offerIsAvailable = false, madeAnOffer = false;

	public void setUpRoles() {
		c.moveToFirst();
		int author_index = c
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_CREATED_BY);
		String authorID = c.getString(author_index);
		String userID = SessionManager.getInstance(this.getActivity())
				.getUserID();
		if (authorID.equals(userID)) {
			isCreator = true;
		} else {
			isCreator = false;
		}
		int offercount_index = c
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_OFFER_COUNT);
		int offerCount = c.getInt(offercount_index);
		if (offerCount > 0) {
			offerIsAvailable = true;
		} else {
			offerIsAvailable = false;
		}
		// /now query offers database
		Cursor offers = generateOffersCursor();
		if (offers.getCount() > 0) {
			madeAnOffer = true;
		} else {
			madeAnOffer = false;
		}
		// offer is available?
	}

	public void setUpFlowButton() {
		Cursor cursor = generateCursor();
		cursor.moveToFirst();
		int status_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_STATUS);

		String state = cursor.getString(status_index);
		Log.d("state", state);
		if (state.equals(Needs.AVAILABLE)) {
			// if we are the needer
			if (isCreator && offerIsAvailable) {
				/*
				 * Log.d("FLOW BUTTON",
				 * "An offer is available! Check the offers tab.");
				 */
				disableFlowButton("An offer is available! Check the offers tab.");
			} else if (isCreator && !offerIsAvailable) {
				disableFlowButton("Waiting for offers");

			} else if (madeAnOffer) {
				disableFlowButton("You've already made an offer.");
			} else if (isCreator) {
				disableFlowButton("Waiting for offers");
			} else {
				// make button avialble
				flowButtonMakeOffer();
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
		} else if (state.equals(Needs.CLOSED)) {
			// set as closed
			disableFlowButton("Need is closed.");
		} else {
			disableFlowButton("Oops! Couldn't load data. Try again.");
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

	public Cursor generateOffersCursor() {

		String selectionClause = NeedsOfferDatabaseHelper.COLUMN_CREATED_BY
				+ " = ?";
		String[] mSelectionArgs = { "" };
		mSelectionArgs[0] = SessionManager.getInstance(this.getActivity())
				.getUserID();

		Cursor c = this
				.getActivity()
				.getContentResolver()
				.query(

				NearlingsContentProvider
						.contentURIbyTableName(NeedsOfferDatabaseHelper.TABLE_NAME),
						NeedsOfferDatabaseHelper.COLUMNS, selectionClause,
						mSelectionArgs,
						NeedsOfferDatabaseHelper.COLUMN_ID + " DESC");
		return c;

	}
}
