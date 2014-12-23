package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.GUI.CompareWindow;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.semanticAnalysis.SynonymWords;

public class UMLSourceClassManager {

	static List<String> sourceCodeClasses = new ArrayList<String>();
	static List<String> UMLClasses = new ArrayList<String>();
	static List<String> relationNodes = new ArrayList<String>();

	static String projectPath;
	static TableItem tableItem;
	static TreeItem classItem;

	/**
	 * check whether the designed classes are implemented in sourcecode
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> compareClassNames(String projectPath) {
		UMLSourceClassManager.projectPath = projectPath;
                UMLClasses = ClassManager.getUmlClassName(projectPath);
		Map<String, ArtefactElement> UMLMap = UMLArtefactManager.UMLAretefactElements; // get
																						// map
																						// from
																						// extraction
																						// class
		Iterator<Entry<String, ArtefactElement>> UMLIterator = UMLMap
				.entrySet().iterator();

		Map<String, ArtefactElement> artefactMap = SourceCodeArtefactManager.sourceCodeAretefactElements; // get
																											// map
																											// from
																											// extraction
																											// class
		Iterator<Entry<String, ArtefactElement>> sourceIterator = null;

		if(CompareWindow.tree != null){
			TreeColumn column1 = new TreeColumn(CompareWindow.tree, SWT.LEFT);
			column1.setText("SourceXML File");
			column1.setWidth(300);
			
			TreeColumn column2 = new TreeColumn(CompareWindow.tree, SWT.LEFT);
			column2.setText("UML-XML file");
			column2.setWidth(300);
		}
		
		while (UMLIterator.hasNext()) {
			Map.Entry pairs = UMLIterator.next();
			ArtefactElement UMLArtefactElement = (ArtefactElement) pairs
					.getValue(); // get an UML artefact element
			String name = UMLArtefactElement.getName();
			List<ArtefactSubElement> UMLAttributeElements = UMLArtefactElement
					.getArtefactSubElements();
			if (UMLArtefactElement.getType().equalsIgnoreCase("Class")) {

				sourceIterator = artefactMap.entrySet().iterator(); // create an
																	// iterator
																	// for
																	// sourceCodeElements

				while (sourceIterator.hasNext()) {
					Map.Entry pairs1 = sourceIterator.next();
					ArtefactElement sourceArtefactElement = (ArtefactElement) pairs1
							.getValue(); // get sourceartefact element
                                        
                                        if(sourceArtefactElement.getType().equalsIgnoreCase(
							"Class") && SynonymWords.checkSymilarity(sourceArtefactElement.getName(), 
                                                                name, sourceArtefactElement.getType())){
//					if (sourceArtefactElement.getType().equalsIgnoreCase(
//							"Class") // check whether the artefact element is
//										// Class and same name
//							&& sourceArtefactElement.getName()
//									.equalsIgnoreCase(name)) {

						relationNodes.add(UMLArtefactElement
								.getArtefactElementId());
						relationNodes.add(sourceArtefactElement
								.getArtefactElementId());

						if (CompareWindow.tree != null
								&& !CompareWindow.tree.isDisposed()) {
							classItem = new TreeItem(CompareWindow.tree, SWT.NONE);
							classItem.setText(0, sourceArtefactElement.getName());
							classItem.setData("0", sourceArtefactElement);
							classItem.setText(1, UMLArtefactElement.getName());
							classItem.setData("1", UMLArtefactElement);
							classItem.setForeground(Display.getDefault()
									.getSystemColor(SWT.COLOR_DARK_BLUE));
						}

						ArrayList<ArtefactSubElement> UMLAttributesList = new ArrayList<ArtefactSubElement>();
						ArrayList<ArtefactSubElement> UMLMethodsList = new ArrayList<ArtefactSubElement>();

						ArrayList<ArtefactSubElement> sourceAttributesList = new ArrayList<ArtefactSubElement>();
						ArrayList<ArtefactSubElement> sourceMethodsList = new ArrayList<ArtefactSubElement>();

						List<ArtefactSubElement> sourceAttributeElements = sourceArtefactElement
								.getArtefactSubElements();
						for (int i = 0; i < UMLAttributeElements.size(); i++) {
							ArtefactSubElement UMLAttribute = UMLAttributeElements
									.get(i);
							for (int j = 0; j < sourceAttributeElements.size(); j++) {
								ArtefactSubElement sourceElement = sourceAttributeElements
										.get(j);
                                                                if(SynonymWords.checkSymilarity(UMLAttribute.getName(), sourceElement.getName(), sourceElement.getType(),
                                                                        UMLClasses)){
//								if (UMLAttribute.getName().equalsIgnoreCase(
//										sourceElement.getName())) {
									relationNodes.add(UMLAttribute
											.getSubElementId());
									relationNodes.add(sourceElement
											.getSubElementId());
									if (CompareWindow.tree != null
											&& !CompareWindow.tree.isDisposed()) {
										if ((sourceElement.getType())
												.equalsIgnoreCase("Field")) {
											sourceAttributesList
													.add(sourceElement);
											UMLAttributesList.add(UMLAttribute);
										}

										else if ((sourceElement.getType())
												.equalsIgnoreCase("Method")) {
											sourceMethodsList.add(sourceElement);
											UMLMethodsList.add(UMLAttribute);
										}

										UMLAttributeElements
												.remove(UMLAttribute);
										sourceAttributeElements
												.remove(sourceElement);
										i--;
										j--;
										break;
									}
								}
							}
						}
						if (CompareWindow.tree != null
								&& !CompareWindow.tree.isDisposed()) {
							TreeItem subAttribute = new TreeItem(classItem, SWT.NONE);
							subAttribute.setText("Attributes");
							subAttribute.setForeground(Display.getDefault()
									.getSystemColor(SWT.COLOR_GREEN));
							for (int k = 0; k < sourceAttributesList.size(); k++) {
								TreeItem subItem = new TreeItem(subAttribute, SWT.NONE);
								subItem.setText(0, sourceAttributesList.get(k).getName());
								subItem.setData("0", sourceAttributesList.get(k));
								subItem.setText(1, UMLAttributesList.get(k).getName());
								subItem.setData("1", UMLAttributesList.get(k));
							}
							TreeItem subMethod = new TreeItem(classItem, SWT.NONE);
							subMethod.setText("Methods");
							subMethod.setForeground(Display.getDefault()
									.getSystemColor(SWT.COLOR_GREEN));
							for (int k = 0; k < sourceMethodsList.size(); k++) {
								TreeItem subItem = new TreeItem(subMethod, SWT.NONE);
								subItem.setText(0, sourceMethodsList.get(k).getName());
								subItem.setData("0", sourceMethodsList.get(k));
								subItem.setText(1, UMLMethodsList.get(k).getName());
								subItem.setData("1", UMLMethodsList.get(k));
							}
							if (UMLAttributeElements.size() > 0) {
								for (ArtefactSubElement model : UMLAttributeElements) {
									if(model.getType().equalsIgnoreCase("UMLAttribute")){
										TreeItem subItem = new TreeItem(subAttribute,
												SWT.NONE);
										subItem.setText(1, model.getName());
										subItem.setForeground(Display
												.getDefault().getSystemColor(
														SWT.COLOR_RED));
									} else if(model.getType().equalsIgnoreCase("UMLOperation")){
										TreeItem subItem = new TreeItem(subMethod,
												SWT.NONE);
										subItem.setText(1, model.getName());
										subItem.setForeground(Display
												.getDefault().getSystemColor(
														SWT.COLOR_RED));
									}
									
								}

							}
							if (sourceAttributeElements.size() > 0) {
								for (ArtefactSubElement model : sourceAttributeElements) {
									if(model.getType().equalsIgnoreCase("Field")){
										TreeItem subItem = new TreeItem(subAttribute,
												SWT.NONE);
										subItem.setText(0, model.getName());
										subItem.setForeground(Display
												.getDefault().getSystemColor(
														SWT.COLOR_RED));
									} else if(model.getType().equalsIgnoreCase("Method")){
										TreeItem subItem = new TreeItem(subMethod,
												SWT.NONE);
										subItem.setText(0, model.getName());
										subItem.setForeground(Display
												.getDefault().getSystemColor(
														SWT.COLOR_RED));
									}
								}

							}
						}
						

						artefactMap.remove(sourceArtefactElement
								.getArtefactElementId());
						UMLMap.remove(UMLArtefactElement.getArtefactElementId());
						UMLIterator = UMLMap.entrySet().iterator();
						break;
					}

				}
			}
		}

		if (artefactMap.size() > 0 || UMLMap.size() > 0) {
			UMLIterator = UMLMap.entrySet().iterator();
			sourceIterator = artefactMap.entrySet().iterator();

			while (UMLIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = UMLIterator
						.next();
				if (CompareWindow.tree != null
						&& !CompareWindow.shell.isDisposed()) {
					TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
					item.setText(0, artefact.getValue().getName());
					item.setData("0", artefact.getValue());
					item.setForeground(Display.getDefault().getSystemColor(
							SWT.COLOR_RED));
				}
			}

			while (sourceIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = sourceIterator
						.next();
				if (CompareWindow.tree != null
						&& !CompareWindow.shell.isDisposed()) {
					TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
					item.setText(1, artefact.getValue().getName());
					item.setData("1", artefact.getValue());
					item.setForeground(Display.getDefault().getSystemColor(
							SWT.COLOR_RED));
				}
			}
		}
		
		return relationNodes;
	}

	@SuppressWarnings("rawtypes")
	public static int compareClassCount() {
		SourceCodeArtefactManager.readXML(projectPath);
		UMLArtefactManager.readXML(projectPath);
		Iterator it = SourceCodeArtefactManager.sourceCodeAretefactElements
				.entrySet().iterator();
		int countSourceClass = 0;
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			if (artefactElement.getType().equalsIgnoreCase("Class")) {

				countSourceClass++;
			}
			List<ArtefactSubElement> artefactSubElements = artefactElement
					.getArtefactSubElements();
			it.remove(); // avoids a ConcurrentModificationException
		}
		Iterator it1 = UMLArtefactManager.UMLAretefactElements.entrySet()
				.iterator();
		int countUMLClass = 0;
		while (it1.hasNext()) {
			Map.Entry pairs = (Entry) it1.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			if (artefactElement.getType().equalsIgnoreCase("Class")) {
				countUMLClass++;
			}
			List<ArtefactSubElement> artefactSubElements = artefactElement
					.getArtefactSubElements();
			it1.remove(); // avoids a ConcurrentModificationException
		}

		if (countSourceClass == countUMLClass) {
			System.out.println("class compared");
		}
		return countSourceClass;
	}
}
