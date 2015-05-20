package swipe.android.nearlings;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import swipe.android.nearlings.core.AmountPicker;
import swipe.android.nearlings.json.NearlingsResponse;
import swipe.android.nearlings.json.JsonWithdrawMoneyResponse.JsonWithdrawMoneyResponse;
import swipe.android.nearlings.json.UserHistory.JsonTransactionHistoryResponse;
import swipe.android.nearlings.json.UserHistory.PaymentHistory;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.GetDataWebTask;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;
import com.google.gson.JsonObject;

public class AccountBalanceHistory extends NearlingsActivity implements
		AsyncTaskCompleteListener<NearlingsResponse> {
	Button withdraw;
	TextView account_balance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.balance_history);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("History");

		withdraw = (Button) findViewById(R.id.withdraw_button);
		withdraw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showNumberPicker("0", 0);
			}

		});
		account_balance = (TextView) findViewById(R.id.account_value);
		// create the TabHost that will contain the Tabs
		tabHost = (TabHost) findViewById(R.id.tabhost);
		mlam = new LocalActivityManager(this, false);
		mlam.dispatchCreate(savedInstanceState);

		tabHost.setup(mlam);
		withdraw.setEnabled(false);
		// update account value and history
		// retrieveAccountValue();
		retrieveHistory();
	}

	LocalActivityManager mlam;
	TabHost tabHost;

	private void showNumberPicker(String number, int mode) {
		String nowNumber = "0.00";
		if (!number.equals("")) {
			nowNumber = number;
		}
		ap = new AmountPicker(this, listener, nowNumber, mode);
		ap.show();
	}

	AmountPicker ap;
	private AmountPicker.OnMyNumberSetListener listener = new AmountPicker.OnMyNumberSetListener() {
		@Override
		public void onNumberSet(String number, int mode, String address) {
			if (number.equals("") || number.equals(".")) {
				return;
			}
			if (!address.contains("@")) {
				ap.cancel();
				showInvalidAddressDialog();

			}
			withdrawAmount(Float.valueOf(number), address);
		}
	};

	private void showInvalidAddressDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Paypal Address Error!");

		// set dialog message
		alertDialogBuilder.setMessage("You must enter a valid paypal address!")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public void withdrawAmount(float amount, String address) {
		try {
			JSONObject withdraw = new JSONObject();
			withdraw.put("paypal_email", address);
			withdraw.put("amount", amount);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		String url = SessionManager.getInstance(this).withdrawMoneyURL(
				String.valueOf(SessionManager.getInstance(this).getUserID()));
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();

		new PostDataWebTask<JsonWithdrawMoneyResponse>(this,
				JsonWithdrawMoneyResponse.class, true).execute(url,
				MapUtils.mapToString(headers));

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		onBackPressed();
		return true;
	}

	private void updateTabs(ArrayList<PaymentHistory> listProcessed,
			ArrayList<PaymentHistory> listPending) {

		TabSpec tab1 = tabHost.newTabSpec("Processed");
		TabSpec tab2 = tabHost.newTabSpec("Pending");

		// Set the Tab name and Activity
		// that will be opened when particular Tab will be selected
		tab1.setIndicator("Processed");
		Intent intent1 = new Intent(this, PaymentHistoryListActivity.class);
		intent1.putParcelableArrayListExtra("list", listProcessed);
		tab1.setContent(intent1);

		tab2.setIndicator("Pending");
		Intent intent2 = new Intent(this, PaymentHistoryListActivity.class);
		intent2.putParcelableArrayListExtra("list", listPending);
		tab2.setContent(intent2);

		tabHost.addTab(tab1);
		tabHost.addTab(tab2);

	}

	private void updateAccountValue() {
		account_balance
				.setText("$"
						+ String.valueOf(SessionManager.getInstance(this)
								.getBalance()));
	}

	@Override
	public void onTaskComplete(NearlingsResponse result) {

		if (result == null) {
			NearlingsApplication.displayNetworkNotAvailableDialog(this);
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (result.isValid()) {
			if (result instanceof JsonTransactionHistoryResponse) {
				// insert into our thing
				JsonTransactionHistoryResponse history = (JsonTransactionHistoryResponse) result;
				updateTabs(history.getDetails().getPosted(), history
						.getDetails().getPending());
				SessionManager.getInstance(this).setBalance(
						history.getDetails().getBalance());
				withdraw.setEnabled(true);
				updateAccountValue();
				return;

			} else {
				// withdraw money response

				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								dialog.cancel();
								retrieveHistory();
							}
						});
				builder.setTitle("Error");
				builder.setMessage("Successfully withdrew!");

			}
		} else {

			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							dialog.cancel();
						}
					});
			builder.setTitle("Error");
			builder.setMessage(result.getError());
			builder.show();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		mlam.dispatchResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		mlam.dispatchPause(isFinishing());
	}

	private void retrieveHistory() {
		withdraw.setEnabled(false);
		String url = SessionManager.getInstance(this).userHistoryURL(
				String.valueOf(SessionManager.getInstance(this).getUserID()));
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();

		new GetDataWebTask<JsonTransactionHistoryResponse>(this,
				JsonTransactionHistoryResponse.class, true).execute(url,
				MapUtils.mapToString(headers));
	}
}