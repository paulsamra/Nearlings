package de.metagear.android.view.validation.textview;

import android.content.Context;
import de.metagear.android.R;
import de.metagear.android.view.validation.ViewValidator;

/**
 * Validates that the <code>EditText</code> contains a valid email address.
 */
// reviewed
public class EmailValidator extends RegexValidator implements
		ViewValidator {
	private final static String REGEX = "[.\\w-]+@([\\w-]+\\.)+[\\w-]+";
	
	public EmailValidator(Context context) {
		super(context, REGEX);
	}

	@Override
	public String getErrorMessage(String caption) {
		return context.getString(R.string.validation_email,
				caption);
	}
}
