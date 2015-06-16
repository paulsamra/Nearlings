package swipe.android.nearlings;

import java.math.BigDecimal;

import swipe.android.DatabaseHelpers.EventsDatabaseHelper;
import swipe.android.DatabaseHelpers.GroupsDatabaseHelper;
import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsCommentsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsOfferDatabaseHelper;
import swipe.android.DatabaseHelpers.UserReviewDatabaseHelper;
import swipe.android.nearlings.MessagesSync.EventsDetailsRequest;
import swipe.android.nearlings.MessagesSync.GroupsRequest;
import swipe.android.nearlings.MessagesSync.MessagesRequest;
import swipe.android.nearlings.MessagesSync.NearlingsSyncHelper;
import swipe.android.nearlings.MessagesSync.NeedsDetailsRequest;
import swipe.android.nearlings.MessagesSync.NeedsExploreRequest;
import swipe.android.nearlings.MessagesSync.NeedsOffersRequest;
import swipe.android.nearlings.MessagesSync.UserReviewsRequest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.edbert.library.containers.VolleyCoreApplication;
import com.edbert.library.database.DatabaseCommandManager;
import com.edbert.library.sendRequest.SendRequestStrategyManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
public class NearlingsApplication extends MultiDexApplication implements
		Application.ActivityLifecycleCallbacks,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, LocationListener {

	public static final boolean DEVELOPER_MODE = false;
	private GoogleApiClient mGoogleApiClient;

	private LocationRequest mLocationRequest;

	NearlingsSyncHelper helper;
	// paypal stuff
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

	// note that these credentials will differ between live & sandbox
	// environments.
	private static final String CONFIG_CLIENT_ID = "ARd1qxBEIiA16h2AGXwUYIotADloQCu4Z98yc2XDJTCw6165CEl8L6i6QpSe";

	public static final int REQUEST_CODE_PAYMENT = 1;
	public static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	public static final int REQUEST_CODE_PROFILE_SHARING = 3;
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	public static PayPalConfiguration paypalConfig = new PayPalConfiguration()
			.environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("Nearlings Store Title")
			.merchantPrivacyPolicyUri(
					Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(
					Uri.parse("https://www.example.com/legal"));

	private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

	public static final String DOER_ID = "DOER_ID";

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		buildGoogleApiClient();
		helper = new NearlingsSyncHelper(this);
		initImageLoader(getApplicationContext());
		registerDatabaseTables();
		DatabaseCommandManager.createAllTables(NearlingsContentProvider
				.getDBHelperInstance(this).getWritableDatabase());

		SendRequestStrategyManager.register(new MessagesRequest(this));
		SendRequestStrategyManager.register(new NeedsExploreRequest(this));
		SendRequestStrategyManager.register(new EventsDetailsRequest(this));
		SendRequestStrategyManager.register(new GroupsRequest(this));
		SendRequestStrategyManager.register(new NeedsOffersRequest(this));
		SendRequestStrategyManager.register(new UserReviewsRequest(this));

		SendRequestStrategyManager.register(new NeedsDetailsRequest(this));

		super.registerActivityLifecycleCallbacks(this);

		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
		startService(intent);

	}

	public static PayPalPayment generatePayObject(double price, String item,
			String paymentIntent) {
		return new PayPalPayment(new BigDecimal(price), "USD", item,
				paymentIntent);
	}

	@Override
	public void onTerminate() {
		super.unregisterActivityLifecycleCallbacks(this);
		super.onTerminate();
	}

	public Location getCurrentLocation() {
		return location;
	}

	private void registerDatabaseTables() {
		DatabaseCommandManager.register(new MessagesDatabaseHelper());
		DatabaseCommandManager.register(new NeedsDetailsDatabaseHelper());
		DatabaseCommandManager.register(new NeedsCommentsDatabaseHelper());
		DatabaseCommandManager.register(new EventsDatabaseHelper());
		DatabaseCommandManager.register(new GroupsDatabaseHelper());
		DatabaseCommandManager.register(new NeedsOfferDatabaseHelper());
		DatabaseCommandManager.register(new UserReviewDatabaseHelper());

	}

	public static void initImageLoader(Context context) {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}

	public static DisplayImageOptions getDefaultOptions() {
		return new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public NearlingsSyncHelper getSyncHelper() {
		return helper;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {

		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(1000); // Update location every second

		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);

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
		// mLocationClient.connect();
		mGoogleApiClient.connect();
		lastAct = activity;
	}

	Activity lastAct;

	@Override
	public void onActivityPaused(Activity activity) {
		// mLocationClient.disconnect();

		mGoogleApiClient.disconnect();
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

	Location location;

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

		location = arg0;
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub

	}

	public Location getLastLocation() {
		return location;
	}

	public static void displayNetworkNotAvailableDialog(Context ctx) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		String networkMessage = ctx
				.getString(R.string.not_connected_to_internet_msg);
		String networkTitle = ctx
				.getString(R.string.not_connected_to_internet_title);

		builder.setMessage(networkMessage).setTitle(networkTitle)
				.setCancelable(true)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	// Volley
	private static NearlingsApplication mInstance;

	public static synchronized NearlingsApplication getInstance() {
		return mInstance;
	}

	public void logoutDialog(final Activity act) {

		AlertDialog.Builder builder = new AlertDialog.Builder(lastAct);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				((NearlingsApplication) NearlingsApplication.this).logout();
				act.finish();
			}
		});
		builder.setTitle("Error");
		builder.setMessage("Token expired or invalid. Please log in again.");

		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.setCanceledOnTouchOutside(false);
		alert.show();
	}

	public void logout() {
		NearlingsApplication nap = (NearlingsApplication) this
				.getApplicationContext();
		NearlingsSyncHelper nsh = nap.getSyncHelper();

		ContentResolver.cancelSync(nsh.getAccount(), nsh.getAuthority());

		// clear all data tables
		NearlingsContentProvider a = new NearlingsContentProvider();
		a.clearAllTables();

		// reset to neutral
		SessionManager.getInstance(this).resetTables();

		// need to clear all userpref
		SessionManager.getInstance(this).clearUserPref();

		// notfiy user of logged out?
		// ((MainActivity)).reloadNavigationDrawer();
		Intent i = new Intent(lastAct, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		this.startActivity(i);
	}

}
