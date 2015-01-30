package com.project.traceability.manager;

import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.common.PropertyFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.project.traceability.db.GraphDB;
import com.project.traceability.db.VisualizeGraph;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.RequirementModel;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.openide.util.Lookup;

public class ReadXML {

    public static List<String> relationNodes = new ArrayList<String>();

    public static void initApp(String projectPath, String graphType) {//main(String[] args){

        relationNodes = null;
        try {
        	HomeGUI.isComaparing = false;
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
            relationNodes = UMLSourceClassManager
                    .compareClassNames(projectPath);
            graphDB.addRelationTOGraphDB(relationNodes);

            // trace class links between requirement & source code
            List<String> reqSrcRelationNodes = RequirementSourceClassManager
                    .compareClassNames(projectPath);
            graphDB.addRelationTOGraphDB(reqSrcRelationNodes);

            List<String> reqUMLRelationNodes = RequirementUMLClassManager
                    .compareClassNames(projectPath);
            graphDB.addRelationTOGraphDB(reqUMLRelationNodes);

            relationNodes.addAll(reqSrcRelationNodes);
            relationNodes.addAll(reqUMLRelationNodes);
           
            List<String> reqIntraRelations = IntraRelationManager.getReqIntraRelation(projectPath);
            System.out.println("Req Intra Relation: "+ reqIntraRelations.size());
            graphDB.addIntraRelationTOGraphDB(reqIntraRelations);
            relationNodes.addAll(reqIntraRelations);
            
            List<String> sourceIntraRelations = IntraRelationManager.getSourceIntraRelation(projectPath);
            System.out.println("Source Intra Relation: "+ sourceIntraRelations.size());
            graphDB.addIntraRelationTOGraphDB(sourceIntraRelations);
            relationNodes.addAll(sourceIntraRelations);
            
            List<String> UMLIntraRelations = IntraRelationManager.getUMLIntraRelation(projectPath);
            System.out.println("UML Intra Relation: "+ UMLIntraRelations.size());
            graphDB.addIntraRelationTOGraphDB(UMLIntraRelations);
            relationNodes.addAll(UMLIntraRelations);
            
            RelationManager.addLinks(relationNodes);

            graphDB.generateGraphFile();

            VisualizeGraph visual = new VisualizeGraph();
            visual.importFile();
            GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();
            visual.setGraph(model, PropertyFile.graphType);
            visual.showGraph();
            visual.addPanel(visual.getApplet(), visual.getComposite(), visual.getTabItem());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}