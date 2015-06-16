package de.metagear.android.view.validation.textview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import de.metagear.android.R;
import de.metagear.android.view.validation.ViewValidator;

//reviewed
public class MatchingTextViewValidator implements ViewValidator {
	protected Context context;
	protected TextView otherView;
	protected String otherViewCaption;

	public MatchingTextViewValidator(Context context, TextView otherView,
			String otherViewCaption) {
		super();
		
		this.context = context;
		this.otherView = otherView;
		this.otherViewCaption = otherViewCaption;
	}

	@Override
	public boolean validate(View view) {
		boolean lengthOK = (((TextView) view).getText().length() > 0);
		boolean textEquals = ((TextView) view).getText().toString()
				.equals(otherView.getText().toString());
		return (lengthOK && textEquals);
	}

	@Override
	public String getErrorMessage(String caption) {
		return context.getResources().getString(R.string.validation_match,
				caption, this.otherViewCaption);
	}
}
