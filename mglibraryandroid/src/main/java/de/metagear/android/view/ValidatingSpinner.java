package de.metagear.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import de.metagear.android.R;
import de.metagear.android.view.validation.ViewValidator;

// reviewed
public class ValidatingSpinner<T> extends OptionalSelectionSpinner<T> implements
		ValidatingView {
	protected ViewValidator validator;
	protected boolean errorIconEnabled;

	// --------------------------------------------------------------
	// constructors
	// --------------------------------------------------------------

	public ValidatingSpinner(Context context, AttributeSet attrs, int defStyle,
			T emptyItem) {
		super(context, attrs, defStyle, emptyItem);
	}

	public ValidatingSpinner(Context context, AttributeSet attrs, T emptyItem) {
		super(context, attrs, emptyItem);
	}

	public ValidatingSpinner(Context context, T emptyItem) {
		super(context, emptyItem);
	}

	// --------------------------------------------------------------
	// private / protected methods
	// --------------------------------------------------------------

	protected void drawErrorIcon(Canvas canvas) {
		final int ICON_RIGHT_MARGIN = 40;

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.indicator_input_error);
		float left = getWidth() - ICON_RIGHT_MARGIN - bitmap.getWidth();
		float top = (getHeight() - bitmap.getHeight()) / 2;
		left = (left < 0) ? 0 : left;
		top = (top < 0) ? 0 : top;

		canvas.drawBitmap(bitmap, left, top, new Paint());
	}

	// --------------------------------------------------------------
	// View overrides
	// --------------------------------------------------------------

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		if (errorIconEnabled && !isValid()) {
			drawErrorIcon(canvas);
		}
	}

	// --------------------------------------------------------------
	// ValidatingView overrides
	// --------------------------------------------------------------

	/**
	 * @param fieldDisplayNameForErrorMsg
	 *            Ignored. May be null or anything.
	 */
	@Override
	public void setValidator(ViewValidator validator,
			String fieldDisplayNameForErrorMsg) {
		this.validator = validator;
	}

	@Override
	public void flagOrUnflagValidationError() {
		errorIconEnabled = true;
		invalidate();
	}

	@Override
	public void unflagValidationError() {
		errorIconEnabled = false;
		invalidate();
	}

	@Override
	public boolean isValid() {
		if (validator == null) {
			return true;
		}
		return validator.validate(this);
	}

	// --------------------------------------------------------------
	// Spinner and superclasses overrides
	// --------------------------------------------------------------

	@Override
	public void setSelection(int position, boolean animate) {
		super.setSelection(position, animate);
		errorIconEnabled = true;
	}

	@Override
	public void setSelection(int position) {
		super.setSelection(position);
		errorIconEnabled = true;
	}
}
