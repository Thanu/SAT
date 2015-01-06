package com.project.traceability.GUI;

/**
 * @author Gitanjali
 * Nov 12, 2014
 */
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.SourceCodeArtefactManager;
import com.project.traceability.manager.UMLArtefactManager;

public class HomeGUI {

	public static Dimension screen = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize();

	public static Shell shell;
	public static CTabFolder tabFolder;
	public static TabFolder tabFolder_1;
	public static Tree tree;
	public static Composite composite;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			HomeGUI window = new HomeGUI();
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

		shell.setBounds(0, 0, screen.width, screen.height - 20);
		center(shell);		

		tabFolder = new CTabFolder(shell, SWT.CLOSE);		
		
		// optional setting - enables close buttons only on selected tab (defaults to true)
		tabFolder.setUnselectedCloseVisible(false);

		//tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds((int) (screen.width * 0.2 + 20), 0,
				(int) (screen.width * 0.8 - 20), screen.height - 80);
		//tabFolder.setBackgroundMode(SWT.COLOR_GRAY);
		//tabFolder.setVisible(false);

		tabFolder_1 = new TabFolder(shell, SWT.NONE);
		tabFolder_1.setBounds(0, 0, (int) (screen.width * 0.2),
				screen.height - 80);
		composite = new Composite(tabFolder_1, 0);
		composite.setBounds(0, 0, (int) (screen.width * 0.2),
				screen.height - 80);
		composite.setData(tabFolder_1);
		

		File projectFile = new File(PropertyFile.filePath);
		projectFile.mkdir();
		ArrayList<String> projectFiles = new ArrayList<String>(
				Arrays.asList(projectFile.list()));
		if (projectFiles.isEmpty())
			tabFolder_1.setVisible(false);

		TabItem tbtmProjects = new TabItem(tabFolder_1, SWT.NONE);
		tbtmProjects.setText("Projects");

		tree = new Tree(tabFolder_1, SWT.BORDER);
		tbtmProjects.setControl(composite);
		
		tbtmProjects.setControl(tree);
		if (projectFiles.isEmpty())
			tree.setVisible(false);
		else {
			for (int i = 0; i < projectFiles.size(); i++) {
				TreeItem trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
				trtmNewTreeitem.setText(projectFiles.get(i));
				File file = new File(PropertyFile.filePath + projectFiles.get(i) + "/");
				ArrayList<String> files = new ArrayList<String>(Arrays.asList(file.list()));
				String path = PropertyFile.filePath + projectFiles.get(i) + "\\";
				for (int j = 0; j < files.size(); j++) {
					if(files.get(j).contains(".xml")) {
						TreeItem fileTreeItem = new TreeItem(trtmNewTreeitem,
								SWT.NONE);
						fileTreeItem.setText(files.get(j));
						if(files.get(j).contains("UML"))
							UMLArtefactManager.readXML(path);
						else if(files.get(j).contains("Source"))
							SourceCodeArtefactManager.readXML(path);
					}
					//else if(files.get(i).contains("Requirement"))
						//RequirementsManger.readXML(path);
				}
			}
			NewProjectWindow.addPopUpMenu();

		}
		
		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = HomeGUI.tree.getSelection();
				for (int i = 0; i < selection.length; i++){
					string += selection[i] + " ";
					NewProjectWindow.trtmNewTreeitem = selection[i];
				}
				string = string.substring(10, string.length()-2);				
				NewProjectWindow.projectPath = PropertyFile.filePath + string + "/";
				NewProjectWindow.addPopUpMenu();
			}
		});
		
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				System.out.println("_____");
				String string = "";
				String path = "";
				String parent = null;
				TreeItem[] selection = HomeGUI.tree.getSelection();
				for (int i = 0; i < selection.length; i++){
					string += selection[i] + " ";
					parent += selection[i].getParentItem() + " ";
					NewProjectWindow.trtmNewTreeitem = selection[i];
				}
				string = string.substring(10, string.length() - 2 );		
				parent = parent.substring(14, parent.length() - 2);
				System.out.println(parent);
				NewFileWindow.localFilePath = PropertyFile.filePath + parent + "/";
				NewFileWindow.createTabLayout(string);
			}
		});
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

		shell.setText("SWT Application");

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
}
