/**
 * 
 */
package com.project.traceability.manager;


/**
 * @author Gitanjali
 * Dec 1, 2014
 */
public class EditManager {
	
	public static void main(String args[]){
		addLink("Customer", "accountNumber");
	}
	
	public static void addLink(String className, String subElementName){
		System.out.println(className + " " +subElementName);
		//ArrayList<String> relations = new ArrayList<String>();
		//ReadXML.relationNodes.add(className);
		//ReadXML.relationNodes.add(subElementName);
		//RelationManager.createXML(ReadXML.relationNodes);
	}

}
