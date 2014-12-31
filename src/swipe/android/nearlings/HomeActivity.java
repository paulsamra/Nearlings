package swipe.android.nearlings;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class HomeActivity extends Activity {
  GridView grid;
 
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Resources res = getResources();
	TypedArray icons = res.obtainTypedArray(R.array.home_options_icon);
	String[] home_options = res.getStringArray(R.array.home_options);
	int[] home_icons = new int[icons.length()];
	for(int i = 0 ; i < icons.length(); i++){
		home_icons[i] =  icons.getResourceId(i, 0);
	}
    MainMenuGridAdapter adapter = new MainMenuGridAdapter(this, home_options, home_icons);
    grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //Toast.makeText(HomeActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                }
            });
  }
}