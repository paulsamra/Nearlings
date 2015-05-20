package swipe.android.nearlings;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import swipe.android.nearlings.googleplaces.GoogleParser;
import swipe.android.nearlings.googleplaces.GoogleParser.PlacesTask;
import swipe.android.nearlings.jsonResponses.events.create.JsonEventSubmitResponse;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.example.deletableedittext.DeleteableEditText;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.google.gson.JsonObject;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.AdapterView.OnItemClickListener;
import de.metagear.android.util.WidgetUtils;
import de.metagear.android.view.ValidatingEditText;
import de.metagear.android.view.ValidatingView;
import de.metagear.android.view.validation.ViewValidator;
import de.metagear.android.view.validation.textview.EmailValidator;
import de.metagear.android.view.validation.textview.MinLengthValidator;
import de.metagear.android.view.validation.textview.NumberValidator;
import de.metagear.android.view.validation.textview.PriceValidator;
import de.metagear.android.view.validation.textview.ZipCodeValidator;

// reviewed
public class GroupFormViewAdapter extends BaseFormAdapter {
	private List<ValidatingView> validatingViews;

	public GroupFormViewAdapter(FragmentActivity ctx, View rootView,
			Bundle savedInstanceState) {
		super(ctx, rootView, savedInstanceState);

		this.ctx = ctx;
		initializeViews(savedInstanceState);

	}

	Button category;
	EditText title, descriptionBox;
	//Switch switch_online_inperson, private_public_switch;
	Switch switch_online_inperson;
	EditText price;

	private void initializeViews(Bundle savedInstanceState) {
		category = (Button) rootView.findViewById(R.id.category_button);
		setUpCategory(category, R.array.group_types);

		title = (EditText) rootView.findViewById(R.id.title);
		descriptionBox = (EditText) rootView.findViewById(R.id.descriptionBox);

		switch_online_inperson = (Switch) rootView
				.findViewById(R.id.switch_online_inperson);
		/*private_public_switch = (Switch) rootView
				.findViewById(R.id.private_public_switch);*/
		price = (EditText) rootView.findViewById(R.id.price);
		setUpPriceListener(price);
	}

	@Override
	protected void initializeValidators(View parentView) {
		validatingViews = new ArrayList<ValidatingView>();
		Context context = parentView.getContext();

		synchronized (validatingViews) {

			assignValidatorToValidatingView(parentView, R.id.title,
					R.string.title, new MinLengthValidator(context, 1));

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
		// nickname
		jsonObject.put("nickname", descriptionBox.getText());

		//
		jsonObject.put("category", this.category.getText().toString()
				.toLowerCase());
		jsonObject.put("dues",
				FieldsParsingUtils.parsePrice(price.getText().toString()));
		jsonObject.put("mode", FieldsParsingUtils
				.parseSwitchOnlineOffline(this.switch_online_inperson
						.isChecked()));
		/*jsonObject.put("visibility", FieldsParsingUtils
				.parseSwitchPrivatePublic(this.private_public_switch
						.isChecked()));*/
		jsonObject.put("visibility", "public");
		String location = edt_input_place.getText().toString();
		if (location == null || location.equals("")) {
			Location l = NearlingsApplication.getInstance()
					.getCurrentLocation();
			jsonObject.put("latitude", l.getLatitude());
			jsonObject.put("longitude", l.getLongitude());
		} else {
			jsonObject.put("location", location);
		}

		return jsonObject;
	}

}