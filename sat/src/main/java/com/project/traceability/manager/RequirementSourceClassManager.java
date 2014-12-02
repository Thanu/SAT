/**
 * 
 */
package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

import com.project.traceability.GUI.CompareWindow;
import com.project.traceability.ir.LevenshteinDistance;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;

/**13 Nov 2014
 * @author K.Kamalan
 *
 */
public class RequirementSourceClassManager {
	List<String> sourceCodeClasses = new ArrayList<String>();
	List<String> requirementClasses = new ArrayList<String>();
	static List<String> relationNodes = new ArrayList<String>();
	
	static String projectPath;
	static TableItem tableItem;
	

	/**
	 * check whether the requirement classes are implemented in sourcecode
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> compareClassNames(String projectPath) {
		RequirementSourceClassManager.projectPath = projectPath;
		
		Map<String, ArtefactElement> reqMap = RequirementsManger.requirementArtefactElements;
		Iterator<Entry<String, ArtefactElement>> requirementIterator = reqMap
				.entrySet().iterator();
		Map<String, ArtefactElement> artefactMap = SourceCodeArtefactManager.sourceCodeAretefactElements;
		Iterator<Entry<String, ArtefactElement>> sourceIterator = null;
		while (requirementIterator.hasNext()) {
			Map.Entry pairs = requirementIterator.next();
			ArtefactElement reqArtefactElement = (ArtefactElement) pairs
					.getValue();
			String name = reqArtefactElement.getName();
			List<ArtefactSubElement> reqAttributeElements = reqArtefactElement.getArtefactSubElements();
			if (reqArtefactElement.getType().equalsIgnoreCase("Class")) {
				sourceIterator = artefactMap.entrySet().iterator();
				while (sourceIterator.hasNext()) {
					Map.Entry pairs1 = sourceIterator.next();
					ArtefactElement sourceArtefactElement = (ArtefactElement) pairs1
							.getValue();
					LevenshteinDistance.printDistance(sourceArtefactElement.getName(), name);
					if (sourceArtefactElement.getType().equalsIgnoreCase("Class") && (sourceArtefactElement.getName()
									.equalsIgnoreCase(name) ||LevenshteinDistance.printDistance(sourceArtefactElement.getName(), name)>0.6))  {
						relationNodes.add(reqArtefactElement
								.getArtefactElementId().substring(reqArtefactElement
								.getArtefactElementId().length()-3));
						relationNodes.add(sourceArtefactElement
								.getArtefactElementId());
						if(CompareWindow.table != null && !CompareWindow.table.isDisposed()){
							tableItem = new TableItem(CompareWindow.table, SWT.NONE);
							tableItem.setText(sourceArtefactElement.getName());
							tableItem.setData("0", "Source File : " + sourceArtefactElement.getName() + 
									"\nUML File :" + reqArtefactElement.getName());
							
							List<ArtefactSubElement> sourceAttributeElements = sourceArtefactElement.getArtefactSubElements();
							ArrayList<String> attributesList = new ArrayList<String>();
							ArrayList<String> methodsList = new ArrayList<String>();
							
							ArrayList<String> attributesDataList = new ArrayList<String>();		// for maintaining tooltip 
							ArrayList<String> methodsDataList = new ArrayList<String>();        //in the compared table	
							
							for (int i = 0; i < sourceAttributeElements.size(); i++) {
								ArtefactSubElement sourceAttribute = sourceAttributeElements
										.get(i);
								for (int j = 0; j < reqAttributeElements.size(); j++) {
									ArtefactSubElement requElement = reqAttributeElements
											.get(j);
									if (sourceAttribute.getName().equalsIgnoreCase(requElement.getName())
											||LevenshteinDistance.similarity(sourceAttribute.getName(), requElement.getName())>.6) {
										/*relationNodes.add(UMLAttribute
												.getSubElementId());
										relationNodes.add(sourceElement
												.getSubElementId());*/

										if (requElement.getType().equalsIgnoreCase("Field")){											
													attributesList.add(sourceAttribute.getName());
													attributesDataList.add(requElement.getName());
													relationNodes.add(requElement
															.getSubElementId().substring(requElement
																	.getSubElementId().length()-3));
													relationNodes.add(sourceAttribute.getSubElementId());
										}

										else if ((requElement.getType()).equalsIgnoreCase("Method")){
												methodsList.add(sourceAttribute.getName());
												methodsDataList.add(requElement.getName());
												relationNodes.add(requElement
														.getSubElementId().substring(requElement
																.getSubElementId().length()-3));
												relationNodes.add(sourceAttribute.getSubElementId());
										}

										sourceAttributeElements.remove(sourceAttribute);
										reqAttributeElements
												.remove(requElement);
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
											"\nRequirement File :" + attributesDataList.get(k));
								}
								if (k < methodsList.size()){
									tableItem.setText(2, methodsList.get(k));
									tableItem.setData("2", "Source File : " + methodsList.get(k) + 
											"\nRequirement File :" + methodsDataList.get(k));
								}
								tableItem = new TableItem(CompareWindow.table,
										SWT.NONE);
							}
							if (sourceAttributeElements.size() > 0
									|| reqAttributeElements.size() > 0) {
								if (sourceAttributeElements.size() > 0) {
									
									CompareWindow.text_1.append("SourceArtefactFile has following different attributes/methods in "
											+ sourceArtefactElement.getName() + "\n");
									for (ArtefactSubElement model : sourceAttributeElements)
										CompareWindow.text_1.append((model.getName()) + "\n");
									
								}

								if (reqAttributeElements.size() > 0) {
									System.out.println("dsvdddddvvvvvvvvv");
									
									CompareWindow.text_2.append("RequirementArtefactFile has following different attributes/methods in "
											+ reqArtefactElement.getName()
											+ "\n");
									for (ArtefactSubElement model : reqAttributeElements)
										CompareWindow.text_2.append((model.getName()) + "\n");
									
								}
							}
						}
						artefactMap.remove(sourceArtefactElement
								.getArtefactElementId());
						reqMap.remove(reqArtefactElement.getArtefactElementId());
						requirementIterator = reqMap.entrySet().iterator();
						break;
					}
				}
			}
		}
		if (artefactMap.size() > 0 || reqMap.size() > 0) {
			requirementIterator = reqMap.entrySet().iterator();
			sourceIterator = artefactMap.entrySet().iterator();
			CompareWindow.text_2.append("RequirementArtefactFile has following different classes from RequirementArtefactFile: \n");
			while (requirementIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = requirementIterator
						.next();
				if(CompareWindow.tabFolder_2 != null){					
					CompareWindow.text_2.append(artefact.getValue().getName() + "\n");
				}
				
			}
		
			CompareWindow.text_1.append("SourceArtefactFile has following different classes from Source ArtefactFile: \n");
			while (sourceIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = sourceIterator
						.next();
				if(CompareWindow.tabFolder_1 != null){					
					CompareWindow.text_1.append(artefact.getValue().getName() + "\n");
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
		RequirementsManger.readXML(projectPath);
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
		// UMLArtefactManager.readXML();
		Iterator it1 = RequirementsManger.requirementArtefactElements.entrySet()
				.iterator();
		int countReqClass = 0;
		while (it1.hasNext()) {
			Map.Entry pairs = (Entry) it1.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			if (artefactElement.getType().equalsIgnoreCase("Class")) {
				countReqClass++;
			}
			List<ArtefactSubElement> artefactSubElements = artefactElement
					.getArtefactSubElements();
			it1.remove(); // avoids a ConcurrentModificationException
		}

		if (countSourceClass == countReqClass) {
			System.out.println("class compared");
		}
		return countSourceClass;
	}


}
