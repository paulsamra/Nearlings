package de.metagear.android.view.validation.textview;

import android.content.Context;
import de.metagear.android.R;
import de.metagear.android.view.validation.ViewValidator;

/**
 * Validates that the <code>EditText</code> contains a valid ZIP code, that
 * consists of exactly 5 digits.
 */
//reviewed
public class ZipCodeValidator extends RegexValidator implements
		ViewValidator {
	private final static String REGEX = "[0-9]{5}";

	public ZipCodeValidator(Context context) {
		super(context, REGEX);
	}

	@Override
	public String getErrorMessage(String caption) {
		return context.getString(R.string.validation_zipCode, caption);
	}
}
