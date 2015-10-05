package com.liferay.portal.kernel.search.background.task;

import com.liferay.portal.kernel.search.background.task.ReindexStatusMessageSender;
import com.liferay.portal.kernel.search.background.task.ReindexStatusMessageSenderUtil;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

public class ReindexStatusMessageSenderUtil3 {

	public static void hello(){
		System.out.println("HEL3333333333333333-------------------------------------");
	}
	
	public void afterPropertiesSet() throws Exception {
		System.out.println("I HAVE BEEN BEANED33333333333-------------------------------------");
		System.out.println("I HAVE BEEN BEANED-3333333------------------------------------");
		System.out.println("I HAVE BEEN BEANED---3333333----------------------------------");
		System.out.println("I HAVE BEEN BEANED----333333333---------------------------------");
		System.out.println("I HAVE BEEN BEANED------32-------------------------------");
	}
	
	public static ReindexStatusMessageSender
		getReindexStatusMessageSender() {
	
		PortalRuntimePermission.checkGetBeanProperty(
			ReindexStatusMessageSenderUtil.class);
	
		return _reindexStatusMessageSender;
	}
	
	public static void sendStatusMessage(String message) {
		System.out.println("HI-33333333------------------------------------" + message);
		getReindexStatusMessageSender().sendStatusMessage(message);
	}
	
	public void setReindexStatusMessageSender(
		ReindexStatusMessageSender reindexStatusMessageSender) {
	
		PortalRuntimePermission.checkSetBeanProperty(getClass());
	
		_reindexStatusMessageSender = reindexStatusMessageSender;
	}
	
	private static ReindexStatusMessageSender
		_reindexStatusMessageSender;
	
}