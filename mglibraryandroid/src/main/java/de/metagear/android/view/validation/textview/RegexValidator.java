package de.metagear.android.view.validation.textview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import de.metagear.android.R;
import de.metagear.android.view.validation.ViewValidator;

/**
 * Validates that the <code>EditText</code> contains a string that matches the
 * given regular expression.
 */
//reviewed
public class RegexValidator implements ViewValidator {
	protected Context context;
	protected Pattern pattern;

	public RegexValidator(Context context, String regex) {
		super();
		
		this.context = context;
		this.pattern = Pattern.compile(regex);
	}

	@Override
	public boolean validate(View view) {
		Matcher matcher = pattern.matcher(((TextView) view).getText());
		return matcher.matches();
	}

	@Override
	public String getErrorMessage(String caption) {
		return context.getString(R.string.validation_regex, caption,
				this.pattern.toString());
	}
}
