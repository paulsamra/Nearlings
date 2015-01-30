/**
 * Class is the container for a listview and mapview. Both listview and mapview represent
 * 2 views of the same action. 
 */
package swipe.android.nearlings.groups;

import java.util.ArrayList;

import swipe.android.nearlings.BaseContainerFragment;
import swipe.android.nearlings.NearlingsApplication;
import swipe.android.nearlings.R;
import swipe.android.nearlings.SessionManager;
import swipe.android.nearlings.MessagesSync.GroupsRequest;
import swipe.android.nearlings.MessagesSync.NeedsExploreRequest;
import swipe.android.nearlings.discover.options.SearchFilterCategoryOptionsListAdapter;
import swipe.android.nearlings.discover.options.SearchOptionsFilter;
import swipe.android.nearlings.json.groups.GroupsMapViewFragment;
import swipe.android.nearlings.needs.DiscoverContainerFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.edbert.library.sendRequest.SendRequestStrategyManager;
import com.meetme.android.horizontallistview.HorizontalListView;

public class GroupsContainerFragment extends BaseContainerFragment {
	public static final String MESSAGES_START_FLAG = GroupsContainerFragment.class
			.getCanonicalName() + "_MESSAGES_START_FLAG";
	public static final String MESSAGES_FINISH_FLAG = GroupsContainerFragment.class
			.getCanonicalName() + "_MESSAGES_FINISH_FLAG";

	// setup filters! Begin area where we customize. All these functions are for
	// dynamically adding behavior to the
	// filter popup.
	private void setUpFilters(View rootView) {
		final HorizontalListView listview = (HorizontalListView) rootView
				.findViewById(R.id.search_options_listview_categories);
		final ArrayList<SearchOptionsFilter> listOfFilter = new ArrayList();
		Resources res = getResources();
		TypedArray icons = res.obtainTypedArray(R.array.group_category_types_unchecked);
		String[] terms = res.getStringArray(R.array.group_category_types);
		// Drawable drawable = icons.getDrawable(0);

		for (int i = 0; i < icons.length(); i++) {
			int resource = icons.getResourceId(i, 0);
			listOfFilter
					.add(new SearchOptionsFilter(false, resource, terms[i]));
		}

		final SearchFilterCategoryOptionsListAdapter adapter = new SearchFilterCategoryOptionsListAdapter(
				this.getActivity(), R.layout.search_options_view_item,
				listOfFilter);

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				boolean current = listOfFilter.get(position).isSelected();
				for (SearchOptionsFilter f : listOfFilter)
					f.setSelected(false);

				listOfFilter.get(position).setSelected(!current);
				String term = listOfFilter.get(position).getSearchTerm();
				if (current) {
					term = "All";
					SessionManager.getInstance(
							GroupsContainerFragment.this.getActivity())
							.setGroupCategory(SessionManager.DEFAULT_STRING);
				}else{
					SessionManager.getInstance(
							GroupsContainerFragment.this.getActivity())
							.setGroupCategory(term);
				}
				// searchTerm.setText(term);
				adapter.notifyDataSetChanged();
				// requeue with search filters. We should pass in filters at
				// some point
				// DiscoverContainerFragment.this.onRefresh();
			}
		});

		for (SearchOptionsFilter f : listOfFilter) {
			if (f.getSearchTerm().equals(
					SessionManager.getInstance(this.getActivity())
							.getSearchString())) {
				f.setSelected(true);
			}
		}
	}

	private void setUpRadius(View rootView) {
		final HorizontalListView listview = (HorizontalListView) rootView
				.findViewById(R.id.radius_selection);
		final ArrayList<SearchOptionsFilter> listOfFilter = new ArrayList();
		Resources res = getResources();
		TypedArray selectedIcons = res
				.obtainTypedArray(R.array.radius_selected_icons);
		TypedArray unselectedIcons = res
				.obtainTypedArray(R.array.radius_unselected_icons);

		String[] radius = res.getStringArray(R.array.radius);
		for (int i = 0; i < radius.length; i++) {

			int selectedResource = selectedIcons.getResourceId(i, 0);
			int unselectedResource = unselectedIcons.getResourceId(i, 0);

			listOfFilter.add(new SearchOptionsFilter(false, unselectedResource,
					selectedResource, radius[i]));
		}

		final SearchFilterCategoryOptionsListAdapter adapter = new SearchFilterCategoryOptionsListAdapter(
				this.getActivity(),
				R.layout.search_options_view_item_unsquared, listOfFilter);

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				boolean current = listOfFilter.get(position).isSelected();
				for (SearchOptionsFilter f : listOfFilter)
					f.setSelected(false);

				listOfFilter.get(position).setSelected(!current);
				String term = listOfFilter.get(position).getSearchTerm();
				if (current) {
					term = String.valueOf(SessionManager.DEFAULT_VALUE);
				}
				// searchTerm.setText(term);
				adapter.notifyDataSetChanged();
				// requeue with search filters. We should pass in filters at
				// some point
				SessionManager.getInstance(
						GroupsContainerFragment.this.getActivity())
						.setSearchRadius(Float.valueOf(term));
			}
		});

		for (SearchOptionsFilter f : listOfFilter) {
			if (f.getSearchTerm().equals(
					String.valueOf(SessionManager.getInstance(
							this.getActivity()).getSearchRadius()))) {
				f.setSelected(true);
			}
		}
	}

	// privacy
		private void setUpStatus(View rootView) {
			final Button b = (Button) rootView.findViewById(R.id.private_public_btn);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final String[] items = getResources().getStringArray(
							R.array.event_privacy);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							GroupsContainerFragment.this.getActivity());

					builder.setItems(items, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							// change this
							SessionManager.getInstance(
									GroupsContainerFragment.this.getActivity())
									.setSearchString(items[item]);

							b.setText(items[item]);
							dialog.cancel();
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}

			});
			String searchStatus = SessionManager.getInstance(
					GroupsContainerFragment.this.getActivity()).getSearchString();
			if (!searchStatus.equals(SessionManager.DEFAULT_STRING))
				b.setText(searchStatus);
		}

	public void generatePopup() {
		// custom dialog

		// setup
		AlertDialog.Builder builder = new AlertDialog.Builder(super.context);
		TextView title = new TextView(super.context);

		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextSize(40);

		title.setTextSize(30.0f);
		title.setText("Filters");

		builder.setCustomTitle(title)
				.setCancelable(true)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						})
				.setPositiveButton("Search",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								SessionManager.getInstance(
										GroupsContainerFragment.this
												.getActivity())
										.commitPendingChanges();
								GroupsContainerFragment.this
										.updateSearchString();

								requestUpdate();
							}
						});

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialoglayout = inflater.inflate(R.layout.group_filters_popup, null);

		// filters are the categories
		setUpFilters(dialoglayout);
		setUpStatus(dialoglayout);
		setUpRadius(dialoglayout);

		builder.setView(dialoglayout);
		builder.show();
	}

	public void requestUpdate() {
		Bundle b = new Bundle();
		SessionManager sm = SessionManager.getInstance(this.getActivity());
		Location currentLocation = ((NearlingsApplication) this.getActivity()
				.getApplication()).getLastLocation();

		if (!sm.getSearchLocation().equals("")) {
			b.putString(NeedsExploreRequest.BUNDLE_LOCATION,
					sm.getSearchLocation());
			b.putString(NeedsExploreRequest.BUNDLE_LOCATION_TYPE,
					NeedsExploreRequest.BUNDLE_LOCATION_TYPE_ADDRESS);
			b.putFloat(NeedsExploreRequest.BUNDLE_RADIUS, 20.0f);
		}

		if (sm.getSearchRewardMinimum() != -1) {

			b.putFloat(NeedsExploreRequest.BUNDLE_REWARD,
					sm.getSearchRewardMinimum());
		}

		if (!sm.getSearchString().equals("")
				&& !sm.getSearchString().equals(
						SessionManager.DEFAULT_STRING)) {
			b.putString(NeedsExploreRequest.BUNDLE_KEYWORDS,
					sm.getSearchString());
		}

		super.onRefresh(b);
	}

	@Override
	public Fragment mapViewFragment() {
		return new GroupsMapViewFragment();
	}

	@Override
	public Fragment listViewFragment() {
		return new GroupsListFragment();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public String syncStartedFlag() {
		return MESSAGES_START_FLAG;
	}

	@Override
	public String syncFinishedFlag() {
		return MESSAGES_FINISH_FLAG;
	}

	@Override
	public void setSourceRequestHelper() {
		helpers.add(SendRequestStrategyManager
				.getHelper(GroupsRequest.class));

	}
}