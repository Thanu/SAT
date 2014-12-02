package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

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

		Map<String, ArtefactElement> UMLMap = UMLArtefactManager.UMLAretefactElements; // get map from extraction class
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
						if(CompareWindow.table != null && !CompareWindow.table.isDisposed()){
							tableItem = new TableItem(CompareWindow.table, SWT.NONE);
							tableItem.setText(sourceArtefactElement.getName());
							tableItem.setData("0", "Source File : " + sourceArtefactElement.getName() + 
									"\nUML File :" + UMLArtefactElement.getName());

							List<ArtefactSubElement> sourceAttributeElements = sourceArtefactElement
									.getArtefactSubElements();
							ArrayList<String> attributesList = new ArrayList<String>();
							ArrayList<String> methodsList = new ArrayList<String>();
							
							ArrayList<String> attributesDataList = new ArrayList<String>();		// for maintaining tooltip 
							ArrayList<String> methodsDataList = new ArrayList<String>();        //in the compared table							
							
							for (int i = 0; i < UMLAttributeElements.size(); i++) {
								ArtefactSubElement UMLAttribute = UMLAttributeElements
										.get(i);
								for (int j = 0; j < sourceAttributeElements.size(); j++) {
									ArtefactSubElement sourceElement = sourceAttributeElements
											.get(j);
									if (UMLAttribute.getName().equalsIgnoreCase(
											sourceElement.getName())) {
										relationNodes.add(UMLAttribute
												.getSubElementId());
										relationNodes.add(sourceElement
												.getSubElementId());

										if ((sourceElement.getType())
												.equalsIgnoreCase("Field")){
											attributesList.add(sourceElement
													.getName());
											attributesDataList.add(UMLAttribute
													.getName());
										}

										else if ((sourceElement.getType())
												.equalsIgnoreCase("Method")){
											methodsList
													.add(sourceElement.getName());
											methodsDataList
											.add(UMLAttribute.getName());
										}

										UMLAttributeElements.remove(UMLAttribute);
										sourceAttributeElements
												.remove(sourceElement);
										i--;
										j--;
										break;
									}
								}
							}
							int max = Math.max(attributesList.size(),
									methodsList.size());
							for (int k = 0; k < max; k++) {
								if (k < attributesList.size()){
									tableItem.setText(1, attributesList.get(k));
									tableItem.setData("1", "Source File : " + attributesList.get(k) + 
											"\nUML File :" + attributesDataList.get(k));
								}
								if (k < methodsList.size()){
									tableItem.setText(2, methodsList.get(k));
									tableItem.setData("2", "Source File : " + methodsList.get(k) + 
											"\nUML File :" + methodsDataList.get(k));
								}
								tableItem = new TableItem(CompareWindow.table,
										SWT.NONE);
							}
							if (UMLAttributeElements.size() > 0
									|| sourceAttributeElements.size() > 0) {
								if (UMLAttributeElements.size() > 0) {
									
									CompareWindow.text_1.append("UMLArtefactFile has following different attributes in "
											+ UMLArtefactElement.getName() + "\n");
									for (ArtefactSubElement model : UMLAttributeElements){
										CompareWindow.text_1.append((model.getName()) + "\n");
										CompareWindow.text_1.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
									}
									
								}

								if (sourceAttributeElements.size() > 0) {
									CompareWindow.text_2.append("SourceCodeArtefactFile has following different attributes in "
											+ sourceArtefactElement.getName()
											+ "\n");
									for (ArtefactSubElement model : sourceAttributeElements)
										CompareWindow.text_2.append((model.getName()) + "\n");
									
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
				if(CompareWindow.tabFolder_1 != null){
					
					CompareWindow.text_1.append("UMLArtefactFile has following different classes from SourceCodeArtefactFile: \n"
							+ artefact.getValue().getName() + "\n");
				}
			}
			
			while (sourceIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = sourceIterator
						.next();
				if(CompareWindow.tabFolder_2 != null){
					
					CompareWindow.text_2.append("SourceCodeArtefactFile has following different classes from UMLArtefactFile: \n"
									+ artefact.getValue().getName() + "\n");
				}
			}
		}
		
		
		CompareWindow.composite_1.setData(CompareWindow.text_1);
		CompareWindow.tabItem_1.setControl(CompareWindow.composite_1);

		CompareWindow.composite_2.setData(CompareWindow.text_2);
		CompareWindow.tabItem_2.setControl(CompareWindow.composite_2);

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
