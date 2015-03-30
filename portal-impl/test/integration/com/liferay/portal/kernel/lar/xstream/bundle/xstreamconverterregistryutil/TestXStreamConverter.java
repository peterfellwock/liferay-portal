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

package com.liferay.portal.kernel.lar.xstream.bundle.xstreamconverterregistryutil;

import com.liferay.portal.kernel.lar.xstream.XStreamConverter;
import com.liferay.portal.kernel.lar.xstream.XStreamHierarchicalStreamReader;
import com.liferay.portal.kernel.lar.xstream.XStreamHierarchicalStreamWriter;
import com.liferay.portal.kernel.lar.xstream.XStreamMarshallingContext;
import com.liferay.portal.kernel.lar.xstream.XStreamUnmarshallingContext;
import com.liferay.portal.kernel.util.StackTraceUtil;

import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true,
	property = {"service.ranking:Integer=" + Integer.MAX_VALUE}
)
public class TestXStreamConverter implements XStreamConverter {

	@Override
	public boolean canConvert(Class<?> clazz) {
		return false;
	}

	@Override
	public void marshal(
		Object object,
		XStreamHierarchicalStreamWriter xStreamHierarchicalStreamWriter,
		XStreamMarshallingContext xStreamMarshallingContext) {

		_atomicReference.set(StackTraceUtil.getCallerKey());
	}

	@Override
	public Object unmarshal(
		XStreamHierarchicalStreamReader xStreamHierarchicalStreamReader,
		XStreamUnmarshallingContext xStreamUnmarshallingContext) {

		return null;
	}

	@Reference(target = "(test=AtomicState)")
	protected void getAtomicReference(AtomicReference<String> atomicReference) {
		_atomicReference = atomicReference;
	}

	private AtomicReference<String> _atomicReference;

}