package com.edbert.library.sendRequest;

/**
 * Anything that implements this interface is responsible for reading from a source and 
 * writing itself to the database. Ideally, we'd keep the source and writing separate
 * in case the need ever arose for people to want to read from multiple sources
 * that gave the same response but this is highly unlikely.
 * 
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class SendRequestStrategyManager {
	private static HashMap<String, SendRequestInterface> listOfSendRequestInterfaces = new HashMap<String, SendRequestInterface>();

	public static Object executeRequest(SendRequestInterface requestInterface,
			Bundle b) {
		return requestInterface.makeRequest(b);
	}

	public static boolean executePostRetrieval(
			SendRequestInterface requestInterface, Context c, Object o, Bundle extras) {
		if (requestInterface == null) {
			Log.e("SendRequestStrategyManager", "No requestInterface specified");
			return false;
		}
		try{
		return requestInterface.executePostRetrieval(extras, c, o);
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static void executeWriteToDatabase(
			SendRequestInterface requestInterface, Context c, Object o, Bundle extras) {
		if (requestInterface == null) {
			Log.e("SendRequestStrategyManager", "No requestInterface specified");
			return;
		}
		requestInterface.writeToDatabase(extras, c, o);
	}

	public static SendRequestInterface getHelper(String tag) {
		return listOfSendRequestInterfaces.get(tag);
	}
	public static ArrayList<SendRequestInterface> getHelper(ArrayList<String> tag) {
		ArrayList<SendRequestInterface> listOfHelpers = new ArrayList<SendRequestInterface>();
		for (String helperString : tag) {
			listOfHelpers.add(getHelper(helperString));
		}
		return listOfHelpers;
	}
	public static void register(SendRequestInterface helper) {
		String tag = generateTag(helper);
		listOfSendRequestInterfaces.put(tag, helper);
	}

	public static String generateTag(SendRequestInterface helper) {
		return helper.getClass().getCanonicalName().toString();
	}

	public static String generateTag(Class helper) {
		return helper.getCanonicalName().toString();
	}

	public static ArrayList<String> generateTag(
			List<SendRequestInterface> helper) {
		ArrayList<String> listOfHelpers = new ArrayList<String>();
		for (SendRequestInterface helpersInterfaces : helper) {
			listOfHelpers.add(generateTag(helpersInterfaces));
		}
		return listOfHelpers;
	}

	public static SendRequestInterface getHelper(Class helper) {
		return getHelper(generateTag(helper));
		// return helper.getCanonicalName().toString();
	}
}