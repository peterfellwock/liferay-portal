package com.liferay.portal.util;

import java.util.HashMap;
public class ExCatchUtil {

	public static void add(ExCatch exCatch) {
		if (contains(exCatch)) {
			IdExCatch existing = get(exCatch.getKey());

			existing.put(exCatch);
		}else {
			IdExCatch newIdEx = new IdExCatch(exCatch);
			getInstance().catches.put(exCatch.getKey(), newIdEx);
			//System.out.println("SAVING:");
		}

		exCatch.print();
	}

	public static boolean contains(ExCatch exCatch) {
		return getInstance().catches.containsKey(exCatch.getKey());
	}

	public static void discover(Exception e) {
		StackTraceElement[] stacks = e.getStackTrace();

		for (StackTraceElement stack : stacks) {
			String className = stack.getClassName();

			if (className.equals("LayoutSetLocalServiceImpl")) {
				//stack
			}
		}
	}

	public static IdExCatch get(String key) {
		return getInstance().catches.get(key);
	}

	public static ExCatchUtil getInstance() {
		if (instance == null) {
			instance = new ExCatchUtil();
		}

		return instance;
	}

	private ExCatchUtil() {
		catches = new HashMap<>();
	}

	protected static HashMap<String, IdExCatch> catches;
	private static ExCatchUtil instance;

}