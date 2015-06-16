package de.metagear.android.view.validation.textview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import de.metagear.android.R;
import de.metagear.android.view.validation.ViewValidator;

/**
 * Validates that the <code>EditText</code> contains a string of the given
 * minimum length.
 */
//reviewed
public class MinLengthValidator implements ViewValidator {
	protected Context context;
	protected int minLength;

	public MinLengthValidator(Context context, int minLength) {
		super();
		
		this.context = context;
		this.minLength = minLength;
	}

	@Override
	public boolean validate(View view) {
		return ((TextView) view).getText().length() >= this.minLength;
	}

	@Override
	public String getErrorMessage(String caption) {
		return context.getString(R.string.validation_minLength, caption,
				this.minLength);
	}

}
