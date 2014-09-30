package swipe.android.nearlings;

import swipe.android.nearlings.navigationdrawer.NavDrawerItemManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends NavDrawerActivity {
	@Override
	public void setUpNavDrawerTiles() {
		NavDrawerItemManager.getInstance().setDefaultLayout();
		super.setUpNavDrawerTiles();
	}

	@Override
	protected void setUpNavDrawerHeader() {

	}

	@Override
	protected void setUpNavDrawerFooter() {

	}

}
