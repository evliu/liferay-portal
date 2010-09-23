/**
 * Copyright (c) 2000-2010 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.servlet.filters.threaddump;

import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.portal.util.PropsValues;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 */
public class ThreadDumpFilter extends BasePortalFilter {

	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		ThreadDumper threadDumper = new ThreadDumper();

		ScheduledFuture<?> scheduledFuture =
			_scheduledExecutorService.schedule(
				threadDumper, PropsValues.THREAD_DUMP_SPEED_THRESHOLD,
				TimeUnit.SECONDS);

		try {
			processFilter(
				ThreadDumpFilter.class, request, response, filterChain);
		}
		finally {
			scheduledFuture.cancel(true);
		}

		if (!threadDumper.isExecuted()) {
			Runtime runtime = Runtime.getRuntime();

			long freeMemory = runtime.freeMemory();
			long maxMemory = runtime.maxMemory();

			long usedMemory = maxMemory - freeMemory;

			double usedMemoryPercent = (usedMemory / (double)maxMemory) * 100;

			if (usedMemoryPercent > PropsValues.THREAD_DUMP_MEMORY_THRESHOLD) {
				threadDumper.run();
			}
		}
	}

	private static int _MAX_THREAD_DUMPERS = 5;

	private static ScheduledExecutorService _scheduledExecutorService =
		Executors.newScheduledThreadPool(_MAX_THREAD_DUMPERS);

}