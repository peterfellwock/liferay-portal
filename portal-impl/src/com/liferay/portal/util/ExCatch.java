package com.liferay.portal.util;
public class ExCatch {

	public ExCatch(
		Exception e, String id, long mvcc, String className, long threadId) {

		this.e = e;
		this.layoutSetPrototypeId = id;
		this.mvcc = mvcc;
		this.className = className;
		this.threadId = threadId;
		this.time = System.currentTimeMillis();
		this.key = id;
	}

	public String getKey() {
		return layoutSetPrototypeId;
	}

	public long getMVCC() {
		return mvcc;
	}

	public String getStringMVCC() {
		return mvcc + "";
	}

	public void print() {
		System.out.println("{id:" + layoutSetPrototypeId + " mvcc:" + mvcc + " class:" + className + " thread:" + threadId +
			" time:" + time + "}");
	}

	public void printEx() {
		e.printStackTrace(System.out);
	}

	private final String className;
	private final Exception e;
	private final String layoutSetPrototypeId;
	private final String key;
	private final long mvcc;
	private final long threadId;
	private final long time;

}