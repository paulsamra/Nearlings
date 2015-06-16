package de.metagear.android.activity.async;

import android.app.Activity;

// reviewed
public abstract class AbstractAsyncActivity extends Activity implements
		AsyncActivity {
	private AsyncActivitySupport asyncSupport;

	public AbstractAsyncActivity() {
		super();
		this.asyncSupport = new AsyncActivitySupport(this);
	}

	@Override
	public void showLoadingProgressDialog() {
		asyncSupport.showLoadingProgressDialog();
	}

	@Override
	public void dismissProgressDialog() {
		asyncSupport.dismissProgressDialog();
	}
}
