package swipe.android.nearlings.viewAdapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import swipe.android.nearlings.R;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.json.needs.comments.Comments;
import swipe.android.nearlings.json.needs.comments.JsonCommentsResponse;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.GetDataWebTask;
import com.edbert.library.utils.MapUtils;

public class LazyDetailCommentsAdapter extends EndlessAdapter implements
		AsyncTaskCompleteListener<JsonCommentsResponse> {
	private RotateAnimation rotate = null;
	private View pendingView = null;
	String idOfNeed;
	ArrayList<Comments> items;
	int limit = -1;

	public LazyDetailCommentsAdapter(Context ctxt, ArrayList<Comments> list,
			String idOfNeed, int limit) {
		super(new NeedsCommentsAdapter(ctxt, list));
		items = list;
		rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(600);
		rotate.setRepeatMode(Animation.RESTART);
		rotate.setRepeatCount(Animation.INFINITE);
		setRunInBackground(false);
		this.idOfNeed = idOfNeed;
		this.limit = limit;
		// add sublist feature
	}

	@Override
	protected View getPendingView(ViewGroup parent) {
		View row = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.needs_comments_item_layout, null);

		pendingView = row.findViewById(R.id.main_message_body);
		pendingView.setVisibility(View.GONE);
		pendingView = row.findViewById(R.id.throbber);
		pendingView.setVisibility(View.GONE);

		startProgressAnimation();

		return (row);
	}

	JsonCommentsResponse jcp;

	public void requestUpdate() {
		cacheInBackground();
	}

	@Override
	protected boolean cacheInBackground() {
		// SystemClock.sleep(10000); // pretend to do work
		String url = SessionManager.getInstance(mContext).commentsURL(idOfNeed);
		Map<String, String> headers = SessionManager.getInstance(mContext)
				.defaultSessionHeaders();
		if (!alreadyCalled){
			Log.d("URL", url);
			new GetDataWebTask<JsonCommentsResponse>(this, mContext,
					JsonCommentsResponse.class, false).execute(url,
					MapUtils.mapToString(headers));
		}
		return true;
	}
	
	
	@Override
	protected void appendCachedData() {

		/*
		 * @SuppressWarnings("unchecked") ArrayAdapter<Comments> a =
		 * (ArrayAdapter<Comments>) getWrappedAdapter();
		 * 
		 * for (Comments com : items) { a.add(com); }
		 */
	}

	void startProgressAnimation() {
		if (pendingView != null) {
			pendingView.startAnimation(rotate);
		}
	}

	private Context mContext;
	int lastSize = -1;
	boolean alreadyCalled = false;

	@Override
	public void onTaskComplete(JsonCommentsResponse result) {
		if (result == null) {
			return;
		}
		if (result.isValid()) {

			// add elements to al, including duplicates
			if (lastSize == result.getComments().size()) {
				alreadyCalled = true;
				return;
			}
			HashSet<Comments> hs = new HashSet<Comments>();
			hs.addAll(items);
			hs.addAll(result.getComments());
			items.clear();
			items.addAll(hs);

			Collections.sort(items);

			// items = result.getComments();
			this.onDataReady();
			// this.appendCachedData();
			/*
			 * @SuppressWarnings("unchecked") ArrayAdapter<Comments> a =
			 * (ArrayAdapter<Comments>) getWrappedAdapter();
			 * this.notifyDataSetChanged(); a.notifyDataSetChanged();
			 */

		}
		// now we have to add this shit
	}

}