package swipe.android.nearlings.viewAdapters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import swipe.android.nearlings.BaseFormAdapter;
import swipe.android.nearlings.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import de.metagear.android.view.ValidatingView;

// reviewed
public class SubmitToBeDoneFormAdapter extends BaseFormAdapter {
	public SubmitToBeDoneFormAdapter(FragmentActivity ctx, View rootView,
			Bundle savedInstanceState) {
		super(ctx, rootView, savedInstanceState);
		// TODO Auto-generated constructor stub
	}

	private List<ValidatingView> validatingViews;

	public SubmitToBeDoneFormAdapter(FragmentActivity ctx, View rootView,
			Bundle savedInstanceState, String id, String title_string) {
		super(ctx, rootView, savedInstanceState);

		this.ctx = ctx;
		initializeViews(savedInstanceState, id, title_string);

	}

	Button submit;
	EditText message;
	TextView title;
	RatingBar quality, effort, timeliness;

	private void initializeViews(Bundle savedInstanceState, String id,
			String title_string) {

		message = (EditText) rootView.findViewById(R.id.descriptionBox);
		title = (TextView) rootView.findViewById(R.id.title);
		//quality = (RatingBar) rootView.findViewById(R.id.quality);
		//effort = (RatingBar) rootView.findViewById(R.id.effort);
		//timeliness = (RatingBar) rootView.findViewById(R.id.timeliness);
		title.setText(title_string);
	}

	@Override
	protected void initializeValidators(View parentView) {
		validatingViews = new ArrayList<ValidatingView>();
		Context context = parentView.getContext();

		synchronized (validatingViews) {
		}
	}

	public JSONObject getJSONObject() throws Exception {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("message", message.getText().toString());
		
		return jsonObject;
	}

}