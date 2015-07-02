package swipe.android.nearlings;

import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import swipe.android.nearlings.core.AmountPicker;
import swipe.android.nearlings.json.NearlingsResponse;
import swipe.android.nearlings.json.JsonWithdrawMoneyResponse.JsonWithdrawMoneyResponse;
import swipe.android.nearlings.json.UserHistory.JsonTransactionHistoryResponse;
import swipe.android.nearlings.json.UserHistory.PaymentHistory;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.edbert.library.network.AsyncTaskCompleteListener;
import com.edbert.library.network.GetDataWebTask;
import com.edbert.library.network.PostDataWebTask;
import com.edbert.library.utils.MapUtils;

public class ActivityOwnNeeds extends NearlingsActivity implements
        AsyncTaskCompleteListener<NearlingsResponse> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Your Needs");

        tabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mlam = new LocalActivityManager(this, false);
        mlam.dispatchCreate(savedInstanceState);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);



        updateTabs();
    }

    LocalActivityManager mlam;

    @Override
    public void onResume() {
        super.onResume();
        mlam.dispatchResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mlam.dispatchPause(isFinishing());
    }

    FragmentTabHost tabHost;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;
    }

    private void updateTabs() {

        tabHost.clearAllTabs();
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        Bundle arg1 = new Bundle();
        arg1.putString("type",  OwnNeedsFragment.CREATED_BY_SELF);
        tabHost.addTab(
                tabHost.newTabSpec("Your Needs").setIndicator("Your Needs", null),
                OwnNeedsFragment.class, arg1);

        Bundle arg2 = new Bundle();
        arg2.putString("type",  OwnNeedsFragment.CREATED_BY_OTHER);
        tabHost.addTab(
                tabHost.newTabSpec("Needs Done By You").setIndicator("Needs Done By You", null), OwnNeedsFragment.class, arg2);


        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#000000"));
        }
    }

}