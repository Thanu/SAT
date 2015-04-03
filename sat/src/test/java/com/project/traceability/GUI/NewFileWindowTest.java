/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.GUI;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.Test;

import com.project.traceability.common.PropertyFile;

/**
 * 
 * @author Gitanjali
 */
public class NewFileWindowTest extends IsolatedShellTest {

	
	private SWTWorkbenchBot bot = new SWTWorkbenchBot();
	public NewFileWindowTest() {
	}

	@Override
	protected Shell createShell() {
		return new NewFileWindow().open();
	}
	
	@Test
	public void tabItemTest() {
		NewProjectWindow.projectPath = PropertyFile.filePath + "test/";
		System.out.println(bot.activeShell().getText());
		assertEquals("Cancel", bot.button(0).getText());
		assertEquals("Browse", bot.button(1).getText());
		assertEquals("Open", bot.button(2).getText());
	}
}
