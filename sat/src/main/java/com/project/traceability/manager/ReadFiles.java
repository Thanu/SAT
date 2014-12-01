/**
 * 
 */
package com.project.traceability.manager;

import com.project.traceability.common.DefaultWords;

/**
 * @author Gitanjali
 * Nov 19, 2014
 */
public class ReadFiles {
	public static void readFiles(String projectPath){
		SourceCodeArtefactManager.readXML(projectPath);
		UMLArtefactManager.readXML(projectPath);
		RequirementsManger.readXML(projectPath);
		DefaultWords.getDefaultWords();
	}
	
}
