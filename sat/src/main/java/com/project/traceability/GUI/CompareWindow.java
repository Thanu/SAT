/**
 * 
 */
package com.project.traceability.GUI;

/**
 * @author Gitanjali
 * Dec 4, 2014
 */

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.ReadFiles;
import com.project.traceability.manager.RequirementSourceClassManager;
import com.project.traceability.manager.RequirementUMLClassManager;
import com.project.traceability.manager.UMLSourceClassManager;


public class CompareWindow {

	public static Shell shell;
	public static Dimension screen = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize();
	public static TableViewer viewer;
	public static Display display;
	public static Tree tree; 

	/**
	 * Launch the application.
	 * 
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
		
		display = Display.getDefault();
		createContents(selectedFiles);	
		compareFiles(project, selectedFiles);
		
	}
	

	/**
	 * Create contents of the window.
	 */
	protected void createContents(ArrayList<String> files) {
		shell = new Shell();
		shell.setSize(900, 900);
		shell.setText("SWT Application");

		shell.setBounds(0, 0, screen.width, screen.height);

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.NONE);
		mntmFile.setText("File");	
		//CTabFolder tab = new CTabFolder(shell, SWT.NONE);
	//	tab.setBounds(0, 65, 461, 213);
		CTabItem tabItem = new CTabItem(HomeGUI.tabFolder, SWT.NONE);
		tabItem.setText("Compared Results");
		
		Composite composite = new Composite(HomeGUI.tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		tabItem.setControl(composite);
		
		composite.setBounds(40, 31, 900, 627);
		System.out.println(composite.getSize());
		
		tree = new Tree(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL |SWT.FULL_SELECTION);
		GridData gd_tree = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_tree.heightHint = 600;
		tree.setLayoutData(gd_tree);
		tree.setHeaderVisible(true);
		
		Menu men = new Menu(composite);
		tree.setMenu(men);
		final MenuItem item = new MenuItem(men, SWT.PUSH);
		item.setText("Delete Selection");
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("{{{{{{{{{{{");
				 Point pt = new Point(event.x, event.y); 
				 int column = -1;
				 TreeItem treeItem = tree.getItem(pt); 
				 for (int i = 0, n = tree.getColumnCount(); i < n; i++) { 
					 Rectangle rect = treeItem.getBounds(i);
					 System.out.println(treeItem.getText());
					 if (rect.contains(pt)) { 
						 column = i;
						 System.out.println(column + "PPPPPPPPPP");
						 System.out.println(treeItem.getText());
					 } 
				}
				 
				
			}
		});
		
		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		column1.setText(files.get(0));
		column1.setWidth(300);
		
		TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
		column2.setText(files.get(1));
		column2.setWidth(300);
			
	}
	
	private void compareFiles(String project, ArrayList<String> selectedFiles) {
		String filePath = PropertyFile.filePath + project + "\\";
		ReadFiles.readFiles(filePath);
		if (selectedFiles.get(0).contains("UML")
				&& selectedFiles.get(1).contains("Source")
				|| selectedFiles.get(0).contains("Source")
				&& selectedFiles.get(1).contains("UML")) {
			UMLSourceClassManager.compareClassNames(filePath);
			
		} else if (selectedFiles.get(0).contains("UML")
				&& selectedFiles.get(1).contains("Requirement")
				|| selectedFiles.get(0).contains("Requirement")
				&& selectedFiles.get(1).contains("UML")) {
			RequirementUMLClassManager.compareClassNames(filePath);
			
		} else if (selectedFiles.get(0).contains("Source")
				&& selectedFiles.get(1).contains("Requirement")
				|| selectedFiles.get(0).contains("Requirement")
				&& selectedFiles.get(1).contains("Source")) {
			RequirementSourceClassManager.compareClassNames(filePath);
		}
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
