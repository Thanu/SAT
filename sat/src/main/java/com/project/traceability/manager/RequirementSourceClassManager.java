/**
 * 
 */
package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	/**
	 * check whether the requirement classes are implemented in sourcecode
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> compareClassNames(String projectPath) {
		RequirementSourceClassManager.projectPath = projectPath;
		SourceCodeArtefactManager.readXML(projectPath);
		RequirementsManger.readXML(projectPath);
		Map<String, ArtefactElement> reqMap = RequirementsManger.requirementArtefactElements;
		Iterator<Entry<String, ArtefactElement>> requirementIterator = reqMap
				.entrySet().iterator();
		Map<String, ArtefactElement> artefactMap = SourceCodeArtefactManager.sourceCodeAretefactElements;
		Iterator<Entry<String, ArtefactElement>> sourceIterator = null;
		int count = 0;
		while (requirementIterator.hasNext()) {
			Map.Entry pairs = requirementIterator.next();
			ArtefactElement reqArtefactElement = (ArtefactElement) pairs
					.getValue();
			String name = reqArtefactElement.getName();
			if (reqArtefactElement.getType().equalsIgnoreCase("Class")) {
				sourceIterator = artefactMap.entrySet().iterator();
				while (sourceIterator.hasNext()) {
					Map.Entry pairs1 = sourceIterator.next();
					ArtefactElement sourceArtefactElement = (ArtefactElement) pairs1
							.getValue();
					LevenshteinDistance.printDistance(sourceArtefactElement.getName(), name);
					if (sourceArtefactElement.getType().equalsIgnoreCase(
							"Class")
							&& (sourceArtefactElement.getName()
									.equalsIgnoreCase(name) ||LevenshteinDistance.printDistance(sourceArtefactElement.getName(), name)>0.6))  {
						count++;
						relationNodes.add(reqArtefactElement
								.getArtefactElementId().substring(reqArtefactElement
								.getArtefactElementId().length()-3));
						relationNodes.add(sourceArtefactElement
								.getArtefactElementId());
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
			System.out
					.println("requierment ArtefactFile has following different classes from SourceCodeArtefactFile:");
			while (requirementIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = requirementIterator
						.next();
				System.out.println(artefact.getValue().getName());
			}
			System.out
					.println("SourceCodeArtefactFile has following different classes from requirement ArtefactFile:");
			while (sourceIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = sourceIterator
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
