/*
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

package com.liferay.portal.search.background.task;

import com.liferay.portal.kernel.backgroundtask.BackgroundTaskStatusMessage;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.SearchContext;

import java.util.Collection;

/**
 * @author Andrew Betts
 */
public class ReindexBackgroundTaskStatusMessage extends BackgroundTaskStatusMessage {

	public ReindexBackgroundTaskStatusMessage(String phase) {
		put("phase", phase);
	}

	public ReindexBackgroundTaskStatusMessage(
		SearchContext searchContext, Document document) {

		put("searchContext", searchContext);
		put("count", document);
	}

	public ReindexBackgroundTaskStatusMessage(
		SearchContext searchContext, Collection<Document> documents) {

		put("searchContext", searchContext);
	}
}
