package swipe.android.nearlings;

import swipe.android.nearlings.jsonResponses.login.JsonFollowersResponse;
import swipe.android.nearlings.jsonResponses.register.Users;
import swipe.android.nearlings.viewAdapters.FollowersViewAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.edbert.library.network.SocketOperator;

//TODO: Probably want to abstract this
public class NeedsFollowersFragment extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener {
	ListView lView;
	protected ArrayAdapter<Users> mAdapter;
	SwipeRefreshLayout swipeView;

	//Extra stuff
	String id;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.pull_to_refresh_single_list,
				container, false);

		swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
		swipeView.setColorScheme(android.R.color.holo_blue_dark,
				android.R.color.holo_blue_light,
				android.R.color.holo_green_light,
				android.R.color.holo_green_light);

		// we shoudl siable the pull to refresh
		swipeView.setEnabled(false);
		lView = (ListView) rootView.findViewById(R.id.list);

		swipeView.setOnRefreshListener(this);
		// lView.setOnItemClickListener(this);
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onRefresh() {
		// getLoaderManager().initLoader(0, null, this);

		swipeView.setRefreshing(true);
		JsonFollowersResponse followers = SocketOperator.getInstance(
				JsonFollowersResponse.class).postResponse(
				this.getActivity(),
				SessionManager.getInstance(this.getActivity()).needsDetailsFollowersURL(id),
				SessionManager.getInstance(this.getActivity())
						.defaultSessionHeaders());

		// extract the users from the followers
		// then reload it into the adapter
		this.mAdapter = new FollowersViewAdapter(this.getActivity(),
				R.layout.user_item, followers.getUsers());

		mAdapter.notifyDataSetChanged();
		lView.setAdapter(mAdapter);
		swipeView.setRefreshing(false);
	}

}