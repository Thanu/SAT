/**
 * 
 */
package com.project.traceability.manager;

import java.util.ArrayList;

import com.project.traceability.model.ArtefactElement;


/**
 * @author Gitanjali
 * Dec 1, 2014
 */
public class EditManager {
	
	public static void main(String args[]){
		//addLink("Customer", "accountNumber");
	}
	
	public static void addLink(ArtefactElement className, ArtefactElement subElementName){
		System.out.println(className + " " +subElementName);
		//ArrayList<String> relations = new ArrayList<String>();
		ReadXML.relationNodes.add(className.getArtefactElementId());
		ReadXML.relationNodes.add(subElementName.getArtefactElementId());
		RelationManager.createXML(ReadXML.relationNodes);
	}

}
