package com.edbert.library.sendRequest;

import android.content.Context;
import android.os.Bundle;

import com.edbert.library.network.sync.JsonResponseInterface;

public interface SendRequestInterface<T> {
	public T makeRequest(Bundle b);

	public abstract Class<T> getJSONclass();

	public boolean executePostRetrieval(Bundle b, Context c, T o) throws Exception;

	public abstract boolean writeToDatabase(Bundle b, Context c, T o);
}