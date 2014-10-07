package swipe.android.nearlings.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NearlingsSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();

    private static NearlingsSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new NearlingsSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}