package swipe.android.nearlings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import swipe.android.nearlings.MessagesSync.NeedsCommentsRequest;

import com.edbert.library.containers.TabsActivityContainer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class NeedsDetailsActivity extends TabsActivityContainer {
	String id = "0";

	Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar ab = this.getSupportActionBar();
		ab.setDisplayShowTitleEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.refresh_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.refresh_details:
			// for dev purpose add comment
			NeedsCommentsRequest rq = new NeedsCommentsRequest(this);
			rq.writeToDatabase(this, null);
			int pos = this.getSupportActionBar().getSelectedTab().getPosition();
			String tag = createTag(super.mapFragList.getValue(pos));

			Refreshable f = ((Refreshable) getSupportFragmentManager()
					.findFragmentByTag(tag));
			f.onRefresh();
			break;
		// refresh the child fragment
		default:
			onBackPressed();

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void addDefaultFragments() {
		mapFragList.put("Details", new NeedsDetailsFragment());
		mapFragList.put("Followers", new NeedsFollowersFragment());
		mapFragList.put("Offers", new NeedsBidsFragment());

	}

	@Override
	public void onBackPressed() {
		this.finish();
	}

}