package swipe.android.nearlings;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import de.metagear.android.view.ValidatingEditText;
import de.metagear.android.view.ValidatingView;
import de.metagear.android.view.validation.textview.MinLengthValidator;

// reviewed
public class NeedFormViewAdapter extends BaseFormAdapter {
	private List<ValidatingView> validatingViews;

	public NeedFormViewAdapter(FragmentActivity ctx, View rootView,
			Bundle savedInstanceState) {
		super(ctx, rootView, savedInstanceState);

		this.ctx = ctx;
		initializeViews(savedInstanceState);

	}

	Button category;
	EditText title, descriptionBox;
	EditText price;
	Switch switch_online_inperson, private_public_switch;

	private void initializeViews(Bundle savedInstanceState) {
		price = (EditText) rootView.findViewById(R.id.price);
		setUpPriceListener(price);

		category = (Button) rootView.findViewById(R.id.category_button);
		setUpCategory(category, R.array.needs_types);

		title = (EditText) rootView.findViewById(R.id.title);
		descriptionBox = (EditText) rootView.findViewById(R.id.descriptionBox);

		switch_online_inperson = (Switch) rootView
				.findViewById(R.id.switch_online_inperson);
		private_public_switch = (Switch) rootView
				.findViewById(R.id.private_public_switch);
		ValidatingEditText title = (ValidatingEditText) rootView
				.findViewById(R.id.title);
		title.setHint("Need Name");
	}

	@Override
	protected void initializeValidators(View parentView) {
		validatingViews = new ArrayList<ValidatingView>();
		Context context = parentView.getContext();

		synchronized (validatingViews) {

			assignValidatorToValidatingView(parentView, R.id.title,
					R.string.title, new MinLengthValidator(context, 1));
			/*
			 * (assignValidatorToValidatingView(parentView, R.id.age_value,
			 * R.string.age, new NumberValidator(context));
			 */
			assignValidatorToValidatingView(parentView, R.id.descriptionBox,
					R.string.description, new MinLengthValidator(context, 0));
			assignValidatorToValidatingView(parentView, R.id.location,
					R.string.location, new MinLengthValidator(context, 1));
		}
	}

	public JSONObject getJSONObject() throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("title", title.getText());
		jsonObject.put("description", descriptionBox.getText());
		jsonObject.put("time_due", FieldsParsingUtils.getTime(this.ctx,start_date
				.getText().toString(), start_time.getText().toString()));
		jsonObject.put("reward",
				FieldsParsingUtils.parsePrice(price.getText().toString()));

		jsonObject.put("mode", FieldsParsingUtils
				.parseSwitchOnlineOffline(this.switch_online_inperson
						.isChecked()));
		/*
		 * jsonObject.put("visibility", FieldsParsingUtils
		 * .parseSwitchPrivatePublic(this.private_public_switch .isChecked()));
		 */

		String location = edt_input_place.getText().toString();
		if (location == null || location.equals("")) {
			Location l = NearlingsApplication.getInstance()
					.getCurrentLocation();
			jsonObject.put("latitude", l.getLatitude());
			jsonObject.put("longitude", l.getLongitude());
		} else {
			jsonObject.put("location", location);
		}

		jsonObject.put("category", this.category.getText().toString()
				.toLowerCase());
		return jsonObject;
	}

}