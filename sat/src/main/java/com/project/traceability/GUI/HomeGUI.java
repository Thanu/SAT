package com.project.traceability.GUI;

/**
 * @author Gitanjali Nov 12, 2014
 */
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.db.GraphDB;
import com.project.traceability.db.GraphDB.RelTypes;
import com.project.traceability.manager.ReadXML;
import com.project.traceability.manager.SourceCodeArtefactManager;
import com.project.traceability.manager.UMLArtefactManager;

public class HomeGUI {

	public static Dimension screen = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize();

	public static Shell shell;
	public static CTabFolder tabFolder;
	public static CTabFolder graphTab;
	public static CTabFolder newTab;
	public static Tree tree;
	public static Composite composite;
	public static Composite graphComposite;
	public static Composite projComposite;

	public static boolean isComaparing = false;
	public static String projectPath = null;
	public static TreeItem trtmNewTreeitem;

	public static CTabItem graphtabItem;

	private SashForm sidebarSF;
	public SashForm workSF;
	private CTabFolder2Listener ctfCTF2L;
	private MouseListener ctfML;

	static Vector nodes = new Vector();
	static TreeViewer treeViewer;
	
	static String string = "";

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			HomeGUI window = new HomeGUI();
			window.open();
			window.eventLoop(Display.getDefault());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 * 
	 * @return
	 */
	public Shell open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();

		return shell;
	}

	public void eventLoop(Display display) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void initUI() {

		Label label = new Label(shell, SWT.LEFT);

		Point p = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		label.setBounds(5, 5, p.x + 5, p.y + 5);
	}

	public void center(Shell shell) {

		Rectangle bds = shell.getDisplay().getBounds();

		Point p = shell.getSize();
		shell.setFullScreen(true);
		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;

		shell.setBounds(nLeft, nTop, p.x, p.y);
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setBounds(0, 0, screen.width, screen.height - 20);
		center(shell);
		shell.setLayout(new FillLayout());		

		sidebarSF = new SashForm(shell, SWT.HORIZONTAL | SWT.SMOOTH);
		
		newTab = new CTabFolder(sidebarSF, SWT.BORDER | SWT.CLOSE);
		newTab.setData("Project");
		newTab.setSize(400, 900);
		newTab.setMinimizeVisible(true);
		newTab.setMaximizeVisible(true);

		projComposite = new Composite(newTab, 0);
		projComposite.setData(newTab);
		projComposite.setLayout(new FillLayout());

		workSF = new SashForm(sidebarSF, SWT.VERTICAL | SWT.SMOOTH);
		sidebarSF.setWeights(new int[] { 1, 3 });

		tabFolder = new CTabFolder(workSF, SWT.BORDER | SWT.CLOSE);
		tabFolder.setData("WorkSpace");
		tabFolder.setMinimizeVisible(true);
		tabFolder.setMaximizeVisible(true);

		graphTab = new CTabFolder(workSF, SWT.BORDER | SWT.NONE);
		graphTab.setData("Graph");
		graphTab.setMinimizeVisible(true);
		graphTab.setMaximizeVisible(true);
		
		workSF.setWeights(new int[] { 1, 1 });

		composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout());

		graphComposite = new Composite(graphTab, SWT.NONE);
		graphComposite.setLayout(new FillLayout());

		composite.pack();
		graphComposite.pack();
		projComposite.pack();
		
		defineListeners();

		newTab.pack();
		newTab.addCTabFolder2Listener(ctfCTF2L);
		newTab.addMouseListener(ctfML);

		tabFolder.pack();
		tabFolder.addCTabFolder2Listener(ctfCTF2L);
		tabFolder.addMouseListener(ctfML);

		graphTab.pack();
		graphTab.addCTabFolder2Listener(ctfCTF2L);
		graphTab.addMouseListener(ctfML);


		graphtabItem = new CTabItem(graphTab, SWT.NONE);
		graphtabItem.setText("Graph");

		File projectFile = new File(PropertyFile.filePath);
		projectFile.mkdir();
		ArrayList<String> projectFiles = new ArrayList<String>(
				Arrays.asList(projectFile.list()));
		if (projectFiles.isEmpty())
			newTab.setVisible(false);

		CTabItem tbtmProjects = new CTabItem(newTab, SWT.NONE);
		tbtmProjects.setText("Projects");

		tree = new Tree(newTab, SWT.BORDER);
		//treeViewer = new TreeViewer(tree);

		//treeViewer.setContentProvider(new MyTreeContentProvider());

		//treeViewer.setLabelProvider(new MyLabelProvider());

		tbtmProjects.setControl(projComposite);

		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++) {
					string += selection[i] + " ";
					trtmNewTreeitem = selection[i];
				}
				string = string.substring(10, string.length() - 2);
				projectPath = PropertyFile.filePath + string + "/";
				addPopUpMenu();
			}

		});

		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				String string = "";
				String parent = null;
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++) {
					string += selection[i] + " ";
					parent += selection[i].getParentItem() + " ";
					trtmNewTreeitem = selection[i];
				}

				if(!selection[0].getParent().equals(tree)) {
					string = string.substring(10, string.length() - 2);
					parent = parent.substring(14, parent.length() - 2);
					NewFileWindow.localFilePath = PropertyFile.filePath + parent
							+ "/";
					NewFileWindow.createTabLayout(string);
				} else if(selection[0].getParent().equals(tree)) {
					/*TreeItem[] items = trtmNewTreeitem.getItems();
					trtmNewTreeitem.removeAll();
					System.out.println(items[0].getText());
					for(int i = 0; i < items.length; i++){
						TreeItem treeItem = new TreeItem(items[i], SWT.NONE);
						treeItem.setText(items[i].getText());
					}
					//trtmNewTreeitem.
					trtmNewTreeitem.setItemCount(3);*/
					trtmNewTreeitem.setExpanded(true);
				}
			}
		});

		tbtmProjects.setControl(tree);

		if (projectFiles.isEmpty())
			tree.setVisible(false);
		else {
			for (int i = 0; i < projectFiles.size(); i++) {
				//Node trtmNewTreeitem = new Node(projectFiles.get(i), null);
				//nodes.add(trtmNewTreeitem);
				TreeItem trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
				trtmNewTreeitem.setText(projectFiles.get(i));
				File file = new File(PropertyFile.filePath
						+ projectFiles.get(i) + "/");
				ArrayList<String> files = new ArrayList<String>(
						Arrays.asList(file.list()));
				String path = PropertyFile.filePath + projectFiles.get(i)
						+ "\\";
				for (int j = 0; j < files.size(); j++) {
					
					if (files.get(j).contains(".xml") && !files.get(j).equalsIgnoreCase("Relations.xml")) {
						// trtmNewTreeitem = new Node(files.get(j),
						// trtmNewTreeitem);
						//new Node(files.get(j), trtmNewTreeitem);

						 TreeItem fileTreeItem = new TreeItem(trtmNewTreeitem, SWT.NONE);
						 fileTreeItem.setText(files.get(j));
						 
						if (files.get(j).contains("UML"))
							UMLArtefactManager.readXML(path);
						else if (files.get(j).contains("Source"))
							SourceCodeArtefactManager.readXML(path);
					}
					// else if(files.get(i).contains("Requirement"))
					// RequirementsManger.readXML(path);
				}
			}
			// addPopUpMenu();

		}

		//treeViewer.setInput(nodes);

		shell.setText("Software Artefact Traceability Analyzer");

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("File");

		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);

		MenuItem mntmNew = new MenuItem(menu_1, SWT.CASCADE);
		mntmNew.setText("New");

		Menu menu_4 = new Menu(mntmNew);
		mntmNew.setMenu(menu_4);

		MenuItem mntmProject = new MenuItem(menu_4, SWT.NONE);
		mntmProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewProjectWindow newProjWin = new NewProjectWindow();
				newProjWin.open();
			}
		});
		mntmProject.setText("Project");

		MenuItem mntmFile = new MenuItem(menu_4, SWT.NONE);
		mntmFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewFileWindow newFileWin = new NewFileWindow();
				newFileWin.open();
			}
		});
		mntmFile.setText("File");

		MenuItem mntmSave = new MenuItem(menu_1, SWT.NONE);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
				String localFilePath = fileDialog.open();
				if (localFilePath != null) {
					// return pull(sync,localFilePath,remoteFilePath);
				}
			}
		});
		mntmSave.setText("Save");

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		mntmExit.setText("Exit");

		MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
		mntmView.setText("View");

		Menu menu_2 = new Menu(mntmView);
		mntmView.setMenu(menu_2);

		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");

		Menu menu_3 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_3);

	}

	

	public static void addPopUpMenu() {
		Menu popupMenu = new Menu(tree);
		MenuItem newItem = new MenuItem(popupMenu, SWT.CASCADE);
		newItem.setText("New");

		MenuItem graphItem = new MenuItem(popupMenu, SWT.CASCADE);
		graphItem.setText("Visualization");

		MenuItem refreshItem = new MenuItem(popupMenu, SWT.NONE);
		refreshItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				composite.pack(true);
			}
		});
		refreshItem.setText("Refresh");

		MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
		deleteItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteFiles(projectPath);
			}
		});
		deleteItem.setText("Delete");

		MenuItem compareItem = new MenuItem(popupMenu, SWT.NONE);
		compareItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final FileSelectionWindow window = new FileSelectionWindow();
				
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++) {
					string += selection[i] + " ";
				}
				string = string.substring(10, string.length() - 2);
				window.open(string);
				
			}
		});
		compareItem.setText("Compare Files");

		Menu newMenu = new Menu(popupMenu);
		newItem.setMenu(newMenu);

		MenuItem fileItem = new MenuItem(newMenu, SWT.NONE);
		fileItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				File file = new File(projectPath);
				if (!file.isDirectory()) {
					TreeItem parent = trtmNewTreeitem.getParentItem();
					trtmNewTreeitem = trtmNewTreeitem.getParentItem();
					projectPath = PropertyFile.filePath + parent.getText();
				}
				System.out.println(trtmNewTreeitem + projectPath);
				NewFileWindow newFileWin = new NewFileWindow();
				newFileWin.open(trtmNewTreeitem, projectPath);
			}
		});
		fileItem.setText("File");

		Menu visualMenu = new Menu(popupMenu);
		graphItem.setMenu(visualMenu);

		final MenuItem allItem = new MenuItem(visualMenu, SWT.NONE);
		allItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String projectName = trtmNewTreeitem.getText();
				PropertyFile.setProjectName(projectName);
				PropertyFile.setGraphDbPath(projectPath + projectName
						+ ".graphdb");
				PropertyFile.setGeneratedGexfFilePath(projectPath + projectName
						+ ".gexf");
				PropertyFile.setRelationshipXMLPath(projectPath
						+ "Relations.xml");
				PropertyFile.setGraphType(allItem.getText());
				System.out.println("Path: " + projectPath);
				System.out.println("DB Path: " + PropertyFile.graphDbPath);
				System.out.println("Graph Type: " + PropertyFile.graphType);
				ReadXML.initApp(projectPath, allItem.getText());
			}
		});
		allItem.setText("Full Graph");

		final MenuItem edgeItem = new MenuItem(visualMenu, SWT.CASCADE);
		edgeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String projectName = trtmNewTreeitem.getText();
				PropertyFile.setProjectName(projectName);
				PropertyFile.setGraphDbPath(projectPath + projectName
						+ ".graphdb");
				PropertyFile.setGeneratedGexfFilePath(projectPath + projectName
						+ ".gexf");
				PropertyFile.setRelationshipXMLPath(projectPath
						+ "Relations.xml");
				PropertyFile.setGraphType(edgeItem.getText());
				System.out.println("Path: " + projectPath);
				System.out.println("DB Path: " + PropertyFile.graphDbPath);
				System.out.println("Graph Type: " + PropertyFile.graphType);
				ReadXML.initApp(projectPath, edgeItem.getText());
			}
		});
		edgeItem.setText("Edge Filtered");

		Menu edgeMenu = new Menu(popupMenu);
		edgeItem.setMenu(edgeMenu);
		List<MenuItem> menuItem = new ArrayList<MenuItem>();
		int i = 0;
		for (RelTypes type : GraphDB.RelTypes.values()) {
			menuItem.add(new MenuItem(edgeMenu, SWT.NONE));
			final MenuItem item = menuItem.get(i);

			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String projectName = trtmNewTreeitem.getText();
					PropertyFile.setProjectName(projectName);
					PropertyFile.setGraphDbPath(projectPath + projectName
							+ ".graphdb");
					PropertyFile.setGeneratedGexfFilePath(projectPath
							+ projectName + ".gexf");
					PropertyFile.setRelationshipXMLPath(projectPath
							+ "Relations.xml");
					PropertyFile.setGraphType(item.getText());
					System.out.println("Path: " + projectPath);
					System.out.println("DB Path: " + PropertyFile.graphDbPath);
					System.out.println("Graph Type: " + PropertyFile.graphType);
					ReadXML.initApp(projectPath, item.getText());
				}
			});
			menuItem.get(i).setText(type.getValue());
			i++;
		}

		final MenuItem nodeItem = new MenuItem(visualMenu, SWT.CASCADE);
		nodeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String projectName = trtmNewTreeitem.getText();
				PropertyFile.setProjectName(projectName);
				PropertyFile.setGraphDbPath(projectPath + projectName
						+ ".graphdb");
				PropertyFile.setGeneratedGexfFilePath(projectPath + projectName
						+ ".gexf");
				PropertyFile.setRelationshipXMLPath(projectPath
						+ "Relations.xml");
				PropertyFile.setGraphType(nodeItem.getText());
				System.out.println("Path: " + projectPath);
				System.out.println("DB Path: " + PropertyFile.graphDbPath);
				System.out.println("Graph Type: " + PropertyFile.graphType);
				ReadXML.initApp(projectPath, nodeItem.getText());
			}
		});
		nodeItem.setText("Node Filtered");

		Menu nodeMenu = new Menu(popupMenu);
		nodeItem.setMenu(nodeMenu);

		final MenuItem classItem = new MenuItem(nodeMenu, SWT.NONE);
		classItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String projectName = trtmNewTreeitem.getText();
				PropertyFile.setProjectName(projectName);
				PropertyFile.setGraphDbPath(projectPath + projectName
						+ ".graphdb");
				PropertyFile.setGeneratedGexfFilePath(projectPath + projectName
						+ ".gexf");
				PropertyFile.setRelationshipXMLPath(projectPath
						+ "Relations.xml");
				PropertyFile.setGraphType(classItem.getText());
				System.out.println("Path: " + projectPath);
				System.out.println("DB Path: " + PropertyFile.graphDbPath);
				System.out.println("Graph Type: " + PropertyFile.graphType);
				ReadXML.initApp(projectPath, classItem.getText());
			}
		});
		classItem.setText("Class");

		final MenuItem attributeItem = new MenuItem(nodeMenu, SWT.NONE);
		attributeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String projectName = trtmNewTreeitem.getText();
				PropertyFile.setProjectName(projectName);
				PropertyFile.setGraphDbPath(projectPath + projectName
						+ ".graphdb");
				PropertyFile.setGeneratedGexfFilePath(projectPath + projectName
						+ ".gexf");
				PropertyFile.setRelationshipXMLPath(projectPath
						+ "Relations.xml");
				PropertyFile.setGraphType(attributeItem.getText());
				System.out.println("Graph Type: " + PropertyFile.graphType);
				ReadXML.initApp(projectPath, attributeItem.getText());
			}
		});
		attributeItem.setText("Attributes");

		final MenuItem methodItem = new MenuItem(nodeMenu, SWT.NONE);
		methodItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String projectName = trtmNewTreeitem.getText();
				PropertyFile.setProjectName(projectName);
				PropertyFile.setGraphDbPath(projectPath + projectName
						+ ".graphdb");
				PropertyFile.setGeneratedGexfFilePath(projectPath + projectName
						+ ".gexf");
				PropertyFile.setRelationshipXMLPath(projectPath
						+ "Relations.xml");
				PropertyFile.setGraphType(methodItem.getText());
				System.out.println("Path: " + projectPath);
				System.out.println("DB Path: " + PropertyFile.graphDbPath);
				System.out.println("Graph Type: " + PropertyFile.graphType);
				ReadXML.initApp(projectPath, methodItem.getText());
			}
		});
		methodItem.setText("Methods");
		tree.setMenu(popupMenu);
	}

	private void addMUCUI() {
		
	}

	private void defineListeners() {
		ctfCTF2L = new CTabFolder2Adapter() {
			public void close(CTabFolderEvent e) {
				e.doit = false;
				CTabFolder ctf = (CTabFolder) e.getSource();
				String ctfname = (String) ctf.getData();
				CTabItem item = ctf.getSelection();
				item.dispose();
				//ctf.dispose();
				workSF.layout();
				sidebarSF.layout();
			}

			public void maximize(CTabFolderEvent e) {
				e.doit = false;
				CTabFolder ctf = (CTabFolder) e.getSource();
				String ctfname = (String) ctf.getData();
				ctf.setMaximized(true);
				if (!ctfname.equalsIgnoreCase("Project")) {
					workSF.setMaximizedControl(ctf);
					sidebarSF.setMaximizedControl(workSF);
				} else
					sidebarSF.setMaximizedControl(ctf);

				System.out.println("Maximized: " + ctfname);
			}

			public void minimize(CTabFolderEvent e) {
				e.doit = false;
				CTabFolder ctf = (CTabFolder) e.getSource();
				String ctfname = (String) ctf.getData();
				
				if (ctfname.equalsIgnoreCase("WorkSpace")) {
					CTabFolder next = (CTabFolder) workSF.getTabList()[1];
					workSF.setMaximizedControl(next);
				} else if (ctfname.equalsIgnoreCase("Graph")) {
					CTabFolder next = (CTabFolder) workSF.getTabList()[0];
					workSF.setMaximizedControl(next);
				} else if (ctfname.equalsIgnoreCase("Project")) {
					CTabFolder next = (CTabFolder) workSF.getTabList()[1];
					sidebarSF.setMaximizedControl(workSF);
				}
				ctf.setMinimized(true);

				workSF.layout(true);
				sidebarSF.layout(true);
			}

			public void restore(CTabFolderEvent e) {
				e.doit = false;
				CTabFolder ctf = (CTabFolder) e.getSource();
				String ctfname = (String) ctf.getData();

				// if restoring from maximized state
				if (!ctfname.equalsIgnoreCase("Project")) {
					if (ctf.equals(workSF.getMaximizedControl())) {
						ctf.setMaximized(false);
						workSF.setMaximizedControl(null);
						sidebarSF.setMaximizedControl(null);
					}
				} else if (ctf.equals(sidebarSF.getMaximizedControl())) {
					ctf.setMaximized(false);
					sidebarSF.setMaximizedControl(null);
				}
				// if restoring from minimized state
				else {
					int w = ctf.getSize().x;
					ctf.setMinimized(false);

				}

				System.out.println("Restored: " + ctfname);
			}
		};

		ctfML = new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				CTabFolder ctf = (CTabFolder) e.getSource();
				if (!ctf.isDisposed()) {
					String ctfname = (String) ctf.getData();
					System.out.println("Clicked: " + ctfname);
				}
			}
		};
	}

	public static void deleteFiles(String projectPath) {

		TreeItem parent = trtmNewTreeitem.getParentItem();

		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
		File filePath = new File(projectPath);
		if (!filePath.isDirectory()) {
			projectPath = PropertyFile.filePath + parent.getText() + "/"
					+ trtmNewTreeitem.getText();
		}
		messageBox.setMessage("Do you really want to delete " + projectPath
				+ " ?");
		messageBox.setText("Deleting " + projectPath);
		int response = messageBox.open();
		if (response == SWT.YES) {
			File file = new File(projectPath);
			String[] files = file.list();
			if (files != null) {
				for (String stringFile : files) {
					File deleteFile = new File(projectPath + stringFile);
					deleteFile.delete();
				}
			}
			file.delete();
			HomeGUI.trtmNewTreeitem.dispose();
		}
		projComposite.layout();
	}
}
