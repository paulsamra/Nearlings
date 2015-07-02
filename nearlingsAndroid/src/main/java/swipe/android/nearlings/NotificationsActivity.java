package swipe.android.nearlings;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class NotificationsActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  addPreferencesFromResource(R.xml.preference_list_content);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        // use action bar here
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Notifications");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        this.finish();
        return true;
    }
    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle paramBundle) {
            super.onCreate(paramBundle);
            addPreferencesFromResource(R.xml.preference_notification);
        }
    }

}