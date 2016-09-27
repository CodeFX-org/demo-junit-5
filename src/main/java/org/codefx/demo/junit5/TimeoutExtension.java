package org.codefx.demo.junit5;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.TestExtensionContext;

import java.util.Optional;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

class TimeoutExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

	private static final Namespace NAMESPACE = Namespace.create("org", "codefx", "Timeout");
	private static final String LAUNCH_TIME_KEY = "LaunchTime";

	@Override
	public void beforeTestExecution(TestExtensionContext context) {
		storeNowAsLaunchTime(context);
	}

	@Override
	public void afterTestExecution(TestExtensionContext context) {
		annotatedTimeout(context).ifPresent(timeout -> failTestIfRanTooLong(context, timeout));
	}

	private void failTestIfRanTooLong(TestExtensionContext context, Long timeout) {
		long launchTime = loadLaunchTime(context);
		long elapsedTime = currentTimeMillis() - launchTime;

		if (elapsedTime > timeout) {
			String message = format(
					"Test '%s' was supposed to run no longer than %d ms but ran %d ms.",
					context.getDisplayName(), timeout, elapsedTime);
			throw new IllegalStateException(message);
		}
	}

	private Optional<Long> annotatedTimeout(TestExtensionContext context) {
		return findAnnotation(context.getElement(), Test.class)
				.map(Test::timeout)
				.filter(timeout -> timeout != 0L);
	}

	private static void storeNowAsLaunchTime(ExtensionContext context) {
		context.getStore(NAMESPACE).put(LAUNCH_TIME_KEY, currentTimeMillis());
	}

	private static long loadLaunchTime(ExtensionContext context) {
		return context.getStore(NAMESPACE).get(LAUNCH_TIME_KEY, long.class);
	}

}

