package swipe.android.nearlings.viewAdapters;

import java.util.ArrayList;
import java.util.List;

import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.R;
import swipe.android.nearlings.json.needs.comments.Comments;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class NeedsCommentsAdapter extends ArrayAdapter<Comments> {

	private static class ViewHolder {
		public TextView sender, comment, date;
		public ImageView personImage;

	}

	private static int NO_LAYOUT = -1;
	private int layout = NO_LAYOUT;

	public NeedsCommentsAdapter(Context context, List<Comments> users) {
		super(context, R.layout.needs_comments_item_layout, users);
		layout = R.layout.needs_comments_item_layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Comments comment = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder holder; // view lookup cache stored in tag
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(layout, parent, false);

			holder.comment = (TextView) convertView
					.findViewById(R.id.needs_comments_message);
			holder.sender = (TextView) convertView
					.findViewById(R.id.needs_comments_name);
			holder.personImage = (ImageView) convertView
					.findViewById(R.id.needs_comments_image);
holder.date =(TextView) convertView
.findViewById(R.id.needs_comments_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/*holder.sender.setText(comment.getSender());

		// problem is processing. this should only happen once.
*/
		holder.comment.setText(comment.getMessage());
		holder.sender.setText(comment.getSender());
	holder.date.setText(comment.getTime().getDate());
		//holder.comment.setText("comment");
		ImageLoader.getInstance().displayImage(comment.getSender_thumbnail(),
				holder.personImage, NearlingsApplication.getDefaultOptions());

		return convertView;
	}
}