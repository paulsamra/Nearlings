package de.metagear.android.view.validation;

import android.view.View;

// reviewed
public interface ViewValidator {
	boolean validate(View view);

	/**
	 * Returns a localized error message (even when validation has been
	 * successful).
	 * 
	 * @param caption
	 *            the display name of the <code>TextView</code> (i.e.,
	 *            "ZIP Code")
	 * @return localized error message; never null
	 */
	String getErrorMessage(String caption);
}
