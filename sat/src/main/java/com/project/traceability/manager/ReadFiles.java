/**
 * 
 */
package com.project.traceability.manager;

/**
 * @author Gitanjali
 * Nov 19, 2014
 */
public class ReadFiles {
	public static void readFiles(String projectPath){
		SourceCodeArtefactManager.readXML(projectPath);
		UMLArtefactManager.readXML(projectPath);
		RequirementsManger.readXML(projectPath);

	}
	
}
