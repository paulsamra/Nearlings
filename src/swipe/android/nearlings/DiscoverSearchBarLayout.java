package swipe.android.nearlings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class DiscoverSearchBarLayout extends RelativeLayout
{       
    public DiscoverSearchBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);      
        LayoutInflater.from(context).inflate(R.layout.search_bar_layout, this, true);    
    }
}