package com.project.traceability.manager;

import java.util.List;
import java.util.Map;
import com.project.traceability.db.GraphDB;
import com.project.traceability.db.GraphFileGenerator;
import com.project.traceability.db.VisualizeGraph;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.RequirementModel;


public class ReadXML {

	public static void main(String argv[]) {
	
		try {
			SourceCodeArtefactManager.readXML();
			UMLArtefactManager.readXML();		
			RequirementsManger.readXML();
			
		    Map<String, ArtefactElement> UMLAretefactElements = UMLArtefactManager.UMLAretefactElements;
		    Map<String, ArtefactElement> sourceCodeAretefactElements = SourceCodeArtefactManager.sourceCodeAretefactElements;
		    List<RequirementModel> requirementsAretefactElements = RequirementsManger.requirementElements;
		   		    
		    GraphDB graphDB = new GraphDB();
	        graphDB.initiateGraphDB();
	        graphDB.addNodeToGraphDB(sourceCodeAretefactElements);
		    graphDB.addNodeToGraphDB(UMLAretefactElements);
		    graphDB.addRequirementsNodeToGraphDB(requirementsAretefactElements);
	        
	        //SourceCodeArtefactManager.manageArtefactSubElements();
			List<String> relationNodes = UMLSourceClassManager.compareClassNames();
			//List<String> relationNodes = ClassCompareManager.relationNodes;
			graphDB.addRelationTOGraphDB(relationNodes);
			
					
			//trace class links between requirement & source code
			List<String> reqSrcRelationNodes = RequirementSourceClassManager.compareClassNames();
			for(int i = 0;i<reqSrcRelationNodes.size();i++){
				System.out.println(reqSrcRelationNodes.get(i));
			}
			graphDB.addRelationTOGraphDB(reqSrcRelationNodes);
			
			List<String> reqUMLRelationNodes = RequirementUMLClassManager.compareClassNames();
			for(int i = 0;i<reqUMLRelationNodes.size();i++){
				System.out.println(reqUMLRelationNodes.get(i));
			}
			graphDB.addRelationTOGraphDB(reqUMLRelationNodes);
			
			
			relationNodes.addAll(RequirementSourceClassManager.compareClassNames());
			relationNodes.addAll(RequirementUMLClassManager.compareClassNames());
			
			graphDB.generateGraphFile();		
			
			VisualizeGraph visual = new VisualizeGraph();
			visual.script();
				
			
			//List<String> relationNodes = ClassCompareManager.compareClassNames();
			relationNodes.addAll(AttributeManager.mapAttributes());
			relationNodes.addAll(MethodManager.mapAttributes());
			RelationManager.createXML(relationNodes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	

}
