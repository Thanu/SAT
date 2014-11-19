package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

import com.project.traceability.GUI.CompareWindow;
import com.project.traceability.ir.LevenshteinDistance;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;

public class RequirementUMLClassManager {

	List<String> umlClasses = new ArrayList<String>();
	List<String> requirementClasses = new ArrayList<String>();
	static List<String> relationNodes = new ArrayList<String>();

	static String projectPath;
	static TableItem tableItem;
	static StyledText text_1;
	static StyledText text_2;
	
	static Composite composite_1;
	static Composite composite_2;

	/**
	 * check whether the requirement classes are implemented in UML
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> compareClassNames(String projectPath) {
		// UMLArtefactManager.readXML(projectPath);
		// RequirementsManger.readXML(projectPath);
		Map<String, ArtefactElement> reqMap = RequirementsManger.requirementArtefactElements;
		Iterator<Entry<String, ArtefactElement>> requirementIterator = reqMap
				.entrySet().iterator();
		Map<String, ArtefactElement> artefactMap = UMLArtefactManager.UMLAretefactElements;
		Iterator<Entry<String, ArtefactElement>> umlIterator = null;
		int count = 0;
		while (requirementIterator.hasNext()) {
			Map.Entry pairs = requirementIterator.next();
			ArtefactElement reqArtefactElement = (ArtefactElement) pairs
					.getValue();
			String name = reqArtefactElement.getName();
			List<ArtefactSubElement> reqAttributeElements = reqArtefactElement.getArtefactSubElements();
			if (reqArtefactElement.getType().equalsIgnoreCase("Class")) {
				umlIterator = artefactMap.entrySet().iterator();
				
				while (umlIterator.hasNext()) {
					Map.Entry pairs1 = umlIterator.next();
					ArtefactElement umlArtefactElement = (ArtefactElement) pairs1
							.getValue();
					LevenshteinDistance.printDistance(
							umlArtefactElement.getName(), name);
					if (umlArtefactElement.getType().equalsIgnoreCase("Class")
							&& (umlArtefactElement.getName().equalsIgnoreCase(
									name) || LevenshteinDistance.printDistance(
									umlArtefactElement.getName(), name) > 0.6)) {
						count++;

						// get last 3 characters because of the id was add with
						// generated unique id
						relationNodes.add(reqArtefactElement
								.getArtefactElementId().substring(
										reqArtefactElement
												.getArtefactElementId()
												.length() - 3));
						relationNodes.add(umlArtefactElement
								.getArtefactElementId());
						if (CompareWindow.table != null
								&& !CompareWindow.table.isDisposed()) {
							tableItem = new TableItem(CompareWindow.table,
									SWT.NONE);
							tableItem.setText(umlArtefactElement.getName());
							List<ArtefactSubElement> UMLAttributeElements = umlArtefactElement.getArtefactSubElements();
							ArrayList<String> attributesList = new ArrayList<String>();
							ArrayList<String> methodsList = new ArrayList<String>();
							for (int i = 0; i < UMLAttributeElements.size(); i++) {
								ArtefactSubElement UMLAttribute = UMLAttributeElements
										.get(i);
								for (int j = 0; j < reqAttributeElements.size(); j++) {
									ArtefactSubElement sourceElement = reqAttributeElements
											.get(j);
									if (UMLAttribute.getName().equalsIgnoreCase(
											sourceElement.getName())) {
										relationNodes.add(UMLAttribute
												.getSubElementId());
										relationNodes.add(sourceElement
												.getSubElementId());

										if ((sourceElement.getType())
												.equalsIgnoreCase("Field"))
											attributesList.add(sourceElement
													.getName());

										else if ((sourceElement.getType())
												.equalsIgnoreCase("Method"))
											methodsList
													.add(sourceElement.getName());

										UMLAttributeElements.remove(UMLAttribute);
										reqAttributeElements
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
								if (k < attributesList.size())
									tableItem.setText(1, attributesList.get(k));
								if (k < methodsList.size())
									tableItem.setText(2, methodsList.get(k));
								tableItem = new TableItem(CompareWindow.table,
										SWT.NONE);
							}
							if (UMLAttributeElements.size() > 0
									|| reqAttributeElements.size() > 0) {
								if (UMLAttributeElements.size() > 0) {
									if(composite_1 == null)
									composite_1 = new Composite(
											CompareWindow.tabFolder_1, SWT.NONE);
									composite_1.setLayout(new FillLayout());
								
									if(text_1 == null)
										text_1 = new StyledText(composite_1,
											SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
													| SWT.H_SCROLL);
									text_1.append("UMLArtefactFile has following different attributes in "
											+ umlArtefactElement.getName() + "\n");
									for (ArtefactSubElement model : UMLAttributeElements)
										text_1.append((model.getName()) + "\n");
									
								}

								if (reqAttributeElements.size() > 0) {
									if(composite_2 == null)
										composite_2 = new Composite(
											CompareWindow.tabFolder_2, SWT.NONE);
									composite_2.setLayout(new FillLayout());
									
									if(text_2 == null)
									text_2 = new StyledText(composite_2,
											SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
													| SWT.H_SCROLL);
									text_2.append("RequirementArtefactFile has following different attributes in "
											+ reqArtefactElement.getName()
											+ "\n");
									for (ArtefactSubElement model : reqAttributeElements)
										text_2.append((model.getName()) + "\n");
									
								}
							}
						}
						artefactMap.remove(umlArtefactElement
								.getArtefactElementId());
						reqMap.remove(umlArtefactElement.getArtefactElementId());
						requirementIterator = reqMap.entrySet().iterator();
						break;
					}

				}
			}
		}

		if (artefactMap.size() > 0 || reqMap.size() > 0) {
			requirementIterator = reqMap.entrySet().iterator();
			umlIterator = artefactMap.entrySet().iterator();
			while (requirementIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = requirementIterator
						.next();
				if(CompareWindow.tabFolder_2 != null){
					if(composite_2 == null)
						composite_2 = new Composite(
							CompareWindow.tabFolder_2, SWT.NONE);
					composite_2.setLayout(new FillLayout());
				
					if(text_2 == null)
						text_2 = new StyledText(composite_2,
							SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
									| SWT.H_SCROLL);
					text_2.append("RequirementArtefactFile has following different classes from UMLArtefactFile: \n"
							+ artefact.getValue().getName() + "\n");
				}
			}
			
			while (umlIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = umlIterator
						.next();
				if(CompareWindow.tabFolder_1 != null){
					if(composite_1 == null)
						composite_1 = new Composite(
							CompareWindow.tabFolder_1, SWT.NONE);
					composite_1.setLayout(new FillLayout());
				
					if(text_1 == null)
						text_1 = new StyledText(composite_1,
							SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
									| SWT.H_SCROLL);
					text_1.append("UMLArtefactFile has following different classes from requirement ArtefactFile: \n"
							+ artefact.getValue().getName() + "\n");
				}
			}
		}
		
		if(composite_1 != null && text_1 != null){
			composite_1.setData(text_1);
			CompareWindow.tabItem_1.setControl(composite_1);
		}
		
		if(composite_2 != null && text_2 != null){
			composite_2.setData(text_2);
			CompareWindow.tabItem_2.setControl(composite_2);
		}


		return relationNodes;
	}

	@SuppressWarnings("rawtypes")
	public static int compareClassCount() {
		SourceCodeArtefactManager.readXML(projectPath);
		RequirementsManger.readXML(projectPath);
		Iterator it = UMLArtefactManager.UMLAretefactElements.entrySet()
				.iterator();
		int countUMLClass = 0;
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			if (artefactElement.getType().equalsIgnoreCase("Class")) {

				countUMLClass++;
			}
			List<ArtefactSubElement> artefactSubElements = artefactElement
					.getArtefactSubElements();
			it.remove(); // avoids a ConcurrentModificationException
		}
		// UMLArtefactManager.readXML();
		Iterator it1 = RequirementsManger.requirementArtefactElements
				.entrySet().iterator();
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

		if (countUMLClass == countReqClass) {
			System.out.println("class compared");
		}
		return countUMLClass;
	}

}
