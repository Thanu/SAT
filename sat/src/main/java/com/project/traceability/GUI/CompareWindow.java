package com.project.traceability.GUI;

import java.awt.Dimension;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class CompareWindow {

	protected Shell shell;public static Dimension screen = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize();

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CompareWindow window = new CompareWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		center(shell);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		shell.setBounds(0, 0, screen.width, screen.height);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.NONE);
		mntmFile.setText("File");
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(0, 0,(int) (screen.width * 0.5), (int) (screen.height * 0.5));
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("New Item 1");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		
		TabFolder tabFolder_1 = new TabFolder(shell, SWT.NONE);
		tabFolder_1.setBounds((int) (screen.width * 0.5), 0,
				(int) (screen.width * 0.5), (int) (screen.height * 0.5));
		
		TabItem tabItem_1 = new TabItem(tabFolder_1, SWT.NONE);
		tabItem_1.setText("New Item 2");
		
		Composite composite_1 = new Composite(tabFolder_1, SWT.NONE);
		tabItem_1.setControl(composite_1);
		
		TabFolder tabFolder_2 = new TabFolder(shell, SWT.NONE);
		tabFolder_2.setBounds(0, (int) (screen.height * 0.5),	screen.width, (int) (screen.height * 0.5));
		
		TabItem tabItem_2 = new TabItem(tabFolder_2, SWT.NONE);
		tabItem_2.setText("New Item Merge");
		
		Composite composite_2 = new Composite(tabFolder_2, SWT.NONE);
		tabItem_2.setControl(composite_2);

	}
	
	public void center(Shell shell) {

		Rectangle bds = shell.getDisplay().getBounds();

		Point p = shell.getSize();
		shell.setFullScreen(true);
		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;

		shell.setBounds(nLeft, nTop, p.x, p.y);
	}
}
