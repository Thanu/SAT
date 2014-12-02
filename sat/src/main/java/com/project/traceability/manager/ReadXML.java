package com.project.traceability.manager;

import java.util.List;
import java.util.Map;
import com.project.traceability.db.GraphDB;
import com.project.traceability.db.VisualizeGraph;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.RequirementModel;

public class ReadXML {

	public static void initApp(String projectPath) {//main(String[] args){
		
		//String projectPath = "D:/SATWork/def/";
		
		try {
			ReadFiles.readFiles(projectPath);
			Map<String, ArtefactElement> UMLAretefactElements = UMLArtefactManager.UMLAretefactElements;
			Map<String, ArtefactElement> sourceCodeAretefactElements = SourceCodeArtefactManager.sourceCodeAretefactElements;
			List<RequirementModel> requirementsAretefactElements = RequirementsManger.requirementElements;

			GraphDB graphDB = new GraphDB();
			graphDB.initiateGraphDB();
			graphDB.addNodeToGraphDB(sourceCodeAretefactElements);
			graphDB.addNodeToGraphDB(UMLAretefactElements);
			graphDB.addRequirementsNodeToGraphDB(requirementsAretefactElements);

			List<String> relationNodes = UMLSourceClassManager
					.compareClassNames(projectPath);
			graphDB.addRelationTOGraphDB(relationNodes);

			// trace class links between requirement & source code
			List<String> reqSrcRelationNodes = RequirementSourceClassManager
					.compareClassNames(projectPath);
			for (int i = 0; i < reqSrcRelationNodes.size(); i++) {
				System.out.println(reqSrcRelationNodes.get(i));
			}

			graphDB.addRelationTOGraphDB(reqSrcRelationNodes);

			List<String> reqUMLRelationNodes = RequirementUMLClassManager
					.compareClassNames(projectPath);
			for (int i = 0; i < reqUMLRelationNodes.size(); i++) {
				System.out.println(reqUMLRelationNodes.get(i));
			}
			graphDB.addRelationTOGraphDB(reqUMLRelationNodes);

			relationNodes.addAll(reqSrcRelationNodes);
			relationNodes.addAll(reqUMLRelationNodes);

			List<String> UMLSourceAttributeRelation = AttributeManager.mapAttributes(projectPath);
			relationNodes.addAll(UMLSourceAttributeRelation);
			System.out.println("-------------------UMLSourceAttributeRelation--------------------------");
			for (int i = 0; i < UMLSourceAttributeRelation.size(); i++) {
				System.out.println(UMLSourceAttributeRelation.get(i));
			}
			graphDB.addRelationTOGraphDB(UMLSourceAttributeRelation);
			
			List<String> UMLSourceMethodRelation = MethodManager.mapAttributes(projectPath);
			relationNodes.addAll(UMLSourceMethodRelation);
			System.out.println("-------------------UMLSourceMethodRelation--------------------------");
			for (int i = 0; i < UMLSourceMethodRelation.size(); i++) {
				System.out.println(UMLSourceMethodRelation.get(i));
			}
			graphDB.addRelationTOGraphDB(UMLSourceMethodRelation);
			
			List<String> ReqSourceAttributeRelation = RequirementSourceCodeAttributeManager.mapAttributes(projectPath);
			relationNodes.addAll(ReqSourceAttributeRelation);
			System.out.println("-------------------ReqSourceAttributeRelation--------------------------");
			for (int i = 0; i < ReqSourceAttributeRelation.size(); i++) {
				System.out.println(ReqSourceAttributeRelation.get(i));
			}
			graphDB.addRelationTOGraphDB(ReqSourceAttributeRelation);
			
			List<String> ReqSourceMethodRelation = RequirementSourceCodeMethodManager.mapAttributes(projectPath);
			relationNodes.addAll(ReqSourceMethodRelation);
			System.out.println("-------------------ReqSourceMethodRelation--------------------------");
			for (int i = 0; i < ReqSourceMethodRelation.size(); i++) {
				System.out.println(ReqSourceMethodRelation.get(i));
			}
			graphDB.addRelationTOGraphDB(ReqSourceMethodRelation );
			
			List<String> ReqUMLAttributeRelation = RequirementUMLAttributeManager.mapAttributes(projectPath);
			relationNodes
					.addAll(ReqUMLAttributeRelation);
			System.out.println("-------------------ReqUMLAttributeRelation--------------------------");
			for (int i = 0; i < ReqUMLAttributeRelation.size(); i++) {
				System.out.println(ReqUMLAttributeRelation.get(i));
			}
			graphDB.addRelationTOGraphDB(ReqUMLAttributeRelation);
			
			List<String> ReqUMLMethodRelation = RequirementUMLMethodManager.mapAttributes(projectPath);
			relationNodes.addAll(ReqUMLMethodRelation);
			System.out.println("-------------------ReqUMLMethodRelation--------------------------");
			for (int i = 0; i < ReqUMLMethodRelation.size(); i++) {
				System.out.println(ReqUMLMethodRelation.get(i));
			}
			graphDB.addRelationTOGraphDB(ReqUMLMethodRelation);
			
			RelationManager.createXML(relationNodes);
			
			graphDB.generateGraphFile();

			VisualizeGraph visual = new VisualizeGraph();
			visual.script();


			relationNodes.addAll(AttributeManager.mapAttributes(projectPath));
			relationNodes.addAll(MethodManager.mapAttributes(projectPath));

			relationNodes.addAll(RequirementSourceCodeAttributeManager
					.mapAttributes(projectPath));
			relationNodes.addAll(RequirementSourceCodeMethodManager
					.mapAttributes(projectPath));
			relationNodes
					.addAll(RequirementUMLAttributeManager.mapAttributes(projectPath));
			relationNodes.addAll(RequirementUMLMethodManager.mapAttributes(projectPath));
			RelationManager.createXML(relationNodes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}