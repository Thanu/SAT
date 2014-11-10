package com.project.traceability.manager;

import java.util.List;
import java.util.Map;
import com.project.traceability.db.GraphDB;
import com.project.traceability.db.PreviewJFrame;
import com.project.traceability.db.VisualizeGraph;
import com.project.traceability.model.ArtefactElement;


public class ReadXML {

	public static void main(String argv[]) {
	
		try {
			SourceCodeArtefactManager.readXML();
			UMLArtefactManager.readXML();		
			
		    Map<String, ArtefactElement> UMLAretefactElements = UMLArtefactManager.UMLAretefactElements;
		    Map<String, ArtefactElement> sourceCodeAretefactElements = SourceCodeArtefactManager.sourceCodeAretefactElements;
		   		    
		    GraphDB graphDB = new GraphDB();
	        graphDB.initiateGraphDB();
	        graphDB.addNodeToGraphDB(sourceCodeAretefactElements);
		    graphDB.addNodeToGraphDB(UMLAretefactElements);
		  
	        
	        //SourceCodeArtefactManager.manageArtefactSubElements();
			List<String> relationNodes = UMLSourceClassManager.compareClassNames();
			//List<String> relationNodes = ClassCompareManager.relationNodes;
			graphDB.addRelationTOGraphDB(relationNodes);
			//graphDB.setVisible(true);
			
						
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
