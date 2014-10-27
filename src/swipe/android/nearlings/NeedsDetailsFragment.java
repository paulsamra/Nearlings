package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.viewAdapters.DiscoverListOfNeedsAdapter;
import swipe.android.nearlings.viewAdapters.NeedsDetailViewAdapter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

//need to check whether parent clas has sync. In fact, we just need to know how toa ccess it.
public class NeedsDetailsFragment extends Fragment {
	NeedsDetailViewAdapter adapter;
String id;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// we need to refrence our container class. For now, we should just
		// return a text message
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.needs_details, container, false);
		
		Bundle b = getActivity().getIntent().getExtras();
		id = b.getString("id");
		adapter = new NeedsDetailViewAdapter(view, this.getActivity(), id, savedInstanceState);

		return view;

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.switch_to_map_view, menu);
	}
}