package com.project.traceability.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



import com.project.traceability.ir.LevenshteinDistance;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;

public class RequirementUMLClassManager {

	List<String> umlClasses = new ArrayList<String>();
	List<String> requirementClasses = new ArrayList<String>();
	static List<String> relationNodes = new ArrayList<String>();
	
	static String projectPath;

	/**
	 * check whether the requirement classes are implemented in UML
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> compareClassNames(String projectPath) {
		UMLArtefactManager.readXML(projectPath);
		RequirementsManger.readXML(projectPath);
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
			if (reqArtefactElement.getType().equalsIgnoreCase("Class")) {
				umlIterator = artefactMap.entrySet().iterator();
				while (umlIterator.hasNext()) {
					Map.Entry pairs1 = umlIterator.next();
					ArtefactElement umlArtefactElement = (ArtefactElement) pairs1
							.getValue();
					LevenshteinDistance.printDistance(umlArtefactElement.getName(), name);
					if (umlArtefactElement.getType().equalsIgnoreCase(
							"Class")
							&& (umlArtefactElement.getName()
									.equalsIgnoreCase(name) ||LevenshteinDistance.printDistance(umlArtefactElement.getName(), name)>0.6))  {
						count++;
						
						//get last 3 characters because of the id was add with generated unique id
						relationNodes.add(reqArtefactElement
								.getArtefactElementId().substring(reqArtefactElement
								.getArtefactElementId().length()-3));
						relationNodes.add(umlArtefactElement
								.getArtefactElementId());
						artefactMap.remove(umlArtefactElement
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
			umlIterator = artefactMap.entrySet().iterator();
			System.out
					.println("requierment ArtefactFile has following different classes from UMLArtefactFile:");
			while (requirementIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = requirementIterator
						.next();
				System.out.println(artefact.getValue().getName());
			}
			System.out
					.println("UMLArtefactFile has following different classes from requirement ArtefactFile:");
			while (umlIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = umlIterator
						.next();
				System.out.println(artefact.getValue().getName());
			}
		}

		return relationNodes;
	}

	@SuppressWarnings("rawtypes")
	public static int compareClassCount() {
		SourceCodeArtefactManager.readXML(projectPath);
		RequirementsManger.readXML(projectPath);
		Iterator it = UMLArtefactManager.UMLAretefactElements
				.entrySet().iterator();
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

		if (countUMLClass == countReqClass) {
			System.out.println("class compared");
		}
		return countUMLClass;
	}

}
