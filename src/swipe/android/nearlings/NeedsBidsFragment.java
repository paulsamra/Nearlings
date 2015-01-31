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

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.json.needs.needsdetailsoffersresponse.JsonNeedsOffersResponse;
import swipe.android.nearlings.json.needs.needsdetailsoffersresponse.NeedsOffers;
import swipe.android.nearlings.jsonResponses.login.JsonBidsResponse;
import swipe.android.nearlings.jsonResponses.login.JsonLoginResponse;
import swipe.android.nearlings.jsonResponses.register.Bids;
import swipe.android.nearlings.jsonResponses.register.Users;
import swipe.android.nearlings.viewAdapters.BidsViewAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

//TODO: Probably want to abstract this
public class NeedsBidsFragment extends
		RefreshableListNonSwipeFragment<JsonNeedsOffersResponse> implements ActivityCallbackFromAdapter {
	protected ArrayAdapter<NeedsOffers> mAdapter;
	// Extra stuff
	String id;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		Bundle b = getActivity().getIntent().getExtras();
		id = b.getString("id");

		return view;
	}

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

	@Override
	public void onRefresh() {
		super.onRefresh();
		swipeView.setRefreshing(true);
		task = new GetDataWebTask<JsonNeedsOffersResponse>(this.getActivity(),
				this, JsonNeedsOffersResponse.class, false);
		Map<String, String> headers = SessionManager.getInstance(this.getActivity())
				.defaultSessionHeaders();
		task.execute(SessionManager.getInstance(this.getActivity()).loginURL(),
				MapUtils.mapToString(headers));
	}

	static ArrayList<NeedsOffers> u = new ArrayList<NeedsOffers>();

	@Override
	public void onTaskComplete(JsonNeedsOffersResponse result) {
		// TODO Auto-generated method stub
		if (result == null) {
			return;
		}
		u.clear();
		for (NeedsOffers offers : result.getOffers()) {
			u.add(offers);
		}
		this.mAdapter = new BidsViewAdapter(this.getActivity(),
				R.layout.bid_item, u);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);
		lView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				acceptNeedPurchase(position);
			}

		});
		swipeView.setRefreshing(false);
	}

	@Override
	public void onResume() {
		super.onResume();
		this.mAdapter = new BidsViewAdapter(this.getActivity(),
				R.layout.bid_item, u);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);
		swipeView.setRefreshing(false);
	}

	public void acceptNeedPurchase(int position) {
		Context context = this.getActivity();
		Cursor cursor = this.generateCursorLoader().loadInBackground();
NeedsOffers offer= u.get(position);
double price = offer.getOfferprice();
String item =  cursor.getString(cursor
		.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_TITLE));

		PayPalPayment thingToBuy = NearlingsApplication.generatePayObject(
				price, item, PayPalPayment.PAYMENT_INTENT_SALE);

		Intent intent = new Intent(context, PaymentActivity.class);

		// send the same configuration for restart resiliency
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,
				NearlingsApplication.config);

		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

		startActivityForResultBridge(intent,
				NearlingsApplication.REQUEST_CODE_PAYMENT);
	}	
	@Override
	public void startActivityForResultBridge(Intent i, int request_code) {

		startActivityForResult(i, NearlingsApplication.REQUEST_CODE_PAYMENT);
	}
String TAG  = "NeedBidFragment";
String TAG2  = "NeedBidFragment";
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
}