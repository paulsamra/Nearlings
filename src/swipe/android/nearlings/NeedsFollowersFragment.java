package swipe.android.nearlings;

import java.util.ArrayList;

import swipe.android.nearlings.jsonResponses.login.JsonFollowersResponse;
import swipe.android.nearlings.jsonResponses.register.Users;
import swipe.android.nearlings.viewAdapters.FollowersViewAdapter;
import android.widget.ArrayAdapter;

//TODO: Probably want to abstract this
public class NeedsFollowersFragment extends
		RefreshableListNonSwipeFragment<JsonFollowersResponse> {

	protected ArrayAdapter<Users> mAdapter;
	// Extra stuff
	String id;

	@Override
	public void onRefresh() {
		// getLoaderManager().initLoader(0, null, this);
		// /request the sync
		super.onRefresh();
		swipeView.setRefreshing(true);
		task = new DummyWebTask<JsonFollowersResponse>(this.getActivity(),
				this, JsonFollowersResponse.class, false);

		task.execute();

	}

	static int lastInt = 1;
	static ArrayList<Users> u = new ArrayList<Users>();

	@Override
	public void onTaskComplete(JsonFollowersResponse result) {
		
		if (lastInt == 1) {

			u.add(new Users(
					"id_1",
					"Bob",
					"http://icons.iconarchive.com/icons/designbolts/despicable-me-2/128/Minion-Amazed-icon.png"));
			lastInt = 0;
		} else {
			u.clear();
			lastInt = 1;
		}
		this.mAdapter = new FollowersViewAdapter(this.getActivity(),
				R.layout.user_item, u);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);
		swipeView.setRefreshing(false);
	
	}
	@Override
	public void onResume(){
		super.onResume();
		this.mAdapter = new FollowersViewAdapter(this.getActivity(),
				R.layout.user_item, u);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);
		swipeView.setRefreshing(false);
	}

}