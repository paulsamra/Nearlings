package de.metagear.android.activity.async;

// reviewed
public interface AsyncActivity {
	void showLoadingProgressDialog();

	void dismissProgressDialog();

	void onAsyncTaskFailed(Class<?> taskClass, int requestCode,
			Throwable throwable);
}
