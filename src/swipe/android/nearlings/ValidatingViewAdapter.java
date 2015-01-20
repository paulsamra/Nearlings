package swipe.android.nearlings;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import de.metagear.android.util.WidgetUtils;
import de.metagear.android.view.ValidatingEditText;
import de.metagear.android.view.ValidatingView;
import de.metagear.android.view.validation.ViewValidator;
import de.metagear.android.view.validation.textview.EmailValidator;
import de.metagear.android.view.validation.textview.MinLengthValidator;


public abstract class ValidatingViewAdapter {
	private List<ValidatingView> validatingViews;
protected View rootView;
	public ValidatingViewAdapter(View rootView) {
		
			validatingViews = new ArrayList<ValidatingView>();
		
		this.rootView = rootView;
		initializeValidators(rootView);
	}
	
	public String fromView(int id){
		return WidgetUtils.getTextFromView(rootView, id);
	}

	public void validateAllViews() {
		synchronized (validatingViews) {
			for (ValidatingView view : validatingViews) {
				view.flagOrUnflagValidationError();
			}
		}
	}
	public void addTextWatcherToAll(TextWatcher watcher){
		synchronized (validatingViews) {
			for (ValidatingView view : validatingViews) {
				EditText v = (EditText) view;
				v.addTextChangedListener(watcher);
			}
		}
	}
	public void validateAddressViews() {
		// TODO this is a bit much too hard-coded: the form design could change.
		// Instead we should use a Map<Integer, View> with the view IDs.
		synchronized (validatingViews) {
			for (ValidatingView view : validatingViews) {
				view.flagOrUnflagValidationError();
			}
		}
	}

	public boolean areAllViewsValid() {
		synchronized (validatingViews) {
			for (ValidatingView view : validatingViews) {
				if (!view.isValid()) {
					return false;
				}
			}
		}

		return true;
	}
	public void updateAllCompareToViews() {

		synchronized (validatingViews) {
			for (ValidatingView view : validatingViews) {
				//if(view.getValidator() instanceof compareToValidator){
				view.flagOrUnflagValidationError();
//
			//	}
			}
		}
	}
	public void resetAllViewValues() {
		synchronized (validatingViews) {
			for (ValidatingView view : validatingViews) {
				if (view instanceof EditText) {
					((EditText) view).setText("");
				} else if (view instanceof Spinner) {
					((Spinner) view).setSelection(0);
				}

				view.unflagValidationError();
			}
		}
	}

	protected abstract void initializeValidators(View parentView);
		

	protected void assignValidatorToValidatingView(View parentView,
			int validatingViewResID, int fieldDisplayNameResID,
			ViewValidator validator) {

		ValidatingView view = (ValidatingView) parentView
				.findViewById(validatingViewResID);
		String caption = parentView.getContext().getResources()
				.getString(fieldDisplayNameResID);

		view.setValidator(validator, caption);

		validatingViews.add(view);
	}
}