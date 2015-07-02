package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.BalanceDatabaseHelper;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.BalanceRequest;
import swipe.android.nearlings.MessagesSync.MessagesRequest;
import swipe.android.nearlings.MessagesSync.NeedsExploreRequest;
import swipe.android.nearlings.json.UserHistory.PaymentHistory;
import swipe.android.nearlings.needs.DiscoverContainerFragment;
import swipe.android.nearlings.needs.DiscoverListViewFragment;
import swipe.android.nearlings.sync.NearlingsSyncAdapter;
import swipe.android.nearlings.viewAdapters.BalanceViewAdapter;
import swipe.android.nearlings.viewAdapters.MessagesViewAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class OwnNeedsFragment extends DiscoverListViewFragment {
String type;
    public static final String CREATED_BY_SELF = "CREATED_BY_SELF";

    public static final String CREATED_BY_OTHER = "CREATED_BY_OTHER";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View v=  super.onCreateView(inflater, container, savedInstanceState);

        type = getArguments().getString("type");
        return v;
    }
    @Override
    public void onRefresh() {
        previousAmount = -1;
        requestUpdate(BaseContainerFragment.is_reload_and_blank);
        // DiscoverContainerFragment.
    }


    public  void requestUpdate(int status) {
        Bundle b = new Bundle();
        SessionManager sm = SessionManager.getInstance(this.getActivity());
        Location currentLocation = ((NearlingsApplication) this.getActivity()
                .getApplication()).getLastLocation();

        if (sm.getSearchLocation() != null
                && !sm.getSearchLocation().equals("")) {
            String location_string = sm.getSearchLocation();
            String url_encode_location = location_string;
            try {
                url_encode_location = URLEncoder.encode(location_string,
                        "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
                b.putString(NeedsExploreRequest.BUNDLE_LOCATION,
                        url_encode_location);
            }

            b.putString(NeedsExploreRequest.BUNDLE_LOCATION_TYPE,
                    NeedsExploreRequest.BUNDLE_LOCATION_TYPE_ADDRESS);
            b.putFloat(NeedsExploreRequest.BUNDLE_RADIUS,
                    SessionManager.DEFAULT_SEARCH_RADIUS);
        } else if (currentLocation != null) {
            b.putString(NeedsExploreRequest.BUNDLE_LOCATION_TYPE,
                    NeedsExploreRequest.BUNDLE_LOCATION_TYPE_COORDINATES);
            b.putString(NeedsExploreRequest.BUNDLE_LOCATION_LATITUDE,
                    String.valueOf(currentLocation.getLatitude()));
            b.putString(NeedsExploreRequest.BUNDLE_LOCATION_LONGITUDE,
                    String.valueOf(currentLocation.getLongitude()));

            b.putFloat(NeedsExploreRequest.BUNDLE_RADIUS,
                    SessionManager.DEFAULT_SEARCH_RADIUS);
        }

        if (sm.getSearchRewardMinimum() != sm.DEFAULT_VALUE
                && sm.getSearchRewardMinimum() != 0) {
            b.putFloat(NeedsExploreRequest.BUNDLE_REWARD,
                    sm.getSearchRewardMinimum());
        }

        if (sm.getExploreCategory() != null
                && !sm.getExploreCategory().equals(
                SessionManager.DEFAULT_STRING)) {
            b.putString(NeedsExploreRequest.BUNDLE_CATEGORY,
                    sm.getExploreCategory());
        }
        if (sm.getSearchString() != null
                && !sm.getSearchString().equals(SessionManager.DEFAULT_STRING)) {
            String s = sm.getSearchString();

            try {
                s = URLEncoder.encode(s, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
                b.putString(NeedsExploreRequest.BUNDLE_KEYWORDS, s);
            }

        }

        if (sm.getSearchVisibility() != null
                && !sm.getSearchVisibility().equals("")) {
            b.putString(NeedsExploreRequest.BUNDLE_VISIBILITY,
                    sm.getSearchVisibility());
        }else{
            b.putString(NeedsExploreRequest.BUNDLE_VISIBILITY, "public");
        }
		/*
		 * if(sm.getTimeEnd() != sm.DEFAULT_VALUE){
		 * b.putLong(NeedsDetailsRequest.BUNDLE_TIME_END, sm.getTimeEnd()); }
		 *
		 * if(sm.getTimeStart() != sm.DEFAULT_VALUE){
		 * b.putLong(NeedsDetailsRequest.BUNDLE_TIME_END, sm.getTimeStart()); }
		 */
        b.getInt(NearlingsSyncAdapter.LIMIT, setNumElements());
        if(status == BaseContainerFragment.is_reload_and_blank){
            footerView.setVisibility(View.VISIBLE);
            b.putInt(BaseContainerFragment.STATUS, BaseContainerFragment.is_reload_and_blank);
            super.onRefresh(b, true);
        }else if(status == BaseContainerFragment.is_maintain) {
            footerView.setVisibility(View.VISIBLE);
            b.putInt(NearlingsSyncAdapter.LIMIT, setNumElements());
            b.putInt(BaseContainerFragment.STATUS, BaseContainerFragment.is_maintain);
            super.onRefresh(b, false);
        }else{
            footerView.setVisibility(View.VISIBLE);
            b.putInt(NearlingsSyncAdapter.LIMIT, setNumElements());
            b.putInt(BaseContainerFragment.STATUS,BaseContainerFragment.is_loadMore);
            super.onRefresh(b, false);
        }
    }
    @Override
    public CursorLoader generateCursorLoader() {

        String allActiveSearch = null;

        String[] activeStates = { "" };
        if(type.equals(CREATED_BY_OTHER)){

            allActiveSearch=NeedsDetailsDatabaseHelper.COLUMN_ASSIGNED_TO + " = ?";
            activeStates[0]= SessionManager.getInstance(this.getActivity()).getUserID();
        }else{

            allActiveSearch=NeedsDetailsDatabaseHelper.COLUMN_CREATED_BY + " = ?";
            activeStates[0]= SessionManager.getInstance(this.getActivity()).getUserID();
        }


        CursorLoader cursorLoader = new CursorLoader(
                this.getActivity(),
                NearlingsContentProvider
                        .contentURIbyTableName(NeedsDetailsDatabaseHelper.TABLE_NAME),
                NeedsDetailsDatabaseHelper.COLUMNS, allActiveSearch,
                activeStates, NeedsDetailsDatabaseHelper.COLUMN_DUE_DATE + " DESC");

        return cursorLoader;

    }





}