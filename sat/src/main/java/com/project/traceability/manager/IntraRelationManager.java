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
    static List<String> relationNodes1 = new ArrayList<String>();
    static List<String> relationNodes2 = new ArrayList<String>();

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
    
    public static List<String> getSourceIntraRelation(String projectPath) {
        SourceCodeArtefactManager.readXML(projectPath);
        Map<String, ArtefactElement> sourceMap = SourceCodeArtefactManager.sourceCodeAretefactElements;
        Iterator<Map.Entry<String, ArtefactElement>> sourceIterator = null;

        while (sourceIterator.hasNext()) {
            Map.Entry pairs1 = sourceIterator.next();
            ArtefactElement sourceArtefactElement = (ArtefactElement) pairs1
                    .getValue();
            List<ArtefactSubElement> sourceAttributeElements = sourceArtefactElement
                    .getArtefactSubElements();
            for (int i = 0; i < sourceAttributeElements.size() && sourceAttributeElements.get(i).getType().equalsIgnoreCase("Field"); i++) {
                int count = 0;
                for (int j = 0; j < sourceAttributeElements.size(); j++) {
                    if (sourceAttributeElements.get(j).getType().equalsIgnoreCase("Method")) {
                        if (sourceAttributeElements.get(i).getName().equalsIgnoreCase(sourceAttributeElements.get(j).getName().substring(3))) {
                            count++;
                            if (sourceAttributeElements.get(j).getName().contains("get")) {
                                relationNodes1.add(sourceAttributeElements.get(i).getName());
                                relationNodes1.add("-Getter Method-");
                                relationNodes1.add(sourceAttributeElements.get(j).getName());
                            } else {
                                relationNodes1.add(sourceAttributeElements.get(i).getName());
                                relationNodes1.add("-Setter Method-");
                                relationNodes1.add(sourceAttributeElements.get(j).getName());

                            }
                            if (count == 2) {
                                break;
                            }
                        }
                    }
                }

            }

        }
        
        return relationNodes1;
    }
}
