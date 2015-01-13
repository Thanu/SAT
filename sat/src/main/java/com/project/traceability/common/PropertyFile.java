/**
 * 
 */
package com.project.traceability.common;

import com.project.traceability.GUI.NewProjectWindow;
import com.project.traceability.db.VisualizeGraph;

/**13 Nov 2014
 * @author K.Kamalan
 *
 */
public class PropertyFile {
	public static final String requirementXMLPath = "E:/ATOM/RequirementArtefactFile.xml";
	public static final String umlXMLPath = "E:/ATOM/UMLArtefactFile.xml";
	public static final String sourceXMLPath = "E:/ATOM/SourceCodeArtefactFile.xml";
	
	public static final String wordNetDbDirectory = "E:/Programs/WordNet/dict";
	public static final String filePath = "E:/SATWork/";
	public static final String xmlFilePath = "E:/ATOM";
        public static final String imagePath = "E:/SAT/sat/img";		//img folder in project
        public static String projectName  = null; 
        public static String graphDbPath = null;//NewProjectWindow.projectPath+"/"+projectName+".graphdb";
	public static String generatedGexfFilePath = null;//NewProjectWindow.projectPath+"/"+projectName+".gexf";
        public static String relationshipXMLPath = null;//"E:/ATOM/createFile.xml";
        public static String graphType = null;
        public static VisualizeGraph visual = null;
	

    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String projectName) {
        PropertyFile.projectName = projectName;
    }

    public static String getGraphDbPath() {
        return graphDbPath;
    }

    public static void setGraphDbPath(String graphDbPath) {
        PropertyFile.graphDbPath = graphDbPath;
    }

    public static String getGeneratedGexfFilePath() {
        return generatedGexfFilePath;
    }

    public static void setGeneratedGexfFilePath(String generatedGexfFilePath) {
        PropertyFile.generatedGexfFilePath = generatedGexfFilePath;
    }

    public static String getRelationshipXMLPath() {
        return relationshipXMLPath;
    }

    public static void setRelationshipXMLPath(String relationshipXMLPath) {
        PropertyFile.relationshipXMLPath = relationshipXMLPath;
    }

    public static String getGraphType() {
        return graphType;
    }

    public static void setGraphType(String graphType) {
        PropertyFile.graphType = graphType;
    }

    public static VisualizeGraph getVisual() {
        return visual;
    }

    public static void setVisual(VisualizeGraph visual) {
        PropertyFile.visual = visual;
    }
       
    
}

