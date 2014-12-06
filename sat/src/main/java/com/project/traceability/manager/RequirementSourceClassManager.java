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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.GUI.CompareWindow;
import com.project.traceability.GUI.CompareWindow1;
import com.project.traceability.ir.LevenshteinDistance;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.semanticAnalysis.SynonymWords;

/**
 * 13 Nov 2014
 * 
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
			List<ArtefactSubElement> reqAttributeElements = reqArtefactElement
					.getArtefactSubElements();
			if (reqArtefactElement.getType().equalsIgnoreCase("Class")) {
				sourceIterator = artefactMap.entrySet().iterator();
				while (sourceIterator.hasNext()) {
					Map.Entry pairs1 = sourceIterator.next();
					ArtefactElement sourceArtefactElement = (ArtefactElement) pairs1
							.getValue();
					LevenshteinDistance.printDistance(
							sourceArtefactElement.getName(), name);

					// System.out.println(LevenshteinDistance.printDistance(
					// sourceArtefactElement.getName(), name));
					if (sourceArtefactElement.getType().equalsIgnoreCase("Class")
							&& ( sourceArtefactElement.getName().equalsIgnoreCase(name) | SynonymWords.checkSymilarity(
									sourceArtefactElement.getName(), name))) {

						relationNodes.add(reqArtefactElement
								.getArtefactElementId().substring(
										reqArtefactElement
												.getArtefactElementId()
												.length() - 3));
						relationNodes.add(sourceArtefactElement
								.getArtefactElementId());
						if (CompareWindow.tree != null
								&& !CompareWindow.tree.isDisposed()) {

							TreeItem item = new TreeItem(CompareWindow.tree,
									SWT.NONE);
							item.setText(0, sourceArtefactElement.getName());
							item.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE));
							item.setText(1, reqArtefactElement.getName());
							

							List<ArtefactSubElement> sourceAttributeElements = sourceArtefactElement
									.getArtefactSubElements();

							ArrayList<String> reqAttributesList = new ArrayList<String>();
							ArrayList<String> reqMethodsList = new ArrayList<String>();

							ArrayList<String> sourceAttributesList = new ArrayList<String>();
							ArrayList<String> sourceMethodsList = new ArrayList<String>();

							for (int i = 0; i < sourceAttributeElements.size(); i++) {
								ArtefactSubElement sourceAttribute = sourceAttributeElements
										.get(i);
								for (int j = 0; j < reqAttributeElements.size(); j++) {
									ArtefactSubElement requElement = reqAttributeElements
											.get(j);
									if (SynonymWords.checkSymilarity(
											sourceAttribute.getName(),
											requElement.getName())) {
										// if (sourceAttribute.getName()
										// .equalsIgnoreCase(
										// requElement.getName())
										// || LevenshteinDistance.similarity(
										// sourceAttribute.getName(),
										// requElement.getName()) > .6) {
										/*
										 * relationNodes.add(UMLAttribute
										 * .getSubElementId());
										 * relationNodes.add(sourceElement
										 * .getSubElementId());
										 */

										if (requElement.getType()
												.equalsIgnoreCase("Field")) {
											sourceAttributesList
													.add(sourceAttribute
															.getName());
											reqAttributesList
													.add(reqArtefactElement
															.getName());
											relationNodes
													.add(requElement
															.getSubElementId()
															.substring(
																	requElement
																			.getSubElementId()
																			.length() - 3));
											relationNodes.add(sourceAttribute
													.getSubElementId());
										}

										else if ((requElement.getType())
												.equalsIgnoreCase("Method")) {
											sourceMethodsList
													.add(sourceAttribute
															.getName());
											reqMethodsList.add(requElement
													.getName());
											relationNodes
													.add(requElement
															.getSubElementId()
															.substring(
																	requElement
																			.getSubElementId()
																			.length() - 3));
											relationNodes.add(sourceAttribute
													.getSubElementId());
										}

										sourceAttributeElements
												.remove(sourceAttribute);
										reqAttributeElements
												.remove(requElement);
										i--;
										j--;
										break;
									}
								}
							}

							for (int k = 0; k < sourceAttributesList.size(); k++) {
								TreeItem subItem = new TreeItem(item, SWT.NONE);
								subItem.setText(0, sourceAttributesList.get(k));
								subItem.setText(1, sourceAttributesList.get(k));
							}
							
							for (int k = 0; k < sourceMethodsList.size(); k++) {
								TreeItem subItem = new TreeItem(item, SWT.NONE);
								subItem.setText(0, sourceMethodsList.get(k));
								subItem.setText(1, reqMethodsList.get(k));
							}
							
							if (sourceAttributeElements.size() > 0
									|| reqAttributeElements.size() > 0) {
								if (sourceAttributeElements.size() > 0) {
									for (ArtefactSubElement model : sourceAttributeElements){
										TreeItem subItem = new TreeItem(item,
												SWT.NONE);
										subItem.setText(0, model.getName());
										subItem.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
									}

								}

								if (reqAttributeElements.size() > 0) {
									
									for (ArtefactSubElement model : reqAttributeElements){
										TreeItem subItem = new TreeItem(item,
												SWT.NONE);
										subItem.setText(1, model.getName());
										subItem.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
									}

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
			
			while (requirementIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = requirementIterator
						.next();
				if(CompareWindow.tree != null && !CompareWindow.shell.isDisposed()) {
					TreeItem item = new TreeItem(CompareWindow.tree,
							SWT.NONE);
					item.setText(1, artefact.getValue().getName());
					item.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				}
			}
			
			while (sourceIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = sourceIterator
						.next();
				
				if(CompareWindow.tree != null && !CompareWindow.shell.isDisposed()) {
					TreeItem item = new TreeItem(CompareWindow.tree,
						SWT.NONE);
					item.setText(0, artefact.getValue().getName());
					item.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				}
			}
		}

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

		if (countSourceClass == countReqClass) {
			System.out.println("class compared");
		}
		return countSourceClass;
	}

}
