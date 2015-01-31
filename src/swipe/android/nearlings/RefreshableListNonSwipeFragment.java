package swipe.android.nearlings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.edbert.library.network.AsyncTaskCompleteListener;

//TODO: Probably want to abstract this
public abstract class RefreshableListNonSwipeFragment<T> extends Fragment
		implements SwipeRefreshLayout.OnRefreshListener, Refreshable,
		AsyncTaskCompleteListener<T> {
	ListView lView;
	SwipeRefreshLayout swipeView;

	// Extra stuff
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

		swipeView.setEnabled(false);
		lView = (ListView) rootView.findViewById(R.id.list);

		swipeView.setOnRefreshListener(this);
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onPause() {
		super.onPause();
		if (task != null)
			task.cancel(true);

		swipeView.setRefreshing(false);
	}

	AsyncTask<String, Void, T> task;

	@Override
	public void onRefresh() {
		if (swipeView.isRefreshing()) {
			return;
		}

	}

	public abstract void onTaskComplete(T result);

}
/**
package swipe.android.nearlings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.edbert.library.network.AsyncTaskCompleteListener;

//TODO: Probably want to abstract this
public abstract class RefreshableListNonSwipeFragment<T> extends NearlingsSwipeToRefreshFragment {


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		super.swipeView.setEnabled(false);
		return view;
	}


}**/