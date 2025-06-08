package io.github.n1ck145.redhook.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {
    public static  List<String> splitLongString(String str, int lineLength) {
		List<String> lines = new ArrayList<>();
		int lastBreak = 0;
		int lastSpace = 0;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if (c == ' ') {
				lastSpace = i;
			}

			if (c == '\n') {
				lines.add(str.substring(lastBreak, i));
				lastBreak = i + 1;
				lastSpace = lastBreak;
			}

			if (i - lastBreak >= lineLength) {
				if (lastSpace > lastBreak) {
					lines.add(str.substring(lastBreak, lastSpace));
					lastBreak = lastSpace + 1;
				}
				else {
					lines.add(str.substring(lastBreak, i));
					lastBreak = i;
				}
				lastSpace = lastBreak;
			}
		}

		if (lastBreak < str.length()) {
			lines.add(str.substring(lastBreak));
		}

		return lines;
	}

	public static String splitCamelCase(String original) {
		if (original == null || original.isEmpty()) {
			return original;
		}

		StringBuilder result = new StringBuilder();
		result.append(original.charAt(0));

		for (int i = 1; i < original.length(); i++) {
			char current = original.charAt(i);
			if (Character.isUpperCase(current)) {
				result.append(" ");
			}
			result.append(current);
		}

		return result.toString();
	}
}
