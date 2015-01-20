package swipe.android.nearlings;

import android.content.Context;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.AutoCompleteTextView;
import de.metagear.android.view.ValidatingEditText;
import de.metagear.android.view.ValidatingView;
import de.metagear.android.view.validation.ViewValidator;
import de.metagear.android.view.validation.textview.EmailValidator;
import de.metagear.android.view.validation.textview.PhoneNumberValidator;
import de.metagear.android.view.validation.textview.ZipCodeValidator;


public class ValidatingAutoCompleteView extends AutoCompleteTextView  implements ValidatingView {
	private ViewValidator validator;
	private String fieldDisplayNameForErrorMsg;
	public ValidatingAutoCompleteView(Context context) {
		super(context);
	}

	public ValidatingAutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ValidatingAutoCompleteView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setValidator(ViewValidator validator,
			String fieldDisplayNameForErrorMsg) {
		this.validator = validator;
		this.fieldDisplayNameForErrorMsg = fieldDisplayNameForErrorMsg;

		initInputType(validator);
		registerListeners();
	}
	protected void registerListeners() {
		registerOnFocusChangeListener();
		registerOnKeyListener();
		registerOnLongClickListener();
	}

	protected void initInputType(ViewValidator validator) {
		if (validator instanceof EmailValidator) {
			setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		} else if (validator instanceof PhoneNumberValidator) {
			setInputType(InputType.TYPE_CLASS_PHONE);
		} else if (validator instanceof ZipCodeValidator) {
			setInputType(InputType.TYPE_CLASS_NUMBER);
		} else if (!isPassword()) {
			setInputType(InputType.TYPE_CLASS_TEXT);
		} else if (isPassword()) {
			setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		setInputType(getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
	}
	protected boolean isPassword() {
		TransformationMethod method = getTransformationMethod();
		return method != null && method instanceof PasswordTransformationMethod;
	}
	protected void registerOnLongClickListener() {
		setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				ValidatingAutoCompleteView.this.flagOrUnflagValidationError();
				return true;
			}
		});
	}

	protected void registerOnKeyListener() {
		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				final int KEYCODE_ENTER = 66;

				boolean consumed = false;

				if (keyCode == KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					ValidatingAutoCompleteView.this.flagOrUnflagValidationError();
					consumed = true;
				}

				return consumed;
			}
		});
	}

	protected void registerOnFocusChangeListener() {
		setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (!hasFocus) {
					ValidatingAutoCompleteView.this.flagOrUnflagValidationError();
				}
			}
		});
	}
	@Override
	public void flagOrUnflagValidationError() {
		String msg = this.isValid() ? null : this.validator
				.getErrorMessage(this.fieldDisplayNameForErrorMsg);
		this.setError(msg);
	}

	@Override
	public boolean isValid() {
		return validator.validate(this);
	}

	@Override
	public void unflagValidationError() {
		setError(null);
	}

}