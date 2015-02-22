package swipe.android.nearlings;

import java.util.Map;

import org.json.JSONObject;

import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.json.cancelOffer.CancelOfferResponse;
import swipe.android.nearlings.json.changeStateResponse.MarkAsAssignedResponse;
import swipe.android.nearlings.json.changeStateResponse.MarkAsForReviewResponse;
import swipe.android.nearlings.json.jsonSubmitReviewResponse.JsonSubmitReviewResponse;
import swipe.android.nearlings.json.jsonoffersresponse.JsonMakeOffersResponse;
import swipe.android.nearlings.viewAdapters.MakeOfferFormAdapter;
import swipe.android.nearlings.viewAdapters.SubmitReviewFormAdapter;
import swipe.android.nearlings.viewAdapters.SubmitToBeDoneFormAdapter;
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
import com.edbert.library.network.PutDataWebTask;
import com.edbert.library.utils.MapUtils;

public class SubmitForCompletionActivity extends FragmentActivity implements
		AsyncTaskCompleteListener<MarkAsForReviewResponse> {

	SubmitToBeDoneFormAdapter makeFormAdapter;
	TextView finishLabel;
	GreyedOutButton makeOffer;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("Finish Task");

		setContentView(R.layout.submit_for_completion_layout);
		makeOffer = (GreyedOutButton) findViewById(R.id.needs_change_state);
		makeOffer.setText("Request Review");
		makeOffer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submitForCompletion();
			}

		});
		id = getIntent().getExtras().getString("id");

		String title = getIntent().getExtras().getString("title");
		makeFormAdapter = new SubmitToBeDoneFormAdapter(this, getWindow()
				.getDecorView().findViewById(android.R.id.content),
				savedInstanceState, id, title);
	}

	String id = "";

	// construct the body
	public void submitForCompletion() {
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();
		String url = SessionManager.getInstance(this).changeStateURL(id);
		String body ="";
		try {
			JSONObject jsonRep = makeFormAdapter.getJSONObject();
			//need to get the other guy
			jsonRep.put("status", "review");
			body = jsonRep.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		new PutDataWebTask<MarkAsForReviewResponse>(
				this, MarkAsForReviewResponse.class,
				true).execute(
				SessionManager.getInstance(this)
						.changeStateURL(id), MapUtils.mapToString(headers),
				body.toString());
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		onBackPressed();
		return true;
	}

	@Override
	public void onTaskComplete(MarkAsForReviewResponse result) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (result.isValid()) {

			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					dialog.cancel();
					SubmitForCompletionActivity.this.finish();
				}
			});
			builder.setTitle("Success!");
			builder.setMessage("You have sucessfully marked your task as complete.");
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
		AlertDialog alert = builder.create();
		alert.show();
	}

}