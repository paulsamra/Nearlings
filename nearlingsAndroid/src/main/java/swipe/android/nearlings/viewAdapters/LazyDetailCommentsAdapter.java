package swipe.android.nearlings.viewAdapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import swipe.android.nearlings.R;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.json.needs.comments.Comments;
import swipe.android.nearlings.json.needs.comments.JsonCommentsResponse;
import android.content.Context;
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
		Date now = new Date();
		long currentTime = new Long(now.getTime());
		if (currentTime > nextTime)
			cacheInBackground();
	}

	public void requestUrgentUpdate() {

		cacheInBackground();
	}

	long nextTime = 0;

	public static long getWaitTimeExp(int retryCount) {

		long waitTime = ((long) Math.pow(2, retryCount) * 100L);

		return waitTime;
	}

	@Override
	protected boolean cacheInBackground() {
		// SystemClock.sleep(10000); // pretend to do work
		String url = SessionManager.getInstance(mContext).commentsURL(idOfNeed);
		Map<String, String> headers = SessionManager.getInstance(mContext)
				.defaultSessionHeaders();
		if (!alreadyCalled) {
			new GetDataWebTask<JsonCommentsResponse>( mContext, this,
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
	int retryCount = 0;

	@Override
	public void onTaskComplete(JsonCommentsResponse result) {
		if (result == null) {
			retryCount++;
			return;
		}
		if (!result.isValid() || result.getComments().size() == items.size()) {
			retryCount++;
			nextTime = getWaitTimeExp(retryCount);
			return;
		}
		if (result.isValid()) {
			retryCount = 0;
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