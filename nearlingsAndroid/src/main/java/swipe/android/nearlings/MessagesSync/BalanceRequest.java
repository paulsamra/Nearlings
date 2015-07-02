package swipe.android.nearlings.MessagesSync;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.edbert.library.network.SocketOperator;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import swipe.android.DatabaseHelpers.BalanceDatabaseHelper;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;
import swipe.android.nearlings.NearlingsRequest;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.json.UserHistory.JsonTransactionHistoryResponse;
import swipe.android.nearlings.json.UserHistory.PaymentHistory;
import swipe.android.nearlings.json.alerts.Alerts;
import swipe.android.nearlings.json.alerts.JsonMessagesResponse;
import swipe.android.nearlings.sync.NearlingsSyncAdapter;

public class BalanceRequest extends NearlingsRequest <JsonTransactionHistoryResponse>{
	public BalanceRequest(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	public static final String SEARCH_PARAMS_TAG = "SEARCH_PARAMS_TAG";

	// we will have a base URL as wel
	@Override
	public JsonTransactionHistoryResponse makeRequest(Bundle b) {
	Map<String, String> headers = SessionManager.getInstance(c)
				.defaultSessionHeaders();
		String url = SessionManager.getInstance(c).userHistoryURL(
				String.valueOf(SessionManager.getInstance(c).getUserID()));
		if(b.getInt(NearlingsSyncAdapter.LIMIT)>0){
			int page_number =  (b.getInt(NearlingsSyncAdapter.LIMIT) / (SessionManager.SEARCH_LIMIT))+1;
			url +=( "?page=" +page_number);
			url += ("&limit="+ (SessionManager.SEARCH_LIMIT));

		}else{
			url +=( "?limit=" + (SessionManager.SEARCH_LIMIT));
		}
		Object o = SocketOperator.getInstance(JsonTransactionHistoryResponse.class).getResponse(c, url,
				headers);
		if (o == null)
			return null;

		return (JsonTransactionHistoryResponse) o;
	
	}

	@Override
	public Class getJSONclass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeToDatabase(Bundle b,Context c, JsonTransactionHistoryResponse o) {
		// for now we will write random dummy stuff to the database

		if (o == null)
			return false;
if(b.getBoolean(NearlingsSyncAdapter.CLEAR_DB)) {
	Log.d("CLEAR DB", "CLEAR DB");
		NearlingsContentProvider
				.clearSingleTable(new BalanceDatabaseHelper());
}
		List<ContentValues> mValueList = new LinkedList<ContentValues>();
		for (int i = 0; i < o.getDetails().getPending().size(); i++) {


			PaymentHistory a = o.getDetails().getPending().get(i);
			ContentValues cv = new ContentValues();

			cv.put(BalanceDatabaseHelper.LIST_TYPE, "Pending");
			cv.put(BalanceDatabaseHelper.PAYMENT_ID, a.getPayment_id());
			cv.put(BalanceDatabaseHelper.BUYER_ID, a.getBuyer_id());
			cv.put(BalanceDatabaseHelper.SELLER_ID,a.getSeller_id());
			cv.put(BalanceDatabaseHelper.TITLE,a.getTitle());
			cv.put(BalanceDatabaseHelper.CREATED_AT,a.getCreated_at());
			cv.put(BalanceDatabaseHelper.STATUS,a.getStatus());
			cv.put(BalanceDatabaseHelper.TXN_TYPE,a.getTxn_type());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_RECIPIENT_USERNAME,a.getTxn_details().getRecipient_username());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_RECIPIENT_NAME,a.getTxn_details().getRecipient_name());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_task_id,a.getTxn_details().getTask_id());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_task_title,a.getTxn_details().getTask_title());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_task_cost,a.getTxn_details().getTask_cost());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_fee,a.getTxn_details().getFee());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_total_charge,a.getTxn_details().getTotal_charge());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_paypal_email,a.getTxn_details().getPaypal_email());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_amount_deposited,a.getTxn_details().getAmount_deposited());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_total_amount,a.getTxn_details().getTotal_amount());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_earnings,a.getTxn_details().getEarnings());


			c.getContentResolver()
					.insert(NearlingsContentProvider
							.contentURIbyTableName(BalanceDatabaseHelper.TABLE_NAME),
							cv);

			
			
			mValueList.add(cv);

		}
		for (int i = 0; i < o.getDetails().getPosted().size(); i++) {


			PaymentHistory a = o.getDetails().getPosted().get(i);
			ContentValues cv = new ContentValues();

			cv.put(BalanceDatabaseHelper.LIST_TYPE, "Processed");
			cv.put(BalanceDatabaseHelper.PAYMENT_ID, a.getPayment_id());
			cv.put(BalanceDatabaseHelper.BUYER_ID, a.getBuyer_id());
			cv.put(BalanceDatabaseHelper.SELLER_ID,a.getSeller_id());
			cv.put(BalanceDatabaseHelper.TITLE,a.getTitle());
			cv.put(BalanceDatabaseHelper.CREATED_AT,a.getCreated_at());
			cv.put(BalanceDatabaseHelper.STATUS,a.getStatus());
			cv.put(BalanceDatabaseHelper.TXN_TYPE,a.getTxn_type());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_RECIPIENT_USERNAME,a.getTxn_details().getRecipient_username());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_RECIPIENT_NAME,a.getTxn_details().getRecipient_name());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_task_id,a.getTxn_details().getTask_id());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_task_title,a.getTxn_details().getTask_title());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_task_cost,a.getTxn_details().getTask_cost());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_fee,a.getTxn_details().getFee());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_total_charge,a.getTxn_details().getTotal_charge());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_paypal_email,a.getTxn_details().getPaypal_email());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_amount_deposited,a.getTxn_details().getAmount_deposited());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_total_amount,a.getTxn_details().getTotal_amount());
			cv.put(BalanceDatabaseHelper.TXN_DETAIL_earnings,a.getTxn_details().getEarnings());


			c.getContentResolver()
					.insert(NearlingsContentProvider
									.contentURIbyTableName(BalanceDatabaseHelper.TABLE_NAME),
							cv);



			mValueList.add(cv);

		}
		ContentValues[] bulkToInsert;
		bulkToInsert = new ContentValues[mValueList.size()];
		mValueList.toArray(bulkToInsert);
		c.getContentResolver()
				.bulkInsert(
						NearlingsContentProvider
								.contentURIbyTableName(BalanceDatabaseHelper.TABLE_NAME),
						bulkToInsert);

		return true;
		
	}

	// for testing purposes
	public static int counter = 0;
}