package de.metagear.android.view;

import de.metagear.android.view.validation.ViewValidator;

/**
 * A <code>View</code> that validates itself by using the given
 * <code>ViewValidator</code>.<br>
 * If not valid, the view shall display a visual sign, like an error icon.
 */
// reviewed
public interface ValidatingView {
	/**
	 * Registers the given validator.
	 * 
	 * @param validator
	 * @param fieldDisplayNameForErrorMsg
	 *            Localized display field name, i.e., "City" or "Postal Code".
	 */
	void setValidator(ViewValidator validator,
			String fieldDisplayNameForErrorMsg);

	/**
	 * If the view's value ain't valid, shows an appropriate visual sign, else
	 * removes that sign.
	 */
	void flagOrUnflagValidationError();

	/**
	 * Removes any visual signs corresponding to errors from the view.
	 */
	void unflagValidationError();

	/**
	 * Returns whether the view's value is valid.
	 */
	boolean isValid();
}
