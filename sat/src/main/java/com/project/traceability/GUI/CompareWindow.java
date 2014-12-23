/**
 * 
 */
package com.project.traceability.GUI;

/**
 * @author Gitanjali
 * Dec 4, 2014
 */

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import org.apache.xpath.operations.Variable;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.graphics.GC;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.EditManager;
import com.project.traceability.manager.ReadFiles;
import com.project.traceability.manager.RequirementSourceClassManager;
import com.project.traceability.manager.RequirementUMLClassManager;
import com.project.traceability.manager.UMLSourceClassManager;
import com.project.traceability.model.ArtefactElement;

public class CompareWindow {

	public static Shell shell;
	public static Dimension screen = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize();
	public static TableViewer viewer;
	public static Display display;
	public static Tree tree;
	public static int column;
	public static int alterColumn;

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

		CTabItem tabItem = new CTabItem(HomeGUI.tabFolder, SWT.NONE);
		tabItem.setText("Compared Results");

		Composite composite = new Composite(HomeGUI.tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		tabItem.setControl(composite);

		composite.setBounds(40, 31, 900, 627);
		System.out.println(composite.getSize());

		tree = new Tree(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		GridData gd_tree = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_tree.heightHint = 600;
		tree.setLayoutData(gd_tree);
		tree.setHeaderVisible(true);

		tree.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event e) {
				Point pt = new Point(e.x, e.y);
				TreeItem treeItem = tree.getItem(pt);
				column = 0;

				if (treeItem == null) {
					return;
				}
				for (int i = 0; i < tree.getColumnCount(); i++) {
					Rectangle rect = treeItem.getBounds(i);
					if (rect.contains(pt)) {
						int index = tree.indexOf(treeItem);
						System.out.println("Item " + index + "-" + i);
						column = i;
						if(column == 0)
							alterColumn = 1;
						else if(column == 1)
							alterColumn = 0;
						break;
					}
				}
				Menu men = new Menu(tree);
				tree.setMenu(men);
				final MenuItem item = new MenuItem(men, SWT.PUSH);
				item.setText("Add a link");
				final TreeEditor editor = new TreeEditor(tree);
				item.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						final TreeItem[] selection = tree.getSelection();
						boolean showBorder = true;
						final Composite composite = new Composite(tree,
								SWT.NONE);
						final CCombo text = new CCombo(composite, SWT.NONE);

						TreeItem[] classList = tree.getItems();
						if (classList != null && classList.length > 0) {
							for (int i = 0; i < classList.length; i++) {
								TreeItem item = classList[i];
								System.out.println(classList[i].getText());	
								if(column == 0)
									alterColumn = 1;
								//else
								if (item.getData(Integer.toString(alterColumn)) != null
										&& item.getText(alterColumn) != "") {
									text.add(((ArtefactElement) item.getData(Integer.toString(alterColumn))).getName());
									text.setData(((ArtefactElement) item.getData(Integer.toString(alterColumn))).getName(),
											item.getData(Integer.toString(alterColumn)));
								}
							}
						}

						editor.grabHorizontal = true;
						editor.setEditor(text, selection[0], column);
						final int inset = showBorder ? 1 : 0;
						composite.addListener(SWT.Resize, new Listener() {
							@Override
							public void handleEvent(Event e) {
								Rectangle rect = composite.getClientArea();
								text.setBounds(rect.x + inset, rect.y + inset,
										rect.width - inset * 2, rect.height
												- inset * 2);
							}
						});
						Listener textListener = new Listener() {
							@Override
							public void handleEvent(final Event e) {
								switch (e.type) {
								case SWT.FocusOut:
									confirmMapping(
											((ArtefactElement) selection[0]
													.getData("0")),
											(ArtefactElement) text.getData(text
													.getText()));
									composite.dispose();
									break;
								case SWT.Verify:
									String newText = text.getText();
									String leftText = newText.substring(0,
											e.start);
									String rightText = newText.substring(e.end,
											newText.length());
									GC gc = new GC(text);
									Point size = gc.textExtent(leftText
											+ e.text + rightText);
									gc.dispose();
									size = text
											.computeSize(size.x, SWT.DEFAULT);
									editor.horizontalAlignment = SWT.LEFT;
									Rectangle itemRect = selection[0]
											.getBounds(),
									rect = tree.getClientArea();
									editor.minimumWidth = Math.max(size.x,
											itemRect.width) + inset * 2;
									int left = itemRect.x,
									right = rect.x + rect.width;
									editor.minimumWidth = Math.min(
											editor.minimumWidth, right - left);
									editor.minimumHeight = size.y + inset * 2;
									editor.layout();
									break;
								case SWT.Traverse:
									switch (e.detail) {
									case SWT.TRAVERSE_RETURN:
										item.setText(text.getText());
										// FALL THROUGH
									case SWT.TRAVERSE_ESCAPE:
										composite.dispose();
										e.doit = false;
									}
									break;
								}
							}
						};
						text.addListener(SWT.FocusOut, textListener);
						text.addListener(SWT.Traverse, textListener);
						text.addListener(SWT.Verify, textListener);
						editor.setEditor(composite, selection[0]);
						text.setText(item.getText());
						// text.deselectAll ();
						text.setFocus();

						/*
						 * System.out.println(selection[0].getParentItem().
						 * getParentItem( ) .getText(0) + " " +
						 * selection[0].getParentItem().getParentItem()
						 * .getText(1)); System.out.println(string);
						 * EditManager.addLink(string,
						 * selection[0].getParentItem
						 * ().getParentItem().getText(0) + " " +
						 * selection[0].getParentItem().getParentItem()
						 * .getText(1));
						 */
					}
				});
			}
		});

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

	public static void confirmMapping(ArtefactElement className,
			ArtefactElement mapClass) {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);

		messageBox.setMessage("Do you really want to map " + className + " to "
				+ mapClass + " ?");
		messageBox.setText("Confirmation");
		int response = messageBox.open();
		if (response == SWT.YES) {
			EditManager.addLink(className, mapClass);
		}
	}
}
