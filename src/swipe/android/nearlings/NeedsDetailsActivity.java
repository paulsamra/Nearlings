package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsOfferDatabaseHelper;
import swipe.android.nearlings.MessagesSync.Needs;
import swipe.android.nearlings.MessagesSync.NeedsCommentsRequest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;

import com.edbert.library.containers.TabsActivityContainer;
import com.edbert.library.greyButton.GreyedOutButton;

public class NeedsDetailsActivity extends TabsActivityContainer {
	String id = "0";

	Menu menu;
	GreyedOutButton doActionButton;
	public static final String MESSAGES_START_FLAG = NeedsDetailsActivity.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	public static final String MESSAGES_FINISH_FLAG = NeedsDetailsActivity.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_frame_with_button);
		ActionBar ab = this.getSupportActionBar();
		ab.setDisplayShowTitleEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);

		doActionButton = (GreyedOutButton) findViewById(R.id.needs_change_state);
		doActionButton.setEnabled(false);
		Bundle b = getIntent().getExtras();
		id = b.getString("id");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.refresh_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.refresh_details:
			// for dev purpose add comment
			NeedsCommentsRequest rq = new NeedsCommentsRequest(this);
			rq.writeToDatabase(null, this, null);
			int pos = this.getSupportActionBar().getSelectedTab().getPosition();
			String tag = createTag(super.mapFragList.getValue(pos));

			Refreshable f = ((Refreshable) getSupportFragmentManager()
					.findFragmentByTag(tag));
			f.onRefresh();
			break;
		// refresh the child fragment
		default:
			onBackPressed();

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void addDefaultFragments() {
		mapFragList.put("Details", new NeedsDetailsFragment());
		mapFragList.put("Offers", new NeedsBidsFragment());
		mapFragList.put("Reviews", new NeedsReviewsFragment());
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(
				onFinishSyncReceiver, new IntentFilter(MESSAGES_FINISH_FLAG));
		LocalBroadcastManager.getInstance(this).registerReceiver(
				onStartSyncReceiver, new IntentFilter(MESSAGES_START_FLAG));
	}

	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				onFinishSyncReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				onStartSyncReceiver);

	}

	//

	private BroadcastReceiver onFinishSyncReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			onReceiveFinish(context, intent);
		}
	};

	private BroadcastReceiver onStartSyncReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			onReceiveStart(context, intent);
		}
	};

	protected void onReceiveStart(Context context, Intent intent) {
		updateRefresh(true);
	}

	protected void onReceiveFinish(Context context, Intent intent) {
		c = generateCursor();
		updateRefresh(false);

	}

	protected void updateRefresh(boolean isSyncing) {

		if (isSyncing) {
			setButtonSyncing();
		} else {
			refreshStateButton();
		}

	}

	Cursor c;

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
		c = generateCursor();
		c.moveToFirst();
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
		doActionButton.setBackgroundColor(this.getResources().getColor(
				R.color.nearlings_theme));
		doActionButton.setTextColor(Color.WHITE);
		doActionButton.setEnabled(true);
		doActionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(NeedsDetailsActivity.this,
						MakeOfferActivity.class);
				Bundle extras = new Bundle();
				int title_index = c
						.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_TITLE);
				String title = c.getString(title_index);
				extras.putString("title", title);
				int id_index = c
						.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_ID);
				String id = c.getString(id_index);
				extras.putString("id", id);

				intent.putExtras(extras);
				startActivity(intent);

				// dont finish off
			}

		});
	}

	public void clickToCancelOffer() {

		doActionButton.setText("Cancel your offer.");
		doActionButton.setBackgroundColor(this.getResources().getColor(
				R.color.nearlings_theme));
		doActionButton.setTextColor(Color.RED);
		doActionButton.setEnabled(true);
		doActionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// launch the cancel

				// dont finish off
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
		String userID = SessionManager.getInstance(this).getUserID();
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

		if (state.equals(Needs.AVAILABLE)) {
			// if we are the needer
			Log.d("Available", "Available");
			if (isCreator && offerIsAvailable) {
				disableFlowButton("An offer is available! Check the offers tab.");
			} else if (isCreator && !offerIsAvailable) {
				disableFlowButton("Waiting for offers");
			} else if (madeAnOffer) {
				// disableFlowButton("You've already made an offer.");
				clickToCancelOffer();
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

	}

	public Cursor generateOffersCursor() {

		String selectionClause = NeedsOfferDatabaseHelper.COLUMN_CREATED_BY
				+ " = ?";
		String[] mSelectionArgs = { "" };
		mSelectionArgs[0] = SessionManager.getInstance(this).getUserID();

		Cursor c = this
				.getContentResolver()
				.query(

				NearlingsContentProvider
						.contentURIbyTableName(NeedsOfferDatabaseHelper.TABLE_NAME),
						NeedsOfferDatabaseHelper.COLUMNS, selectionClause,
						mSelectionArgs,
						NeedsOfferDatabaseHelper.COLUMN_ID + " DESC");
		return c;

	}

	public Cursor generateCursor() {
		String selectionClause = NeedsDetailsDatabaseHelper.COLUMN_ID + " = ?";
		String[] mSelectionArgs = { "" };
		mSelectionArgs[0] = id;// SessionManager.getInstance(this).getUserID();

		Cursor c = this
				.getContentResolver()
				.query(NearlingsContentProvider
						.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
						NeedsDetailsDatabaseHelper.COLUMNS, selectionClause,
						mSelectionArgs,
						NeedsDetailsDatabaseHelper.COLUMN_DUE_DATE + " DESC");
		return c;

	}

}
