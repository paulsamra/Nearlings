package swipe.android.nearlings;

import java.util.Map;

import org.json.JSONException;

import com.edbert.library.greyButton.GreyedOutButton;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;
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
import swipe.android.nearlings.MessagesSync.UserReviewsRequest;
import swipe.android.nearlings.events.EventsContainerFragment;
import swipe.android.nearlings.json.addCommentsResponse.JsonAddCommentsResponse;
import swipe.android.nearlings.sync.NearlingsSyncAdapter;
import swipe.android.nearlings.viewAdapters.NeedsDetailsViewAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
		implements Refreshable{
	String id;
	View view;
	NeedsDetailsViewAdapter adapter;
	// GreyedOutButton doActionButton;

	public static final String MESSAGES_START_FLAG = NeedsDetailsActivity.MESSAGES_START_FLAG;
	public static final String MESSAGES_FINISH_FLAG = NeedsDetailsActivity.MESSAGES_FINISH_FLAG;
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
		/*
		 * doActionButton = (GreyedOutButton) view
		 * .findViewById(R.id.needs_change_state);
		 * doActionButton.setEnabled(false);
		 */
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
		helpers.add(new UserReviewsRequest(this.getActivity()));
	}

	@Override
	public CursorLoader generateCursorLoader() {

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
			// setButtonSyncing();
		} else {
			// refreshStateButton();
		}

	}

	String TAG = "Details Fragment";
	String TAG2 = "Details Fragment Next";

	@Override
	public void requestSync(Bundle b) {
		b.putString(NeedsDetailsRequest.BUNDLE_ID, this.id);
		b.putString(NeedsOffersRequest.BUNDLE_ID, this.id);
		c = generateCursor();
		c.moveToFirst();
		String user_id = c.getString(c.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_CREATED_BY));
		b.putString(UserReviewsRequest.BUNDLE_ID, user_id);
		b.putString(UserReviewsRequest.BUNDLE_ID, user_id);
		super.requestSync(b);
	}

	

}
