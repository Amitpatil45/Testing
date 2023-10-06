package com.root32.onboarding.util;

public class StringUtil {
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		}

		return false;
	}
}
