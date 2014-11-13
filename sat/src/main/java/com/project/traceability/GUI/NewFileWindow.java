package com.project.traceability.GUI;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class NewFileWindow {

	protected Shell shell;
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			NewFileWindow window = new NewFileWindow();
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
		
		Label lblFileName = new Label(shell, SWT.NONE);
		lblFileName.setBounds(35, 42, 67, 15);
		lblFileName.setText("File Name :");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(108, 36, 233, 21);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 FileDialog fileDialog=new FileDialog(shell,SWT.SAVE);
				 fileDialog.setText("Open");
				 fileDialog.setFilterPath("C:/");
				 String localFilePath=fileDialog.open();
				 text.setText(localFilePath);
				 Path path = Paths.get(localFilePath);
				 Path target = Paths.get(NewProjectWindow.projectPath);
				 System.out.println(localFilePath);
				 if (localFilePath != null) {
					
					 	try {
							Files.copy(path, target.resolve(path.getFileName()), REPLACE_EXISTING);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					 	
				 }
			}
		});
		btnNewButton.setBounds(349, 32, 75, 25);
		btnNewButton.setText("Browse");
		
		Button btnSave = new Button(shell, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				createTabLayout();
			}
		});
		btnSave.setBounds(359, 227, 49, 25);
		btnSave.setText("Save");

	}
	
	public void createTabLayout() {
		HomeGUI.tabFolder.setVisible(true);
		
		TabItem tab1 = new TabItem(HomeGUI.tabFolder, SWT.NONE);
		tab1.setText("Tab 1");
	
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
