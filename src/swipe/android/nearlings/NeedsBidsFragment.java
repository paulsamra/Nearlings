package swipe.android.nearlings;

import java.util.ArrayList;

import swipe.android.nearlings.jsonResponses.login.JsonBidsResponse;
import swipe.android.nearlings.jsonResponses.register.Bids;
import swipe.android.nearlings.jsonResponses.register.Users;
import swipe.android.nearlings.viewAdapters.BidsViewAdapter;
import android.widget.ArrayAdapter;

//TODO: Probably want to abstract this
public class NeedsBidsFragment extends
		RefreshableListNonSwipeFragment<JsonBidsResponse> {
	protected ArrayAdapter<Bids> mAdapter;
	// Extra stuff
	String id;

	@Override
	public void onRefresh() {
		super.onRefresh();
		swipeView.setRefreshing(true);
		task = new DummyWebTask<JsonBidsResponse>(this.getActivity(), this,
				JsonBidsResponse.class, false);

		task.execute();

	}

	static int lastInt = 1;
	static ArrayList<Bids> u = new ArrayList<Bids>();

	@Override
	public void onTaskComplete(JsonBidsResponse result) {
		// TODO Auto-generated method stub

		if (lastInt == 1) {
			Users bob = new Users(
					"id_1",
					"Bob",
					"http://icons.iconarchive.com/icons/designbolts/despicable-me-2/128/Minion-Amazed-icon.png");
			u.add(new Bids(12.0f, "June", bob));
			lastInt = 0;
		} else {
			u.clear();
			lastInt = 1;
		}
		this.mAdapter = new BidsViewAdapter(this.getActivity(),
				R.layout.bid_item, u);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);
		swipeView.setRefreshing(false);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		this.mAdapter = new BidsViewAdapter(this.getActivity(),
				R.layout.bid_item, u);

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);
		swipeView.setRefreshing(false);
	}

}