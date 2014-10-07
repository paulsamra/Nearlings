package swipe.android.nearlings;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.nearlings.MessagesSync.NearlingsSyncHelper;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.provider.SyncStateContract.Constants;

import com.edbert.library.database.DatabaseCommandManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class NearlingsApplication extends Application {

	NearlingsSyncHelper helper;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		super.onCreate();
		helper = new NearlingsSyncHelper(this);
		initImageLoader(getApplicationContext());
		registerDatabaseTables();
	}

	private void registerDatabaseTables(){
		DatabaseCommandManager.register(new MessagesDatabaseHelper());
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
	
	public NearlingsSyncHelper getSyncHelper(){
		return helper;
	}
}