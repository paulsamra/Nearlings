package de.metagear.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

// reviewed
public class OptionalSelectionSpinner<T> extends Spinner {
	private Context context;
	private T emptyItem;

	public OptionalSelectionSpinner(Context context, AttributeSet attrs,
			int defStyle, T emptyItem) {
		super(context, attrs, defStyle);

		this.context = context;
		this.emptyItem = emptyItem;
	}

	public OptionalSelectionSpinner(Context context, AttributeSet attrs,
			T emptyItem) {
		super(context, attrs);

		this.context = context;
		this.emptyItem = emptyItem;
	}

	public OptionalSelectionSpinner(Context context, T emptyItem) {
		super(context);

		this.context = context;
		this.emptyItem = emptyItem;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getSelectedItem() {
		return (T) super.getSelectedItem();
	}

	@Override
	public void setAdapter(SpinnerAdapter adapter) {
		if (adapter instanceof OptionalSelectionSpinnerAdapter) {
			super.setAdapter(adapter);
		} else {
			super.setAdapter(new OptionalSelectionSpinnerAdapter<T>(context,
					adapter, emptyItem));
		}
	}
}
