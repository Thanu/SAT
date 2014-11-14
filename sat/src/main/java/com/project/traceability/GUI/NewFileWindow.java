package com.project.traceability.GUI;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Tree;

public class NewFileWindow {

	static Shell shell;
	private Text text;
	static Path path;
	static String localFilePath;
	LineStyler lineStyler = new LineStyler();
	
	StyledText codeText;

	FileDialog fileDialog;
	private Text text_1;



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
		
		Tree tree = new Tree(shell, SWT.BORDER);
		tree.setBounds(12, 57, 412, 338);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(12, 30, 412, 21);
		
		Label lblParentFolderPath = new Label(shell, SWT.NONE);
		lblParentFolderPath.setBounds(12, 10, 104, 15);
		lblParentFolderPath.setText("Parent folder path");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(320, 472, 49, 25);
		btnCancel.setText("Cancel");
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
		shell.setSize(450, 545);
		shell.setText("New File");
		
		Label lblFileName = new Label(shell, SWT.NONE);
		lblFileName.setBounds(12, 418, 67, 15);
		lblFileName.setText("File Name :");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(85, 415, 247, 21);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 FileDialog fileDialog=new FileDialog(shell,SWT.SAVE);
				 fileDialog.setText("Open");
				 fileDialog.setFilterPath("F:/Computer/Semester 7/R & D Project/Product overview documents/");
				 localFilePath=fileDialog.open();
				 text.setText(localFilePath);
				 path = Paths.get(localFilePath);
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
		btnNewButton.setBounds(349, 413, 75, 25);
		btnNewButton.setText("Browse");
		
		Button btnSave = new Button(shell, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				TreeItem treeItem = new TreeItem(NewProjectWindow.trtmNewTreeitem, SWT.NONE);
				treeItem.setText(path.getFileName().toString());
				createTabLayout();
			}
		});
		btnSave.setBounds(375, 472, 49, 25);
		btnSave.setText("Save");

	}
	
	public void createTabLayout() {
		HomeGUI.tabFolder.setVisible(true);
		
		//TreeItem treeItem = new TreeItem(NewProjectWindow.trtmNewTreeitem, SWT.NONE);
		//treeItem.setText(path.getFileName().toString());
		
		CTabItem tabItem = new CTabItem(HomeGUI.tabFolder, SWT.NONE);
		tabItem.setText(path.getFileName().toString());
		
		Composite composite = new Composite(HomeGUI.tabFolder, SWT.NONE);
		
		composite.setLayout(new GridLayout(1, false));
		composite.addControlListener(new ControlListener() {
			
			@Override
			public void controlResized(ControlEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void controlMoved(ControlEvent arg0) {
				lineStyler.disposeColors();
				codeText.removeLineStyleListener(lineStyler);
				
			}
		});
		
		GridData spec = new GridData();
		spec.horizontalAlignment = GridData.FILL;
		spec.grabExcessHorizontalSpace = true;
		spec.verticalAlignment = GridData.FILL;
		spec.grabExcessVerticalSpace = true;
		composite.setLayoutData(spec);
				
		codeText = new StyledText(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
				| SWT.H_SCROLL);		
		codeText.setLayoutData(spec);
		codeText.addLineStyleListener(lineStyler);
		codeText.setEditable(false);
		Color bg = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		codeText.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		final String textString;
		File file = new File(localFilePath);
		try {
			FileInputStream stream = new FileInputStream(file.getPath());
			try {
				Reader in = new BufferedReader(new InputStreamReader(stream));
				char[] readBuffer = new char[2048];
				StringBuffer buffer = new StringBuffer((int) file.length());
				int n;
				while ((n = in.read(readBuffer)) > 0) {
					buffer.append(readBuffer, 0, n);
				}
				textString = buffer.toString();
				stream.close();
			} catch (IOException e) {
				String message = "Err_file_io";
				displayError(message);
				return;
			}
		} catch (FileNotFoundException e) {
			String message = "Err_not_found";
			displayError(message);
			return;
		}
		
		lineStyler.parseBlockComments(textString);
		
		Display display = codeText.getDisplay();
		display.asyncExec(new Runnable() {
			public void run() {
				codeText.setText(textString);
			}
		});
		
		composite.setData(codeText);
		tabItem.setControl(composite);
	}
	
	public void center(Shell shell) {

        Rectangle bds = shell.getDisplay().getBounds();

        Point p = shell.getSize();
        shell.setFullScreen(true);
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;

        shell.setBounds(nLeft, nTop, p.x, p.y);
    }
	
	void displayError(String msg) {
		MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
		box.setMessage(msg);
		box.open();
	}
}
