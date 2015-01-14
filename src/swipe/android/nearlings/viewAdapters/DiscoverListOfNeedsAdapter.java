package swipe.android.nearlings.viewAdapters;

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
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DiscoverListOfNeedsAdapter extends CursorAdapter {

	private Context mContext;

	private Cursor cr;
	private final LayoutInflater inflater;

	private static int NO_LAYOUT = -1;
	private int layout = NO_LAYOUT;

	public DiscoverListOfNeedsAdapter(Context context, Cursor c) {
		super(context, c);

		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.cr = c;
		this.layout = NO_LAYOUT;
	}

	public DiscoverListOfNeedsAdapter(Context context, Cursor c, int layout) {
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
			view = inflater.inflate(R.layout.list_of_needs_list_item_layout,
					parent, false);
		} else {
			view = inflater.inflate(layout, parent, false);
		}
		holder.dateDue = (TextView) view.findViewById(R.id.needs_date_sent);
		holder.sender = (TextView) view.findViewById(R.id.needs_person);
		holder.task = (TextView) view.findViewById(R.id.needs_task);
		// holder.location = (TextView) view.findViewById(R.id.needs_location);
		holder.price = (TextView) view.findViewById(R.id.needs_price);
		// holder.status = (TextView) view.findViewById(R.id.needs_status);

		holder.personImage = (ImageView) view.findViewById(R.id.icon_image);
		// holder.progressBar = (ProgressBar)
		// view.findViewById(R.id.icon_progress);
		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();

		int sender_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR);
		int time_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_DATE);
		int task_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_TITLE);
		int location_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_LOCATION_NAME);
		int status_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_STATUS);
		int price_index = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_PRICE);
		int author_picture_url = cursor
				.getColumnIndexOrThrow(NeedsDetailsDatabaseHelper.COLUMN_AUTHOR_IMAGE_PREVIEW_URL);

		holder.sender.setText(cursor.getString(sender_index));

		// problem is processing. this should only happen once.

		holder.dateDue.setText(cursor.getString(time_index));

		holder.task.setText(cursor.getString(task_index));

		// holder.location.setText(cursor.getString(location_index));
		holder.price.setText("$" + cursor.getString(price_index));
		// holder.status.setText(cursor.getString(status_index));

		ImageLoader.getInstance().displayImage(
				cursor.getString(author_picture_url), holder.personImage,
				NearlingsApplication.getDefaultOptions(),
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// holder.progressBar.setProgress(0);
						// holder.progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// holder.progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// holder.progressBar.setVisibility(View.GONE);
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {
						/*
						 * holder.progressBar.setProgress(Math.round(100.0f
						 * current / total));
						 */
					}
				});

	}

	public static class ViewHolder {
		public TextView dateDue, sender, task, price;
		// public TextView sender, task, price ;
		// ProgressBar progressBar;
		public ImageView personImage;

	}

}