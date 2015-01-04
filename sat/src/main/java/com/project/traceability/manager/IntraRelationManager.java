/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author K.Kamalan
 */
public class IntraRelationManager {

    static List<String> relationNodes = new ArrayList<String>();

    static String projectPath;

    public static List<String> getReqIntraRelation(String projectPath) {
        IntraRelationManager.projectPath = projectPath;
        RequirementsManger.readXML(projectPath);
        Map<String, ArtefactElement> reqMap = RequirementsManger.requirementArtefactElements;
        Iterator<Map.Entry<String, ArtefactElement>> requirementIterator = reqMap
                .entrySet().iterator();
        while (requirementIterator.hasNext()) {
            Map.Entry pairs = requirementIterator.next();
            ArtefactElement reqArtefactElement = (ArtefactElement) pairs
                    .getValue();
            List<ArtefactSubElement> reqAttributeElements = reqArtefactElement
                    .getArtefactSubElements();
            if (reqArtefactElement.getType().equalsIgnoreCase("Class")) {
                System.out.println(reqArtefactElement.getName() + "%%%%%%%%%%%%%%%%");
                compareSubElements(reqArtefactElement);
            }
        }

        return relationNodes;
    }

    public static void compareSubElements(ArtefactElement reqArtefactElement) {
        List<ArtefactSubElement> reqAttributeElements = reqArtefactElement
                .getArtefactSubElements();
        for (int i = 0; i < reqAttributeElements.size() && reqAttributeElements.get(i).getType().equalsIgnoreCase("Field"); i++) {
            System.out.println("KAMALA" + reqAttributeElements.get(i).getName());
            int count = 0;
            for (int j = 0; j < reqAttributeElements.size(); j++) {
                if (reqAttributeElements.get(j).getType().equalsIgnoreCase("Method")) {
                    if (reqAttributeElements.get(i).getName().equalsIgnoreCase(reqAttributeElements.get(j).getName().substring(3))) {
                        System.out.println(reqAttributeElements.get(i).getName() + "KAMALAN" + reqAttributeElements.get(j).getName());
                        count++;
                        if(reqAttributeElements.get(j).getName().toLowerCase().contains("get")){
                            relationNodes.add(reqAttributeElements.get(i).getName());
                            relationNodes.add("-Getter Method-");
                            relationNodes.add(reqAttributeElements.get(j).getName());
                        }else{
                            relationNodes.add(reqAttributeElements.get(i).getName());
                            relationNodes.add("-Setter Method-");
                            relationNodes.add(reqAttributeElements.get(j).getName());
                        }
                        if (count == 2) {
                            break;
                        }
                    }

                }

            }
        }
    }
}
