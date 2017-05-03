package org.codefx.demo.junit5.extensions;

import org.codefx.demo.junit5.DisabledOnOs;
import org.codefx.demo.junit5.OS;
import org.codefx.demo.junit5.TestExceptOnOs;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DisabledOnOsTest {

	@Test
	void runsOnAllOS() {
		assertTrue(true);
	}

	@Test
	@DisabledOnOs(OS.NIX)
	void doesNotRunOnLinux_testFails() {
		assertTrue(false);
	}

	@Test
	@DisabledOnOs(OS.WINDOWS)
	void doesNotRunOnWindows_testFails() {
		assertTrue(false);
	}

	@TestExceptOnOs(OS.NIX)
	void doesNotRunOnLinuxEither_testFails() {
		assertTrue(false);
	}

	@TestExceptOnOs(OS.WINDOWS)
	void doesNotRunOnWindowsEither_testFails() {
		assertTrue(false);
	}

}
