package de.metagear.android.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

// reviewed
public class SharedPrefsAdapter {

	public static <T> void persist(Context context, T object)
			throws JsonGenerationException, JsonMappingException, IOException {
		String key = object.getClass().getName();
		String value = new ObjectMapper().writeValueAsString(object);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();

		editor.putString(key, value);

		editor.commit();
	}

	public static <T> T retrieve(Context context, Class<T> valueType)
			throws JsonParseException, JsonMappingException, IOException,
			InstantiationException, IllegalAccessException {
		String key = valueType.getName();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String value = prefs.getString(key, null);
		if (value == null) {
			return valueType.newInstance();
		}

		return new ObjectMapper().readValue(value, valueType);
	}
}
