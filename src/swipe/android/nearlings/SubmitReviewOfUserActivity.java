package swipe.android.nearlings;

import java.util.Map;

import org.json.JSONObject;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.json.cancelOffer.CancelOfferResponse;
import swipe.android.nearlings.json.jsonSubmitReviewResponse.JsonSubmitReviewResponse;
import swipe.android.nearlings.json.jsonoffersresponse.JsonMakeOffersResponse;
import swipe.android.nearlings.viewAdapters.MakeOfferFormAdapter;
import swipe.android.nearlings.viewAdapters.SubmitReviewFormAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.edbert.library.greyButton.GreyedOutButton;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;

public class SubmitReviewOfUserActivity extends FragmentActivity implements
		AsyncTaskCompleteListener<JsonSubmitReviewResponse> {

	SubmitReviewFormAdapter makeFormAdapter;
	TextView finishLabel;
	GreyedOutButton makeOffer;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("Review User");

		setContentView(R.layout.submit_for_review_layout);
		finishLabel = (TextView) findViewById(R.id.when_label);
		finishLabel.setText("Time Finish");
		makeOffer = (GreyedOutButton) findViewById(R.id.needs_change_state);
		makeOffer.setText("Make Offer");
		makeOffer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submitForReview();
			}

		});
		id = getIntent().getExtras().getString("id");

		String title = getIntent().getExtras().getString("title");
		makeFormAdapter = new SubmitReviewFormAdapter(this, getWindow()
				.getDecorView().findViewById(android.R.id.content),
				savedInstanceState, id, title);
	}

	String id = "";

	// construct the body
	public void submitForReview() {
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();
		String url = SessionManager.getInstance(this).submitReviewURL(id);
		String body ="";
		try {
			JSONObject jsonRep = makeFormAdapter.getJSONObject();
			//need to get the other guy
			String selectionClause = NeedsDetailsDatabaseHelper.COLUMN_ID + " = ?";
			String[] selectionArgs = { "" };
			selectionArgs[0] = this.id;
			Cursor cursor = this
					.getContentResolver()
					.query(NearlingsContentProvider
							.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
							NeedsDetailsDatabaseHelper.COLUMNS, selectionClause,
							selectionArgs, null);
			cursor.moveToFirst();
			String doer= cursor.getString(cursor.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_ASSIGNED_TO));
			jsonRep.put("doer_id",doer);
			jsonRep.put("need_id", id);
			body = jsonRep.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		new PostDataWebTask<JsonSubmitReviewResponse>(this,
				JsonSubmitReviewResponse.class).execute(url, MapUtils
				.mapToString(headers), body);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		onBackPressed();
		return true;
	}

	@Override
	public void onTaskComplete(JsonSubmitReviewResponse result) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (result.isValid()) {

			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					dialog.cancel();
					SubmitReviewOfUserActivity.this.finish();
				}
			});
			builder.setTitle("Success!");
			builder.setMessage("You have sucessfully submitted the review.");
		}else{

			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					dialog.cancel();
					//MakeOfferActivity.this.finish();
				}
			});
			builder.setTitle("Error");
			builder.setMessage(result.getError());
		}
		
	}

}