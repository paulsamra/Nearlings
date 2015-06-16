package swipe.android.nearlings;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import de.metagear.android.view.ValidatingEditText;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.json.NearlingsResponse;
import swipe.android.nearlings.json.changeStateResponse.MarkAsAssignedResponse;
import swipe.android.nearlings.json.jsonSubmitReviewResponse.JsonSubmitReviewResponse;
import swipe.android.nearlings.viewAdapters.SubmitReviewFormAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.edbert.library.greyButton.GreyedOutButton;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.network.PutDataWebTask;
import com.edbert.library.utils.MapUtils;

public class SubmitReviewOfUserActivity extends ActionBarActivity implements
		AsyncTaskCompleteListener<NearlingsResponse> {

	SubmitReviewFormAdapter makeFormAdapter;
	TextView finishLabel;
	GreyedOutButton makeOffer;
	boolean close = false;

	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle("Review User");

		setContentView(R.layout.submit_for_review_layout);

		TextView description_label = (TextView) findViewById(R.id.description_label);
		description_label.setText("Comment");
		ValidatingEditText description= (ValidatingEditText) findViewById(R.id.descriptionBox);
		description.setHint("Comment");
		makeOffer = (GreyedOutButton) findViewById(R.id.needs_change_state);
		makeOffer.setText("Submit Review");
		makeOffer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submitForReview();
			}

		});
		id = getIntent().getExtras().getString("need_id");
		doer_id = getIntent().getExtras().getString("assigned_to");
		creator_id = getIntent().getExtras().getString("created_by");
		close = getIntent().getExtras().getBoolean("close");
		String title = getIntent().getExtras().getString("title");
		makeFormAdapter = new SubmitReviewFormAdapter(this, getWindow()
				.getDecorView().findViewById(android.R.id.content),
				savedInstanceState, id, title);
	}

	String id = "", doer_id = "", creator_id="";

	// construct the body
	public void submitForReview() {
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();
		//we want to submit for the other person
		String id_of_url = creator_id;
		//creator and doer are different
		if(creator_id.equals(SessionManager.getInstance(this).getUserID())){
			id_of_url = doer_id;
		}
		String url = SessionManager.getInstance(this).submitReviewURL(id_of_url);
		Log.d("url", url);
		String body = "";
		try {
			JSONObject jsonRep = makeFormAdapter.getJSONObject();
			// need to get the other guy
			String selectionClause = NeedsDetailsDatabaseHelper.COLUMN_ID
					+ " = ?";
			String[] selectionArgs = { "" };
			selectionArgs[0] = this.id;
			Cursor cursor = this
					.getContentResolver()
					.query(NearlingsContentProvider
									.contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
							NeedsDetailsDatabaseHelper.COLUMNS,
							selectionClause, selectionArgs, null);
			cursor.moveToFirst();
			String doer = cursor
					.getString(cursor
							.getColumnIndex(NeedsDetailsDatabaseHelper.COLUMN_ASSIGNED_TO));
			jsonRep.put("doer_id", SessionManager.getInstance(this).getUserID());
			jsonRep.put("need_id", id);
			body = jsonRep.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("Body", body);
		new PostDataWebTask<JsonSubmitReviewResponse>(this,
				JsonSubmitReviewResponse.class).execute(url,
				MapUtils.mapToString(headers), body);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		onBackPressed();
		return true;
	}

	@Override
	public void onTaskComplete(NearlingsResponse result) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (result != null && result.isValid()) {
			if (result instanceof JsonSubmitReviewResponse && close && creator_id.equals(SessionManager.getInstance(this).getUserID())) {
				sendCloser();
				return;
			} else {
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								dialog.cancel();
								SubmitReviewOfUserActivity.this.finish();
							}
						});
				builder.setTitle("Success!");
				builder.setMessage("You have sucessfully submitted the review.");
			}
		} else {

			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							dialog.cancel();
							// MakeOfferActivity.this.finish();
						}
					});
			builder.setTitle("Error");
			builder.setMessage(result.getError());
		}
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void sendCloser() {

		try {
			JSONObject body = new JSONObject();
			body.put("doer_id", doer_id);
			body.put("status", "closed");
			Map<String, String> headers = SessionManager.getInstance(this)
					.defaultSessionHeaders();

			//we submit review of the other person
			new PutDataWebTask<MarkAsAssignedResponse>(this,
					MarkAsAssignedResponse.class, true).execute(SessionManager
					.getInstance(this).changeStateURL(id), MapUtils
					.mapToString(headers), body.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}