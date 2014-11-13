package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsCommentsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.MessagesSync.MessagesRequest;
import swipe.android.nearlings.MessagesSync.NearlingsSyncHelper;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.sendRequest.SendRequestStrategyManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class NearlingsApplication extends Application  implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, Application.ActivityLifecycleCallbacks {
	LocationClient mLocationClient;
	NearlingsSyncHelper helper;

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onCreate() {
		super.onCreate();

		mLocationClient = new LocationClient(this, this, this);
		helper = new NearlingsSyncHelper(this);
		initImageLoader(getApplicationContext());
		registerDatabaseTables();
	//	Context c = this;
		DatabaseCommandManager.createAllTables(NearlingsContentProvider
				.getDBHelperInstance(this).getWritableDatabase());
		
		SendRequestStrategyManager.register(new MessagesRequest(this));
		SendRequestStrategyManager.register(new NeedsDetailsRequest(this));
		super.registerActivityLifecycleCallbacks(this);
	}
	@Override
	public void onTerminate(){
		super.unregisterActivityLifecycleCallbacks(this);
		super.onTerminate();
	}
	public Location getCurrentLocation() {
		return mLocationClient.getLastLocation();
	}

	private void registerDatabaseTables(){
		DatabaseCommandManager.register(new MessagesDatabaseHelper());
		DatabaseCommandManager.register(new NeedsDetailsDatabaseHelper());
		DatabaseCommandManager.register(new NeedsCommentsDatabaseHelper());
	}
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}
	
	public static DisplayImageOptions getDefaultOptions(){
	return new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	public NearlingsSyncHelper getSyncHelper(){
		return helper;
	}
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onActivityStarted(Activity activity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onActivityResumed(Activity activity) {
		mLocationClient.connect();
		
	}
	@Override
	public void onActivityPaused(Activity activity) {
		mLocationClient.disconnect();
		
	}
	@Override
	public void onActivityStopped(Activity activity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onActivityDestroyed(Activity activity) {
		// TODO Auto-generated method stub
		
	}
}