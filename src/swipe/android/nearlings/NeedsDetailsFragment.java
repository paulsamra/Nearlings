package swipe.android.nearlings;


import org.json.JSONException;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.viewAdapters.NeedsDetailsViewAdapter;

import android.app.Activity;
import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
//need to check whether parent clas has sync. In fact, we just need to know how toa ccess it.
public class NeedsDetailsFragment extends NearlingsSwipeToRefreshFragment
		implements Refreshable, ActivityCallbackFromAdapter {
	String id;
	View view;
	NeedsDetailsViewAdapter adapter;

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

			// view = inflater.inflate(R.layout.needs_details, container,
			// false);
		} catch (InflateException e) {

		}

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
		super.helper = new NeedsDetailsRequest(this.getActivity(), id);
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
				mSelectionArgs, NeedsDetailsDatabaseHelper.COLUMN_DATE
						+ " DESC");
		return cursorLoader;

	}

	Cursor c;

	@Override
	public void onResume() {
		super.onResume();
		// adapter = new NeedsDetailViewAdapter(view, this.getActivity(), id, c,
		// savedInstanceState);
		reloadAdapter();

	}

	@Override
	public void reloadData() {
		// TODO Auto-generated method stub

		Log.e("ReloadAdapter", "adapter");
		// getLoaderManager().initLoader(0, null, this);
		reloadAdapter();

		// dapter = new NeedsDetailViewAdapter(view, this.getActivity(), id,
		// savedInstanceState);

		/*
		 * mAdapter = new NeedsDetailViewAdapter(this.getActivity(), id, c,
		 * savedInstanceState);
		 * 
		 * // this.mAdapter = new NeedsDetailViewAdapter(this.getActivity(), c);
		 * 
		 * mAdapter.();
		 */
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
					c, savedInstanceState, this);

		adapter.reloadData();
	}

	String TAG = "Details Fragment";
	String TAG2 = "Details Fragment Next";

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NearlingsApplication.REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						Log.i(TAG, confirm.toJSONObject().toString(4));
						Log.i(TAG2, confirm.getPayment().toJSONObject()
								.toString(4));
						
						/**
						 * TODO: send 'confirm' (and possibly
						 * confirm.getPayment() to your server for verification
						 * or consent completion. See
						 * https://developer.paypal.com
						 * /webapps/developer/docs/integration
						 * /mobile/verify-mobile-payment/ for more details.
						 * 
						 * For sample mobile backend interactions, see
						 * https://github
						 * .com/paypal/rest-api-sdk-python/tree/master
						 * /samples/mobile_backend
						 */
						Toast.makeText(
								this.getActivity().getApplicationContext(),
								"PaymentConfirmation info received from PayPal",
								Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e(TAG, "an extremely unlikely failure occurred: ",
								e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i(TAG,
						"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}

		}
	}

	@Override
	public void startActivityForResultBridge(Intent i, int request_code) {

		startActivityForResult(i, NearlingsApplication.REQUEST_CODE_PAYMENT);
	}

}
