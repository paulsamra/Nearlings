/**
 * Class is the container for a listview and mapview. Both listview and mapview represent
 * 2 views of the same action. 
 */
package swipe.android.nearlings;

import java.text.NumberFormat;
import java.util.ArrayList;

import swipe.android.nearlings.MessagesSync.GroupsRequest;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.discover.options.SearchFilterCategoryOptionsListAdapter;
import swipe.android.nearlings.discover.options.SearchOptionsFilter;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
		TypedArray icons = res.obtainTypedArray(R.array.needs_types_unchecked);
		String[] terms = res.getStringArray(R.array.needs_types);
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
				}
				// searchTerm.setText(term);
				adapter.notifyDataSetChanged();
				// requeue with search filters. We should pass in filters at
				// some point
				// DiscoverContainerFragment.this.onRefresh();
				SessionManager.getInstance(
						GroupsContainerFragment.this.getActivity())
						.setSearchString(term);
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

					term = "-1";
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

	private void setUpStatus(View rootView) {
		final Button b = (Button) rootView.findViewById(R.id.status_button);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final String[] items = getResources().getStringArray(
						R.array.needs_statuses);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						GroupsContainerFragment.this.getActivity());

				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						SessionManager.getInstance(
								GroupsContainerFragment.this.getActivity())
								.setSearchStatus(items[item]);
						b.setText(items[item]);
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}

		});
		String searchStatus = SessionManager.getInstance(
				GroupsContainerFragment.this.getActivity()).getSearchStatus();
		if (!searchStatus.equals(SessionManager.SEARCH_DEFAULT_FILTER))
			b.setText(searchStatus);
	}

	private void setUpMinimumReward(View rootView) {
		final EditText et = (EditText) rootView
				.findViewById(R.id.reward_minimum);
		et.addTextChangedListener(new TextWatcher() {
			private String current = "";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!s.toString().equals(current)) {
					et.removeTextChangedListener(this);

					String cleanString = s.toString().replaceAll("[$,.]", "");

					double parsed = Double.parseDouble(cleanString);
					SessionManager.getInstance(
							GroupsContainerFragment.this.getActivity())
							.setSearchRewardMinimum((float) parsed);
					String formatted = NumberFormat.getCurrencyInstance()
							.format((parsed / 100));

					current = formatted;
					et.setText(formatted);
					et.setSelection(formatted.length());

					et.addTextChangedListener(this);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		float reward = SessionManager.getInstance(
				GroupsContainerFragment.this.getActivity())
				.getSearchRewardMinimum();
		if (reward != SessionManager.SEARCH_DEFAULT_NUMERIC) {
			double parsed = (double) reward;
			String formatted = NumberFormat.getCurrencyInstance().format(
					(parsed / 100));

			et.setText(formatted);
			et.setSelection(formatted.length());
		}

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
		View dialoglayout = inflater.inflate(R.layout.search_popup, null);

		// filters are the categories
		setUpFilters(dialoglayout);
		setUpStatus(dialoglayout);
		setUpRadius(dialoglayout);
		setUpMinimumReward(dialoglayout);

		builder.setView(dialoglayout);
		builder.show();
	}

	public void requestUpdate() {
		Bundle b = new Bundle();
		SessionManager sm = SessionManager.getInstance(this.getActivity());
		Location currentLocation = ((NearlingsApplication) this.getActivity()
				.getApplication()).getLastLocation();

		if (sm.getSearchLocation() != "") {
			b.putString(NeedsDetailsRequest.BUNDLE_LOCATION,
					sm.getSearchLocation());
			b.putString(NeedsDetailsRequest.BUNDLE_LOCATION_TYPE,
					NeedsDetailsRequest.BUNDLE_LOCATION_TYPE_ADDRESS);
			b.putFloat(NeedsDetailsRequest.BUNDLE_RADIUS, 20.0f);
		}

		if (sm.getSearchRewardMinimum() != -1) {

			b.putFloat(NeedsDetailsRequest.BUNDLE_REWARD,
					sm.getSearchRewardMinimum());
		}

		if (sm.getSearchString() != ""
				&& !sm.getSearchString().equals(
						SessionManager.SEARCH_DEFAULT_FILTER)) {
			b.putString(NeedsDetailsRequest.BUNDLE_KEYWORDS,
					sm.getSearchString());
		}

		super.onRefresh(b);
	}

	@Override
	public Fragment mapViewFragment() {
		// TODO Auto-generated method stub
		return new DiscoverMapViewFragment();
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

		super.helper = SendRequestStrategyManager
				.getHelper(GroupsRequest.class);

	}
}