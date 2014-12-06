package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.GUI.CompareWindow;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;

public class UMLSourceClassManager {

	List<String> sourceCodeClasses = new ArrayList<String>();
	List<String> UMLClasses = new ArrayList<String>();
	static List<String> relationNodes = new ArrayList<String>();

	static String projectPath;
	static TableItem tableItem;

	/**
	 * check whether the designed classes are implemented in sourcecode
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> compareClassNames(String projectPath) {
		UMLSourceClassManager.projectPath = projectPath;

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
					if (sourceArtefactElement.getType().equalsIgnoreCase(
							"Class") // check whether the artefact element is
										// Class and same name
							&& sourceArtefactElement.getName()
									.equalsIgnoreCase(name)) {

						relationNodes.add(UMLArtefactElement
								.getArtefactElementId());
						relationNodes.add(sourceArtefactElement
								.getArtefactElementId());
						if (CompareWindow.tree != null
								&& !CompareWindow.tree.isDisposed()) {

							TreeItem item = new TreeItem(CompareWindow.tree,
									SWT.NONE);
							item.setText(0, sourceArtefactElement.getName());
							item.setText(1, UMLArtefactElement.getName());
							item.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE));
							
							List<ArtefactSubElement> sourceAttributeElements = sourceArtefactElement
									.getArtefactSubElements();
							ArrayList<String> UMLAttributesList = new ArrayList<String>();
							ArrayList<String> UMLMethodsList = new ArrayList<String>();

							ArrayList<String> sourceAttributesList = new ArrayList<String>();
							ArrayList<String> sourceMethodsList = new ArrayList<String>();

							for (int i = 0; i < UMLAttributeElements.size(); i++) {
								ArtefactSubElement UMLAttribute = UMLAttributeElements
										.get(i);
								for (int j = 0; j < sourceAttributeElements
										.size(); j++) {
									ArtefactSubElement sourceElement = sourceAttributeElements
											.get(j);
									if (UMLAttribute.getName()
											.equalsIgnoreCase(
													sourceElement.getName())) {
										relationNodes.add(UMLAttribute
												.getSubElementId());
										relationNodes.add(sourceElement
												.getSubElementId());

										if ((sourceElement.getType())
												.equalsIgnoreCase("Field")) {
											sourceAttributesList
													.add(sourceElement
															.getName());
											UMLAttributesList.add(UMLAttribute
													.getName());
										}

										else if ((sourceElement.getType())
												.equalsIgnoreCase("Method")) {
											sourceMethodsList.add(sourceElement
													.getName());
											UMLMethodsList.add(UMLAttribute
													.getName());
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
							for (int k = 0; k < sourceAttributesList.size(); k++) {
								TreeItem subItem = new TreeItem(item, SWT.NONE);
								subItem.setText(0, sourceAttributesList.get(k));
								subItem.setText(1, UMLAttributesList.get(k));
							}
							for (int k = 0; k < sourceMethodsList.size(); k++) {
								TreeItem subItem = new TreeItem(item, SWT.NONE);
								subItem.setText(0, sourceMethodsList.get(k));
								subItem.setText(1, UMLMethodsList.get(k));
							}

							if (UMLAttributeElements.size() > 0
									|| sourceAttributeElements.size() > 0) {
								if (UMLAttributeElements.size() > 0) {

									for (ArtefactSubElement model : UMLAttributeElements) {
										TreeItem subItem = new TreeItem(item,
												SWT.NONE);
										subItem.setText(1, model.getName());
										subItem.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
									}

								}

								if (sourceAttributeElements.size() > 0) {
									for (ArtefactSubElement model : sourceAttributeElements) {
										TreeItem subItem = new TreeItem(item,
												SWT.NONE);
										subItem.setText(0, model.getName());
										subItem.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
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
				if(CompareWindow.tree != null && !CompareWindow.shell.isDisposed()) {
					TreeItem item = new TreeItem(CompareWindow.tree,
							SWT.NONE);
					item.setText(0, artefact.getValue().getName());
					item.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				}
			}

			while (sourceIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = sourceIterator
						.next();
				if(CompareWindow.tree != null && !CompareWindow.shell.isDisposed()) {
					TreeItem item = new TreeItem(CompareWindow.tree,
							SWT.NONE);
					item.setText(1, artefact.getValue().getName());
					item.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
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
