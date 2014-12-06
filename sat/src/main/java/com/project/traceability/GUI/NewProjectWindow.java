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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.internal.handlers.WizardHandler.New;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.ReadXML;

public class NewProjectWindow {

	public static Shell shell;
	private Text text;
	public static String projectPath = null;
	public static TreeItem trtmNewTreeitem;

	/**
	 * Launch the application.
	 * 
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
				projectPath = PropertyFile.filePath + projectName + "/";
				shell.close();
				HomeGUI.shell.setText("SAT- " + projectName);
				HomeGUI.tabFolder_1.setVisible(true);
				HomeGUI.tree.setVisible(true);

				trtmNewTreeitem = new TreeItem(HomeGUI.tree, SWT.NONE);
				trtmNewTreeitem.setText(projectName);

				File file = new File(projectPath);
				file.mkdir();
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

	public static void addPopUpMenu() {
		Menu popupMenu = new Menu(HomeGUI.tree);
		MenuItem newItem = new MenuItem(popupMenu, SWT.CASCADE);
		newItem.setText("New");
		
		MenuItem graphItem = new MenuItem(popupMenu, SWT.CASCADE);
		graphItem.setText("Visualization");

		MenuItem refreshItem = new MenuItem(popupMenu, SWT.NONE);
		refreshItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HomeGUI.composite.pack(true);
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
				FileSelectionWindow window = new FileSelectionWindow();
				String string = "";
				TreeItem[] selection = HomeGUI.tree.getSelection();
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
					TreeItem parent = NewProjectWindow.trtmNewTreeitem
							.getParentItem();
					NewProjectWindow.trtmNewTreeitem = NewProjectWindow.trtmNewTreeitem
							.getParentItem();
					projectPath = PropertyFile.filePath + parent.getText();
				}
				NewFileWindow newFileWin = new NewFileWindow();
				newFileWin.open();
			}
		});
		fileItem.setText("File");
		
		Menu visualMenu = new Menu(popupMenu);
		graphItem.setMenu(visualMenu);

		final MenuItem allItem = new MenuItem(visualMenu, SWT.NONE);
		allItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReadXML.initApp(projectPath, allItem.getText());
			}
		});
		allItem.setText("Full Graph");
		
		final MenuItem edgeItem = new MenuItem(visualMenu, SWT.NONE);
		edgeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReadXML.initApp(projectPath, edgeItem.getText());
			}
		});
		edgeItem.setText("Edge Filtered");
		
		final MenuItem nodeItem = new MenuItem(visualMenu, SWT.NONE);
		nodeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReadXML.initApp(projectPath, nodeItem.getText());
			}
		});
		nodeItem.setText("Node Filtered");

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

	public static void deleteFiles(String projectPath) {

		MessageBox messageBox = new MessageBox(HomeGUI.shell, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
		File filePath = new File(projectPath);
		if (!filePath.isDirectory()) {
			TreeItem parent = NewProjectWindow.trtmNewTreeitem
					.getParentItem();
			projectPath = PropertyFile.filePath 
					+ parent.getText() + "/"+  NewProjectWindow.trtmNewTreeitem.getText();
		}

		messageBox.setMessage("Do you really want to delete " + projectPath
				+ " ?");
		messageBox.setText("Deleting " + projectPath);
		int response = messageBox.open();
		if (response == SWT.YES) {
			File file = new File(projectPath);
			String[] files = file.list();
			if (files != null)
				for (String stringFile : files) {
					File deleteFile = new File(stringFile);
					deleteFile.delete();
				}
			file.delete();
		}
	}
}
