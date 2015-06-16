package com.edbert.library.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListUtils {
	public static String listToString(ArrayList<String> map) {

		StringBuilder stringBuilder = new StringBuilder();
		for (String s : map) {
			stringBuilder.append(s + " ");
		}
		String s =  stringBuilder.toString();
		return s.substring(0, s.length()-1);
	}

	public static ArrayList<String> stringToList(String input) {
		ArrayList<String> map = new ArrayList<String>();

		String[] nameValuePairs = input.split(" ");
		for (String nameValuePair : nameValuePairs) {
			
			map.add(nameValuePair);
		}

		return map;
	}
}