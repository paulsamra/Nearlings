package swipe.android.nearlings;

import java.util.Map;

import org.json.JSONObject;

import de.metagear.android.view.ValidatingEditText;
import swipe.android.nearlings.json.jsonoffersresponse.JsonMakeOffersResponse;
import swipe.android.nearlings.viewAdapters.MakeOfferFormAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.edbert.library.greyButton.GreyedOutButton;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;

public class MakeOfferActivity extends ActionBarActivity implements
		AsyncTaskCompleteListener<JsonMakeOffersResponse> {

	MakeOfferFormAdapter makeFormAdapter;
	TextView finishLabel;
	GreyedOutButton makeOffer;

	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setTitle("Make Offer");

		setContentView(R.layout.make_offer_layout);
		TextView description_label = (TextView) findViewById(R.id.description_label);
		description_label.setText("Comment");
		ValidatingEditText description= (ValidatingEditText) findViewById(R.id.descriptionBox);
		description.setHint("Comment");
		finishLabel = (TextView) findViewById(R.id.when_label);
		finishLabel.setText("Time Finish");
		makeOffer = (GreyedOutButton) findViewById(R.id.needs_change_state);
		makeOffer.setText("Make Offer");
		makeOffer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(makeFormAdapter.areAllViewsValid())
				submitOffer();
			}
			
		});
		id = getIntent().getExtras().getString("id");

		String title = getIntent().getExtras().getString("title");
		makeFormAdapter = new MakeOfferFormAdapter(this, getWindow()
				.getDecorView().findViewById(android.R.id.content),
				savedInstanceState, id, title);
	}

	String id = "";

	public void submitOffer() {
		// should check first
		Map<String, String> headers = SessionManager.getInstance(this)
				.defaultSessionHeaders();

		try {
			JSONObject jsonObject = this.makeFormAdapter.getJSONObject();
			String body = makeFormAdapter.getJSONObject().toString();
			new PostDataWebTask<JsonMakeOffersResponse>(this,
					JsonMakeOffersResponse.class).execute(SessionManager
					.getInstance(this).makeOfferURL(id), MapUtils
					.mapToString(headers), body);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// construct the body

	@Override
	public void onTaskComplete(JsonMakeOffersResponse result) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (result.isValid()) {

			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					dialog.cancel();
					MakeOfferActivity.this.finish();
				}
			});
			builder.setTitle("Success!");
			builder.setMessage("You have sucessfully sent an offer.");
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		onBackPressed();
		return true;
	}

}