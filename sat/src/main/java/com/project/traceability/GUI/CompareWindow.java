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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
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
import com.project.traceability.model.ArtefactSubElement;

public class CompareWindow {

	public static Shell shell;
	public static Dimension screen = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize();
	public static TableViewer viewer;
	public static Display display;
	public static Tree tree;
	public static int column;
	public static int alterColumn;

	public static TreeItem[] classList;
	public static TreeItem[] subElements;
	public static TreeItem parent;

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
		ReadFiles.readFiles(PropertyFile.filePath + project + "\\");
		compareFiles(project, selectedFiles);

	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(ArrayList<String> files) {
		final Image image = display.getSystemImage(SWT.ICON_INFORMATION);
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
						column = i;
						if (column == 0)
							alterColumn = 1;
						else if (column == 1)
							alterColumn = 0;
						break;
					}
				}
				Menu men = new Menu(tree);
				tree.setMenu(men);
				final MenuItem newLinkItem = new MenuItem(men, SWT.PUSH);
				newLinkItem.setText("Add a link");

				final TreeEditor editor = new TreeEditor(tree);
				newLinkItem.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						final TreeItem[] selection = tree.getSelection();
						TreeItem[] items = selection[0].getItems();
						boolean showBorder = true;
						final Composite composite = new Composite(tree,
								SWT.NONE);
						final CCombo text = new CCombo(composite, SWT.NONE);

						classList = tree.getItems();

						if (items.length == 0) {
							parent = selection[0].getParentItem();
							subElements = selection[0].getParentItem()
									.getItems();
							classList = null;
						}

						if (classList != null && classList.length > 0) {
							for (int i = 0; i < classList.length; i++) {
								TreeItem item = classList[i];
								System.out.println(classList[i].getText());
								if (column == 0)
									alterColumn = 1;
								if (item.getData(Integer.toString(alterColumn)) != null
										&& item.getText(alterColumn) != "") {
									text.add(((ArtefactElement) item
											.getData(Integer
													.toString(alterColumn)))
											.getName());
									text.setData(((ArtefactElement) item
											.getData(Integer
													.toString(alterColumn)))
											.getName(), item.getData(Integer
											.toString(alterColumn)));
								}
							}
						} else if (subElements != null
								&& subElements.length > 0) {
							for (int i = 0; i < subElements.length; i++) {
								TreeItem item = subElements[i];
								System.out.println(subElements[i].getText());
								if (column == 0)
									alterColumn = 1;
								if (item.getData(Integer.toString(alterColumn)) != null
										&& item.getText(alterColumn) != "") {
									text.add(((ArtefactSubElement) item
											.getData(Integer
													.toString(alterColumn)))
											.getName());
									text.setData(((ArtefactSubElement) item
											.getData(Integer
													.toString(alterColumn)))
											.getName(), item.getData(Integer
											.toString(alterColumn)));
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
									System.out.println(text.getData(text
											.getText()));
									if (classList != null) {
										if (confirmMapping(
												((ArtefactElement) selection[0]
														.getData("" + column
																+ "")),
												(ArtefactElement) text
														.getData(text.getText()))) {
											String className = updateSubElements(
													tree.getColumn(0).getText(),
													tree.getColumn(1).getText());
											if (className.equals("RS"))
												RequirementSourceClassManager
														.compareSubElements(
																selection[0],
																(ArtefactElement) selection[0]
																		.getData(""
																				+ column
																				+ ""),
																(ArtefactElement) text
																		.getData(text
																				.getText()));
											else if (className.equals("RU"))
												RequirementUMLClassManager
														.compareSubElements(
																selection[0],
																(ArtefactElement) selection[0]
																		.getData(""
																				+ column
																				+ ""),
																(ArtefactElement) text
																		.getData(text
																				.getText()));
											else if (className.equals("US"))
												UMLSourceClassManager
														.compareSubElements(
																selection[0],
																(ArtefactElement) selection[0]
																		.getData(""
																				+ column
																				+ ""),
																(ArtefactElement) text
																		.getData(text
																				.getText()));
											for (int i = 0; i < classList.length; i++) {
												if (classList[i].getText(
														alterColumn)
														.equalsIgnoreCase(
																text.getText())) {
													selection[0].dispose();
													classList[i].dispose();
													break;
												}
											}
										}
									} else if (subElements != null) {
										if (confirmMapping(
												((ArtefactSubElement) selection[0]
														.getData("" + column
																+ "")),
												(ArtefactSubElement) text
														.getData(text.getText()))) {
											for (int i = 0; i < subElements.length; i++) {
												if (subElements[i].getText(
														alterColumn)
														.equalsIgnoreCase(
																text.getText())) {
													TreeItem newItem = new TreeItem(
															parent, SWT.NONE);
													newItem.setText(0,
															selection[0]
																	.getText(0));
													newItem.setText(1,
															subElements[i]
																	.getText(1));
													selection[0].dispose();
													subElements[i].dispose();
													break;
												}
											}
										}
									}
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
										newLinkItem.setText(text.getText());
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
						text.setText(newLinkItem.getText());
						// text.deselectAll ();
						text.setFocus();
					}
				});

				final MenuItem deleteLinkItem = new MenuItem(men, SWT.PUSH);
				deleteLinkItem.setText("Delete this link");
				deleteLinkItem.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						final TreeItem[] selection = tree.getSelection();
						EditManager.deleteLink(selection[0]);
					}
				});

			}
		});

		/*Listener paintListener = new Listener() {
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.MeasureItem: {
					Rectangle rect = image.getBounds();
					event.width += rect.width;
					event.height += Math.max(event.height, rect.height + 2);
					break;
				}
				case SWT.PaintItem: {
					int x = event.x + event.width;
					Rectangle rect = image.getBounds();
					int offset = Math.max(0, (event.height - rect.height) / 2);
					event.gc.drawImage(image, x, event.y + offset);
					break;
				}
				}
			}
		};
		tree.addListener(SWT.MeasureItem, paintListener);
		tree.addListener(SWT.PaintItem, paintListener);*/
	}

	private void compareFiles(String project, ArrayList<String> selectedFiles) {
		String filePath = PropertyFile.filePath + project + "\\";
		// ReadFiles.readFiles(filePath);
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

	public static boolean confirmMapping(Object className, Object mapClass) {
		boolean confirmed = false;
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);

		messageBox.setMessage("Do you really want to map " + className + " to "
				+ mapClass + " ?");
		messageBox.setText("Confirmation");
		int response = messageBox.open();
		if (response == SWT.YES) {
			EditManager.addLink(className, mapClass);
			confirmed = true;
		}
		return confirmed;
	}

	public static String updateSubElements(String fileName1, String fileName2) {
		String className = "";
		if (fileName1.contains("UML") && fileName2.contains("Source")
				|| fileName1.contains("Source") && fileName2.contains("UML")) {
			className = "US";

		} else if (fileName1.contains("UML")
				&& fileName2.contains("Requirement")
				|| fileName1.contains("Requirement")
				&& fileName2.contains("UML")) {
			className = "RU";

		} else if (fileName1.contains("Source")
				&& fileName2.contains("Requirement")
				|| fileName1.contains("Requirement")
				&& fileName2.contains("Source")) {
			className = "RS";
		}
		return className;
	}
}
