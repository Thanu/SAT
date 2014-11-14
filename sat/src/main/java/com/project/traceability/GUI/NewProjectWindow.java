package com.project.traceability.GUI;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.db.VisualizeGraph;

public class NewProjectWindow {

	protected Shell shell;
	private Text text;
	public static String projectPath = null;
	public static TreeItem trtmNewTreeitem;

	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		try {
			NewProjectWindow window = new NewProjectWindow();
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
		
		Label lblProjectName = new Label(shell, SWT.NONE);
		lblProjectName.setBounds(10, 32, 81, 15);
		lblProjectName.setText("Project Name :");
		
		Button btnNewButton = new Button(shell, SWT.SAVE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String projectName = text.getText();
				projectPath = "D:/SATWork/" + projectName +"/";
				System.out.println(new File(projectPath).mkdir());
				shell.close();
				HomeGUI.shell.setText("SAT- "+ projectName);
				HomeGUI.tabFolder_1.setVisible(true);
				HomeGUI.tree.setVisible(true);
				
				trtmNewTreeitem = new TreeItem(HomeGUI.tree, SWT.NONE);
				trtmNewTreeitem.setText(projectName);
				
				addPopUpMenu();
			}
		});
		btnNewButton.setBounds(295, 227, 51, 25);
		btnNewButton.setText("Create");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnNewButton_1.setBounds(359, 227, 65, 25);
		btnNewButton_1.setText("Cancel");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(97, 26, 316, 21);

	}
	
	public void addPopUpMenu() {
		Menu popupMenu = new Menu(HomeGUI.tree);
	    MenuItem newItem = new MenuItem(popupMenu, SWT.CASCADE);
	    newItem.setText("New");
	    
	    MenuItem refreshItem = new MenuItem(popupMenu, SWT.NONE);
	    refreshItem.setText("Refresh");
	    
	    MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
	    deleteItem.setText("Delete");
	    
	    MenuItem graphItem = new MenuItem(popupMenu, SWT.NONE);
	    graphItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				VisualizeGraph visualizeGraph = new VisualizeGraph();
				visualizeGraph.script();
			}
		});
	    graphItem.setText("Show Graph");


	    Menu newMenu = new Menu(popupMenu);
	    newItem.setMenu(newMenu);

	    MenuItem fileItem = new MenuItem(newMenu, SWT.NONE);
	    fileItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewFileWindow newFileWin = new NewFileWindow();
				newFileWin.open();
			}
		});
	    fileItem.setText("File");
	    
	    HomeGUI.tree.setMenu(popupMenu);
	    
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
