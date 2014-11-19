package com.project.traceability.GUI;

import java.awt.Dimension;
import java.util.ArrayList;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.project.traceability.manager.ReadFiles;
import com.project.traceability.manager.RequirementSourceClassManager;
import com.project.traceability.manager.RequirementUMLClassManager;
import com.project.traceability.manager.UMLSourceClassManager;

public class CompareWindow {

	protected Shell shell;public static Dimension screen = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize();
	public static Table table;
	public static TabItem tabItem_1;
	public static TabItem tabItem_2;
	public static TabFolder tabFolder_1;
	public static TabFolder tabFolder_2;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CompareWindow window = new CompareWindow();
			window.open("", new ArrayList<String>());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open(String project, ArrayList<String> selectedFiles) {
		Display display = Display.getDefault();
		createContents(selectedFiles);
		compareFiles(project, selectedFiles);
		shell.open();
		shell.layout();
		center(shell);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
	}
	
	private void compareFiles(String project, ArrayList<String> selectedFiles) {
		String filePath = "D:\\SATWork\\" + project + "\\";
		System.out.println(selectedFiles.size());
		if (selectedFiles.get(0).contains("UML") && selectedFiles.get(1).contains("Source")
				|| selectedFiles.get(0).contains("Source")
				&& selectedFiles.get(1).contains("UML")) {
			ReadFiles.readFiles(filePath);
			UMLSourceClassManager.compareClassNames(filePath);
			//AttributeManager.mapAttributes(filePath);
			
		} else if (selectedFiles.get(0).contains("UML") && selectedFiles.get(1).contains("Requirement")
				|| selectedFiles.get(0).contains("Requirement")
				&& selectedFiles.get(1).contains("UML")) {
			ReadFiles.readFiles(filePath);
			RequirementUMLClassManager.compareClassNames(filePath);
			//RequirementUMLAttributeManager.mapAttributes(filePath);
			
		}  else if (selectedFiles.get(0).contains("Source") && selectedFiles.get(1).contains("Requirement")
				|| selectedFiles.get(0).contains("Requirement")
				&& selectedFiles.get(1).contains("Source")) {
			ReadFiles.readFiles(filePath);
			RequirementSourceClassManager.compareClassNames(filePath);
			//RequirementUMLAttributeManager.mapAttributes(filePath);
			
		}

	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(ArrayList<String> files) {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		shell.setBounds(0, 0, screen.width, screen.height);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.NONE);
		mntmFile.setText("File");
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(0, 0,683, 740);
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Compared Results");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_2);
		
		table = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(40, 31, 515, 621);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(149);
		tblclmnNewColumn.setText("Classes");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(173);
		tblclmnNewColumn_1.setText("Attributes");
		
		TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(243);
		tblclmnNewColumn_2.setText("Methods");
		
		tabFolder_2 = new TabFolder(shell, SWT.NONE);
		tabFolder_2.setBounds(699, 403,	683, 337);
		
		tabItem_2 = new TabItem(tabFolder_2, SWT.NONE);
		tabItem_2.setText(files.get(0));
		
		tabFolder_1 = new TabFolder(shell, SWT.NONE);
		tabFolder_1.setBounds(699, 22, 651, 362);
		
		tabItem_1 = new TabItem(tabFolder_1, SWT.NONE);
		tabItem_1.setText(files.get(1));
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
