package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.project.traceability.db.GraphDB;
import com.project.traceability.db.VisualizeGraph;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.RequirementModel;

public class ReadXML {

	public static List<String> relationNodes = new ArrayList<String>();
	public static void initApp(String projectPath, String graphType) {//main(String[] args){
		
		
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


			// trace class links between UML & source code

			//List<String> relationNodes = UMLSourceClassManager
				//	.compareClassNames(projectPath);
//			for (int i = 0; i < relationNodes .size(); i++) {
//				System.out.println(relationNodes .get(i));
//			}
			graphDB.addRelationTOGraphDB(relationNodes);

			// trace class links between requirement & source code
			List<String> reqSrcRelationNodes = RequirementSourceClassManager
					.compareClassNames(projectPath);
//			for (int i = 0; i < reqSrcRelationNodes.size(); i++) {
//				System.out.println(reqSrcRelationNodes.get(i));
//			}
			graphDB.addRelationTOGraphDB(reqSrcRelationNodes);

			List<String> reqUMLRelationNodes = RequirementUMLClassManager
					.compareClassNames(projectPath);
//			for (int i = 0; i < reqUMLRelationNodes.size(); i++) {
//				System.out.println(reqUMLRelationNodes.get(i));
//			}
			graphDB.addRelationTOGraphDB(reqUMLRelationNodes);

			relationNodes.addAll(reqSrcRelationNodes);
			relationNodes.addAll(reqUMLRelationNodes);
			//graphDB.addRelationTOGraphDB(relationNodes);
			
			RelationManager.createXML(relationNodes);
			
			graphDB.generateGraphFile();
                        
			VisualizeGraph visual = new VisualizeGraph();
                        visual.showGraph(graphType);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}