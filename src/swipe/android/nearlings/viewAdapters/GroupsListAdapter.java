package swipe.android.nearlings.viewAdapters;

import swipe.android.DatabaseHelpers.GroupsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class GroupsListAdapter extends CursorAdapter {

	private Context mContext;

	private Cursor cr;
	private final LayoutInflater inflater;

	private static int NO_LAYOUT = -1;
	private int layout = NO_LAYOUT;

	public GroupsListAdapter(Context context, Cursor c) {
		super(context, c);

		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.cr = c;
		this.layout = NO_LAYOUT;
	}

	public GroupsListAdapter(Context context, Cursor c, int layout) {
		super(context, c);

		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.cr = c;
		this.layout = layout;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		ViewHolder holder = new ViewHolder();
		View view;
		if (this.layout == NO_LAYOUT) {
			view = inflater.inflate(R.layout.groups_list_item_layout,
					parent, false);
		} else {
			view = inflater.inflate(layout, parent, false);
		}
	holder.title = (TextView) view.findViewById(R.id.groups_title);
		holder.description = (TextView) view.findViewById(R.id.groups_description);
		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();

		int name_index = cursor
				.getColumnIndexOrThrow(GroupsDatabaseHelper.COLUMN_GROUP_NAME);
		int description_index = cursor
				.getColumnIndexOrThrow(GroupsDatabaseHelper.COLUMN_DESCRIPTION);
		holder.title.setText(cursor.getString(name_index));
		holder.description.setText(cursor.getString(description_index));
	}

	public static class ViewHolder {
		public TextView title, description;

	}
	
}