package swipe.android.nearlings;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import swipe.android.nearlings.json.jsonoffersresponse.JsonMakeOffersResponse;
import swipe.android.nearlings.viewAdapters.MakeOfferFormAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.edbert.library.dialog.DialogManager;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;

public class MakeOfferActivity extends FragmentActivity implements
		AsyncTaskCompleteListener<JsonMakeOffersResponse> {

	MakeOfferFormAdapter makeFormAdapter;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("Make Offer");

		setContentView(R.layout.make_offer_layout);
		makeFormAdapter = new MakeOfferFormAdapter(this, getWindow()
				.getDecorView().findViewById(android.R.id.content),
				savedInstanceState);
		 id = savedInstanceState.getString("id");
		

	}
	String id ="";
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
		if(!result.isValid()){
			return;
		}
		this.finish();

	}

}