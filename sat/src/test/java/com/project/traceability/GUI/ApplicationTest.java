package com.project.traceability.GUI;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.widgets.Shell;
import org.junit.Test;
import scala.Application;

public class ApplicationTest extends IsolatedShellTest {

	@Override
	protected Shell createShell() {
		return null;//new Application().open();
	}

	@Test
	public void oneClick() {
		assertEquals("I am empty.", bot.label(0).getText());
		bot.button("Press me!").click();
		assertEquals("I am changed!", bot.label(0).getText());
	}

	@Test
	public void twoClicks() {
		assertEquals("I am empty.", bot.label(0).getText());
		bot.button("Press me!").click();
		bot.button("Press me!").click();
		assertEquals("I am changed!", bot.label(0).getText());
	}

	@Test
	public void threeClicks() {
		assertEquals("I am empty.", bot.label(0).getText());
		bot.button("Press me!").click();
		bot.button("Press me!").click();
		bot.button("Press me!").click();
		assertEquals("I am changed!", bot.label(0).getText());
	}

	@Test
	public void fourClicks() {
		assertEquals("I am empty.", bot.label(0).getText());
		bot.button("Press me!").click();
		bot.button("Press me!").click();
		bot.button("Press me!").click();
		bot.button("Press me!").click();
		assertEquals("I am changed!", bot.label(0).getText());
	}
}
