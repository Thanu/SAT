package com.project.traceability.GUI;

/**
 * @author Gitanjali
 * Nov 12, 2014
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

public class HomeGUI {

	public static Shell shell;
	public static TabFolder tabFolder;
	public static TabFolder tabFolder_1;

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
		center(shell);
		
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(122, 0, 302, 242);
		tabFolder.setVisible(false);
		
		tabFolder_1 = new TabFolder(shell, SWT.NONE);
		tabFolder_1.setBounds(0, 0, 116, 242);
		tabFolder_1.setVisible(false);
					
		//initUI();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void initUI() {

        Label label = new Label(shell, SWT.LEFT);
        
        Point p = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        label.setBounds(5, 5, p.x+5, p.y+5);
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
