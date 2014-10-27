package swipe.android.nearlings.viewAdapters;

import swipe.android.DatabaseHelpers.NeedsCommentsDatabaseHelper;
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

public class NeedsDetailCommentsAdapter extends CursorAdapter {

	private Context mContext;

	private Cursor cr;
	private final LayoutInflater inflater;

	private static int NO_LAYOUT = -1;
	private int layout = NO_LAYOUT;

	public NeedsDetailCommentsAdapter(Context context, Cursor c) {
		super(context, c);

		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.cr = c;
		this.layout = NO_LAYOUT;
	}

	public NeedsDetailCommentsAdapter(Context context, Cursor c, int layout) {
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
			view = inflater.inflate(R.layout.needs_comments_item_layout,
					parent, false);
		} else {
			view = inflater.inflate(layout, parent, false);
		}

	
		holder.comment = (TextView) view.findViewById(R.id.needs_comments_message);
		holder.sender = (TextView) view.findViewById(R.id.needs_comments_name);
		holder.personImage = (ImageView) view.findViewById(R.id.needs_comments_image);
		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		final ViewHolder holder = (ViewHolder) view.getTag();

		int sender_index = cursor
				.getColumnIndexOrThrow(NeedsCommentsDatabaseHelper.COLUMN_AUTHOR);
		int message_index = cursor
				.getColumnIndexOrThrow(NeedsCommentsDatabaseHelper.COLUMN_MESSAGE);
		int author_picture_url = cursor
				.getColumnIndexOrThrow(NeedsCommentsDatabaseHelper.COLUMN_AUTHOR_IMAGE_PREVIEW_URL);

		holder.sender.setText(cursor.getString(sender_index));

		// problem is processing. this should only happen once.

		holder.comment.setText(cursor.getString(message_index));

		ImageLoader.getInstance().displayImage(
				cursor.getString(author_picture_url), holder.personImage,
				NearlingsApplication.getDefaultOptions());

	}

	public static class ViewHolder {
		public TextView  sender, comment;
		public ImageView personImage;

	}

}