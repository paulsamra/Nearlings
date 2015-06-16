package de.metagear.android.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

// reviewed
public class OptionalSelectionSpinnerAdapter<T> extends BaseAdapter implements
		SpinnerAdapter {
	protected Context context;
	protected SpinnerAdapter adapter;
	private T emptyItem;

	public OptionalSelectionSpinnerAdapter(Context context,
			SpinnerAdapter adapter, T emptyItem) {
		super();
		
		this.context = context;
		this.adapter = adapter;
		this.emptyItem = emptyItem;
	}

	@Override
	public int getCount() {
		return adapter.getCount() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position == 0) {
			return emptyItem;
		} else {
			return adapter.getItem(position - 1);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		View newView;
		if (position == 0) {
			newView = new TextView(context);
		} else {
			newView = adapter.getView(position - 1, null, parent);
		}
		return newView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View newView;
		if (position == 0) {
			newView = new TextView(parent.getContext());
		} else {
			newView = adapter.getDropDownView(position - 1, null, parent);
		}
		return newView;
	}
}
