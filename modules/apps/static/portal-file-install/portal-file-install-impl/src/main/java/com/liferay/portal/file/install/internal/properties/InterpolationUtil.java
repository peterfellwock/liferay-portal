/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.file.install.internal.properties;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.framework.BundleContext;

/**
 * @author Matthew Tambara
 */
public class InterpolationUtil {

	public static void performSubstitution(
		Map<String, String> properties, BundleContext bundleContext) {

		performSubstitution(
			properties, new BundleContextSubstitutionCallback(bundleContext));
	}

	public static void performSubstitution(
		Map<String, String> properties,
		SubstitutionalCallback substitutionCallback) {

		Map<String, String> map = new HashMap<>(properties);

		for (Map.Entry<String, String> entry : properties.entrySet()) {
			String name = entry.getKey();

			properties.put(
				name,
				substVars(
					entry.getValue(), name, null, map, substitutionCallback,
					true));
		}
	}

	public static String substVars(
			String value, String currentKey, Map<String, String> cycleMap,
			Map<String, String> configProps, SubstitutionalCallback callback,
			boolean substituteFromConfig)
		throws IllegalArgumentException {

		return _unescape(
			_substVars(
				value, currentKey, cycleMap, configProps, callback,
				substituteFromConfig));
	}

	public static class BundleContextSubstitutionCallback
		implements SubstitutionalCallback {

		public BundleContextSubstitutionCallback(BundleContext context) {
			_bundleContext = context;
		}

		@Override
		public String getValue(String key) {
			String value = null;

			if (key.startsWith(_ENV_PREFIX)) {
				value = System.getenv(key.substring(_ENV_PREFIX.length()));
			}
			else {
				if (_bundleContext != null) {
					value = _bundleContext.getProperty(key);
				}

				if (value == null) {
					value = System.getProperty(key);
				}
			}

			return value;
		}

		private final BundleContext _bundleContext;

	}

	private static int _indexOf(String value, int fromIndex) {
		Matcher escapedOpeningCurlyMatcher = _escapedOpeningCurly.matcher(
			value);

		Matcher escapedClosingCurlyMatcher = _escapedClosingCurly.matcher(
			value);

		int escapedOpeningCurlyMatcherIndex = Integer.MAX_VALUE;

		if (escapedOpeningCurlyMatcher.find(fromIndex)) {
			escapedOpeningCurlyMatcherIndex =
				escapedOpeningCurlyMatcher.start();
		}

		int escapedClosingCurlyMatcherIndex = Integer.MAX_VALUE;

		if (escapedClosingCurlyMatcher.find(fromIndex)) {
			escapedClosingCurlyMatcherIndex =
				escapedClosingCurlyMatcher.start();
		}

		int index = Math.min(
			escapedOpeningCurlyMatcherIndex, escapedClosingCurlyMatcherIndex);

		if (index == Integer.MAX_VALUE) {
			return -1;
		}

		return index;
	}

	private static String _substVars(
			String value, String currentKey, Map<String, String> cycleMap,
			Map<String, String> configProps, SubstitutionalCallback callback,
			boolean substituteFromConfig)
		throws IllegalArgumentException {

		if (cycleMap == null) {
			cycleMap = new HashMap<>();
		}

		// Put the current key in the cycle map

		cycleMap.put(currentKey, currentKey);

		// Assume we have a value that is something like: "leading ${foo.${bar}}
		// middle ${baz} trailing". Find the first ending "}" variable
		// delimiter which will correspond to the first deepest nested variable
		// placeholder.

		int startDelim = value.indexOf(_DELIM_START);
		int stopDelim = value.indexOf(_DELIM_STOP);

		while ((startDelim >= 0) && (stopDelim >= 0)) {
			while ((stopDelim > 0) &&
				   (value.charAt(stopDelim - 1) == _ESCAPE_CHAR)) {

				stopDelim = value.indexOf(_DELIM_STOP, stopDelim + 1);
			}

			// Find the matching starting "${" variable delimiter by looping
			// until we find a start delimiter that is greater than the stop
			// delimiter we have found

			while (stopDelim >= 0) {
				int index = value.indexOf(
					_DELIM_START, startDelim + _DELIM_START.length());

				if ((index < 0) || (index > stopDelim)) {
					break;
				}
				else if (index < stopDelim) {
					startDelim = index;
				}
			}

			if (startDelim < stopDelim) {
				break;
			}

			stopDelim = value.indexOf(_DELIM_STOP, stopDelim + 1);
			startDelim = value.indexOf(_DELIM_START);
		}

		// If we do not have a start or stop delimiter, then just return the
		// existing value

		if ((startDelim < 0) || (stopDelim < 0)) {
			cycleMap.remove(currentKey);

			return value;
		}

		// At this point, we have found a variable placeholder, so we must
		// perform a variable substitution on it. Using the start and stop
		// delimiter indices, extract the first, deepest nested variable
		// placeholder.

		String variable = value.substring(
			startDelim + _DELIM_START.length(), stopDelim);

		String original = variable;

		// Strip expansion modifiers

		int index1 = variable.lastIndexOf(":-");
		int index2 = variable.lastIndexOf(":+");

		int index = -1;

		if ((index1 >= 0) && (index2 >= 0)) {
			index = Math.min(index1, index1);
		}
		else if (index1 >= 0) {
			index = index1;
		}
		else {
			index = index2;
		}

		String op = null;

		if ((index >= 0) && (index < variable.length())) {
			op = variable.substring(index);

			variable = variable.substring(0, index);
		}

		// Verify that this is not a recursive variable reference

		if (cycleMap.get(variable) != null) {
			throw new IllegalArgumentException(
				"recursive variable reference: " + variable);
		}

		String substValue = null;

		// Get the value of the deepest nested variable placeholder. Try the
		// configuration properties first.

		if (substituteFromConfig && (configProps != null)) {
			substValue = configProps.get(variable);
		}

		if ((substValue == null) && (variable.length() > 0)) {
			if (callback != null) {
				substValue = callback.getValue(variable);
			}

			if (substValue == null) {
				substValue = System.getProperty(variable);
			}
		}

		if (op != null) {
			if (op.startsWith(":-")) {
				if ((substValue == null) || (substValue.length() == 0)) {
					substValue = op.substring(":-".length());
				}
			}
			else if (op.startsWith(":+")) {
				if ((substValue != null) && (substValue.length() != 0)) {
					substValue = op.substring(":+".length());
				}
			}
			else {
				throw new IllegalArgumentException(
					"Bad substitution: ${" + original +
						StringPool.CLOSE_CURLY_BRACE);
			}
		}

		if (substValue == null) {
			substValue = "";
		}

		// Remove the found variable from the cycle map since it may appear more
		// than once in the value and we do not want such situations to appear
		// as a recursive reference

		cycleMap.remove(variable);

		// Append the leading characters, the substituted value of the variable,
		// and the trailing characters to get the new value

		value =
			value.substring(0, startDelim) + substValue +
				value.substring(stopDelim + _DELIM_STOP.length());

		// Perform the substitution again since there could still be
		// substitutions to make

		value = _substVars(
			value, currentKey, cycleMap, configProps, callback,
			substituteFromConfig);

		cycleMap.remove(currentKey);

		// Return the value

		return value;
	}

	private static String _unescape(String value) {
		value = value.replaceAll("\\" + _MARKER, "\\$");

		Matcher existingSubstVarMatcher = _existingSubstVar.matcher(value);

		if (!existingSubstVarMatcher.matches()) {
			return value;
		}

		int escape = _indexOf(value, 0);

		while ((escape >= 0) && (escape < (value.length() - 1))) {
			char c = value.charAt(escape + 1);

			if ((c == CharPool.OPEN_CURLY_BRACE) ||
				(c == CharPool.CLOSE_CURLY_BRACE) || (c == _ESCAPE_CHAR)) {

				value =
					value.substring(0, escape) + value.substring(escape + 1);
			}

			escape = _indexOf(value, escape + 1);
		}

		return value;
	}

	private InterpolationUtil() {
	}

	private static final String _DELIM_START = "${";

	private static final String _DELIM_STOP = "}";

	private static final String _ENV_PREFIX = "env:";

	private static final char _ESCAPE_CHAR = '\\';

	private static final String _MARKER = "$__";

	private static final Pattern _escapedClosingCurly = Pattern.compile(
		"\\\\+\\}");
	private static final Pattern _escapedOpeningCurly = Pattern.compile(
		"\\\\+\\{");
	private static final Pattern _existingSubstVar = Pattern.compile(
		".*\\$\\\\*\\{.*\\}.*");

}