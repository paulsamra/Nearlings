package swipe.android.nearlings.viewAdapters;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.metagear.android.view.validation.textview.PriceValidator;
import swipe.android.nearlings.BaseFormAdapter;
import swipe.android.nearlings.FieldsParsingUtils;
import swipe.android.nearlings.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import de.metagear.android.view.ValidatingView;

// reviewed
public class MakeOfferFormAdapter extends BaseFormAdapter {
	private List<ValidatingView> validatingViews;

	public MakeOfferFormAdapter(FragmentActivity ctx, View rootView,
			Bundle savedInstanceState, String id, String title_string) {
		super(ctx, rootView, savedInstanceState);

		this.ctx = ctx;
		initializeViews(savedInstanceState, id, title_string);

	}

	Button submit;
	EditText message, price;
	TextView title;
	private void initializeViews(Bundle savedInstanceState, String id, String title_string) {
		
		price = (EditText) rootView.findViewById(R.id.price);
		//setUpPriceListener(price);

		message = (EditText) rootView.findViewById(R.id.descriptionBox);
		title = (TextView)rootView.findViewById(R.id.title);
	
	
		title.setText(title_string);
	}


	@Override
	protected void initializeValidators(View parentView) {
		validatingViews = new ArrayList<ValidatingView>();
		Context context = parentView.getContext();

		synchronized (validatingViews) {
			assignValidatorToValidatingView(parentView, R.id.price,
					R.string.price, new PriceValidator(context ));
/*
			assignValidatorToValidatingView(parentView, R.id.title,
					R.string.title, new MinLengthValidator(context, 1));
			assignValidatorToValidatingView(parentView, R.id.age_value,
					R.string.age, new NumberValidator(context));

			assignValidatorToValidatingView(parentView, R.id.descriptionBox,
					R.string.description, new MinLengthValidator(context, 0));
			assignValidatorToValidatingView(parentView, R.id.location,
					R.string.location, new MinLengthValidator(context, 1));*/
		}
	}

	public JSONObject getJSONObject() throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", message.getText().toString());
	//	jsonObject.put("user_id", SessionManager.getInstance(this.ctx).getUserID());
		jsonObject.put("amount", FieldsParsingUtils.parsePrice(price.getText().toString()));
		return jsonObject;
	}

}