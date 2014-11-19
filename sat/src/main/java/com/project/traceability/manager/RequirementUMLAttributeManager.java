package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.project.traceability.ir.LevenshteinDistance;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.utils.Constants.ArtefactSubElementType;

/**19 Nov 2014
 * @author K.Kamalan
 *
 */
public class RequirementUMLAttributeManager {
	
	static List<String> relationNodes = new ArrayList<String>();
	static Map<ArtefactElement, List<? extends ArtefactSubElement>> reqSubArtefacts = RequirementsManger
			.manageArtefactSubElements(ArtefactSubElementType.ATTRIBUTE);			//get read source code attribute artifacts
	static Map<ArtefactElement, List<ArtefactSubElement>> UMLSubArtefacts = UMLArtefactManager
			.manageArtefactSubElements(ArtefactSubElementType.ATTRIBUTE);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> mapAttributes() {
		SourceCodeArtefactManager.readXML();
		UMLArtefactManager.readXML();
		Map<ArtefactElement, List<? extends ArtefactSubElement>> reqAttributeArtefactMap = reqSubArtefacts;
		Map<ArtefactElement, List<ArtefactSubElement>> UMLattributeArtefactMap = UMLSubArtefacts;
		Iterator<Entry<ArtefactElement, List<ArtefactSubElement>>> UMLIterator = UMLattributeArtefactMap
				.entrySet().iterator();

		while (UMLIterator.hasNext()) {
			Map.Entry UMLPairs = UMLIterator.next();
			ArtefactElement UMLArtefactElement = (ArtefactElement) UMLPairs
					.getKey();
			List<AttributeModel> UMLAttributeElements = (List<AttributeModel>) UMLPairs
					.getValue();
			Iterator<Entry<ArtefactElement, List<? extends ArtefactSubElement>>> reqIterator = 
					reqAttributeArtefactMap.entrySet().iterator();
			while (reqIterator.hasNext()) {
				Map.Entry reqPairs = reqIterator.next();
				ArtefactElement reqArtefactElement = (ArtefactElement) reqPairs
						.getKey();
				List<AttributeModel> reqAttributeElements = (List<AttributeModel>) reqPairs
						.getValue();
				if (reqArtefactElement.getName().equalsIgnoreCase(UMLArtefactElement.getName())) {
					for (int i = 0; i < UMLAttributeElements.size(); i++) {
						AttributeModel UMLAttribute = UMLAttributeElements.get(i);
						for (int j = 0; j < reqAttributeElements.size(); j++) {
							AttributeModel reqAttribute = reqAttributeElements.get(j);
							//System.out.println(UMLAttribute.getSubElementId()+"@@@@@@@@@@@@"+reqAttribute.getSubElementId());
							if (UMLAttribute.getName().equalsIgnoreCase(reqAttribute.getName())||LevenshteinDistance.similarity(UMLAttribute.getName(), reqAttribute.getName())>.6) {
								relationNodes.add(reqAttribute.getSubElementId().substring(reqAttribute.getSubElementId().length()-3));
								relationNodes.add(UMLAttribute.getSubElementId());
								UMLAttributeElements.remove(UMLAttribute);
								reqAttributeElements.remove(reqAttribute);
								i--; j--;
								break;
							}
						}
					}
					if(UMLAttributeElements.size() > 0 || reqAttributeElements.size() > 0) {
						System.out.println("There are some conflicts among attributes in "+ reqArtefactElement.getName() 
									+ " class.");
						if (UMLAttributeElements.size() > 0) {
							System.out.println("UMLArtefactFile has following different attributes in " 
										+ UMLArtefactElement.getName());
							for(AttributeModel model : UMLAttributeElements)
								System.out.println(model.getName());
						}
						
						if (reqAttributeElements.size() > 0) {
							System.out.println("Requirement ArtefactFile has following different attributes in " 
									+ reqArtefactElement.getName());
							for(AttributeModel model : reqAttributeElements)
								System.out.println(model.getName());
						}
					}
				}
			}
			UMLIterator.remove();
		}
		// RelationManager.createXML(relationNodes);
		return relationNodes;
	}

}
