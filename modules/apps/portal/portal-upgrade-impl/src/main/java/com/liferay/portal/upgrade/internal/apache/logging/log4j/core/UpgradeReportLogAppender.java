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

package com.liferay.portal.upgrade.internal.apache.logging.log4j.core;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.upgrade.internal.report.UpgradeReport;

import java.io.Serializable;

import java.util.Objects;

import org.apache.felix.cm.PersistenceManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sam Ziemer
 */
@Component(
	immediate = true, property = "appender.name=UpgradeReportLogAppender",
	service = Appender.class
)
public class UpgradeReportLogAppender implements Appender {

	@Override
	public void append(LogEvent logEvent) {
		Message message = logEvent.getMessage();

		if (logEvent.getLevel() == Level.ERROR) {
			_upgradeReport.addErrorMessage(
				logEvent.getLoggerName(), message.getFormattedMessage());
		}
		else if (logEvent.getLevel() == Level.INFO) {
			if (Objects.equals(
					logEvent.getLoggerName(), UpgradeProcess.class.getName())) {

				_upgradeReport.addEventMessage(
					logEvent.getLoggerName(), message.getFormattedMessage());
			}
		}
		else if (logEvent.getLevel() == Level.WARN) {
			_upgradeReport.addWarningMessage(
				logEvent.getLoggerName(), message.getFormattedMessage());
		}
	}

	@Override
	public ErrorHandler getHandler() {
		return null;
	}

	@Override
	public Layout<? extends Serializable> getLayout() {
		return null;
	}

	@Override
	public String getName() {
		return "UpgradeReportLogAppender";
	}

	@Override
	public State getState() {
		return null;
	}

	@Override
	public boolean ignoreExceptions() {
		return false;
	}

	@Override
	public void initialize() {
	}

	@Override
	public boolean isStarted() {
		return _started;
	}

	@Override
	public boolean isStopped() {
		return !_started;
	}

	@Override
	public void setHandler(ErrorHandler handler) {
	}

	@Override
	public void start() {
		_started = true;

		_upgradeReport = new UpgradeReport(_persistenceManager);

		_rootLogger.addAppender(this);
	}

	@Override
	public void stop() {
		if (_started) {
			_upgradeReport.generateReport();

			_upgradeReport = null;
		}

		_started = false;
	}

	private static final Logger _rootLogger =
		(Logger)LogManager.getRootLogger();

	@Reference
	private PersistenceManager _persistenceManager;

	private volatile boolean _started;
	private volatile UpgradeReport _upgradeReport;

}