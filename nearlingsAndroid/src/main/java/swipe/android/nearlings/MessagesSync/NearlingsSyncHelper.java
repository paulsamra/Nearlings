package swipe.android.nearlings.MessagesSync;

import org.michenux.drodrolib.network.sync.AbstractSyncHelper;
import org.michenux.drodrolib.network.sync.SyncHelperInterface;

import swipe.android.nearlings.R;
import android.content.Context;


public class NearlingsSyncHelper extends AbstractSyncHelper {
	String ACCOUNT_NAME;
	String AUTHORITY;
	String ACCOUNT_TYPE;
	public NearlingsSyncHelper(boolean enableSync, Context context) {
		super(enableSync, context);
	}

	public NearlingsSyncHelper(Context context) {
		super(context);
	}

	@Override
	public boolean equals(SyncHelperInterface e) {
		if (e instanceof NearlingsSyncHelper) {
			NearlingsSyncHelper messagesHelper = (NearlingsSyncHelper) e;
			if (messagesHelper.mAuthority.equals(this.mAuthority)
					&& messagesHelper.mAccount.equals(this.mAccount)) {
				return true;
			}
		}
		return false;
	}



	@Override
	public String getDefaultAccountName() {
		return c.getString(R.string.nearlings_accountName);
	}

	@Override
	public String getDefaultAccountType() {
		return c.getString(R.string.nearlings_accountType);
	}

	@Override
	public String getDefaultAuthority() {
		return c.getString(R.string.nearlings_sync_contentAuthority);
	}

}