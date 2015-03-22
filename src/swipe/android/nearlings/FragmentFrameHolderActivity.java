package swipe.android.nearlings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


public abstract class FragmentFrameHolderActivity extends NearlingsActivity{
	
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            if (savedInstanceState == null) {
                // During initial setup, plug in the details fragment.

                // create fragment
                Fragment details = getFragment();
                details.setArguments(getIntent().getExtras());

                this.getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, details).commit();
            }
        
    }
        public abstract Fragment getFragment();
}