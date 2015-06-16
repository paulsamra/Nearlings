package de.metagear.android.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import de.metagear.android.R;

// reviewed
public class WidgetUtils {
	public static AlertDialog createOkAlertDialog(Context context,
			int iconResID, int titleResID, String message,
			OnClickListener okClickListener) {
		final AlertDialog alertDialog = createOkAlertDialog(context, iconResID,
				titleResID, message, R.string.androidUtil_ok, okClickListener);

		return alertDialog;
	}

	public static AlertDialog createOkAlertDialog(Context context,
			int iconResID, int titleResID, String message) {
		OnClickListener okClickListener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};

		return createOkAlertDialog(context, iconResID, titleResID, message,
				okClickListener);
	}

	public static AlertDialog createYesNoAlertDialog(Context context,
			int iconResID, int titleResID, String message,
			OnClickListener okClickListener) {
		return createOkCancelOrYesNoAlertDialog(context, iconResID, titleResID,
				message, R.string.androidUtil_yes, R.string.androidUtil_no,
				okClickListener);
	}

	public static AlertDialog createOkCancelAlertDialog(Context context,
			int iconResID, int titleResID, String message,
			OnClickListener okClickListener) {
		return createOkCancelOrYesNoAlertDialog(context, iconResID, titleResID,
				message, R.string.androidUtil_ok, R.string.androidUtil_cancel,
				okClickListener);
	}

	public static <T> boolean setSpinnerSelectedItem(Spinner spinner, T item) {
		int count = spinner.getCount();
		for (int i = 0; i < count; i++) {
			@SuppressWarnings("unchecked")
			T spinnerItem = (T) spinner.getItemAtPosition(i);
			if (spinnerItem.equals(item)) {
				spinner.setSelection(i);
				return true;
			}
		}

		return false;
	}

	public static String getEditTextText(View parentView, int id) {
		return getEditText(parentView, id).getText().toString();
	}

	public static EditText getEditText(View parentView, int id) {
		return (EditText) parentView.findViewById(id);
	}

	public static Spinner getSpinner(View parentView, int id) {
		return (Spinner) parentView.findViewById(id);
	}
	
	private static AlertDialog createOkAlertDialog(Context context,
			int iconResID, int titleResID, String message,
			int okButtonTextResID, OnClickListener okClickListener) {
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();

		alertDialog.setTitle(titleResID);
		alertDialog.setMessage(message);
		alertDialog.setIcon(iconResID);

		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				context.getString(okButtonTextResID), okClickListener);

		return alertDialog;
	}

	private static AlertDialog createOkCancelOrYesNoAlertDialog(
			Context context, int iconResID, int titleResID, String message,
			int okOrYesTextResID, int cancelOrNoResID,
			OnClickListener okClickListener) {
		final AlertDialog alertDialog = createOkAlertDialog(context, iconResID,
				titleResID, message, okOrYesTextResID, okClickListener);

		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				context.getString(cancelOrNoResID), new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.cancel();
					}
				});

		return alertDialog;
	}
}
