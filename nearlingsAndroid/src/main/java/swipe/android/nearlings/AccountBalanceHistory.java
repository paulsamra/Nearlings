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
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
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

public class AccountBalanceHistory extends NearlingsActivity implements
		AsyncTaskCompleteListener<NearlingsResponse> {
	Button withdraw;
	TextView account_value,balance_value;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.balance_history);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle("History");

		withdraw = (Button) findViewById(R.id.withdraw_button);
		withdraw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showNumberPicker("0", 0);
			}

		});
		account_value = (TextView) findViewById(R.id.account_value);
		balance_value = (TextView) findViewById(R.id.balance_value);
		// create the TabHost that will contain the Tabs
		tabHost = (FragmentTabHost) findViewById(R.id.tabhost);
		mlam = new LocalActivityManager(this, false);
		mlam.dispatchCreate(savedInstanceState);
		tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

	//	tabHost.setup(mlam);
		withdraw.setEnabled(false);


		updateTabs();
		// update account value and history
		// retrieveAccountValue();
		retrieveHistory();
	}

	LocalActivityManager mlam;
	FragmentTabHost tabHost;

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
		JSONObject withdraw_json = new JSONObject();
		try {
			withdraw_json.put("paypal_email", address);
			withdraw_json.put("amount", amount);
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
				MapUtils.mapToString(headers), withdraw_json.toString());

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		onBackPressed();
		return true;
	}

	private void updateTabs() {

tabHost.clearAllTabs();
		tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

		Bundle arg1 = new Bundle();
		arg1.putString("type", "Processed");
		tabHost.addTab(
				tabHost.newTabSpec("Processed").setIndicator("Processed", null),
				FragmentProcessedPaymentList.class, arg1);

		Bundle arg2 = new Bundle();
		arg2.putString("type", "Pending");
		tabHost.addTab(
				tabHost.newTabSpec("Pending").setIndicator("Pending", null).setContent(new Intent().putExtra("type", "Pending")),
				FragmentProcessedPaymentList.class, arg2);


		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		{
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			tv.setTextColor(Color.parseColor("#000000"));
		}
		/*
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

		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		{
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
			tv.setTextColor(Color.parseColor("#000000"));
		}*/
	}

	private void updateAccountValue() {
		account_value
				.setText("$"
						+ String.valueOf(SessionManager.getInstance(this)
								.getBalance()));

		this.balance_value
				.setText("$"
						+ String.valueOf(SessionManager.getInstance(this)
						.getAvailableBalance()));
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
				//updateTabs();
				SessionManager.getInstance(this).setBalance(
						history.getDetails().getBalance());

				SessionManager.getInstance(this).setAvailableBalance(
						history.getDetails().getAvailable());
				withdraw.setEnabled(true);
				updateAccountValue();
				return;

			} else {
				// withdraw money response

				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								dialog.cancel();
								//retrieveHistory();
							}
						});
				builder.setTitle("Success");
				builder.setMessage("Successfully withdrew!");
				builder.show();

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