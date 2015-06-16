package swipe.android.nearlings;

import java.util.Map;

import org.json.JSONObject;

import de.metagear.android.view.ValidatingEditText;
import swipe.android.nearlings.json.changeStateResponse.MarkAsForReviewResponse;
import swipe.android.nearlings.viewAdapters.SubmitToBeDoneFormAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.edbert.library.greyButton.GreyedOutButton;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PutDataWebTask;
import com.edbert.library.utils.MapUtils;

public class SubmitForCompletionActivity extends ActionBarActivity implements
		AsyncTaskCompleteListener<MarkAsForReviewResponse> {

	SubmitToBeDoneFormAdapter makeFormAdapter;
	TextView finishLabel;
	Button makeOffer;

	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle("Finish Task");

		setContentView(R.layout.submit_for_completion_layout);
		TextView description_label = (TextView) findViewById(R.id.description_label);
		description_label.setText("Comment");
		ValidatingEditText description= (ValidatingEditText) findViewById(R.id.descriptionBox);
		description.setHint("Comment");
		makeOffer = (Button) findViewById(R.id.needs_change_state);
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
					Intent returnIntent = new Intent();
					returnIntent.putExtra(NeedsDetailsActivity.refresh,true);
					setResult(RESULT_OK,returnIntent);
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