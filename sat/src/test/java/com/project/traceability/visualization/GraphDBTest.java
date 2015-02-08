/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.ReadFiles;
import com.project.traceability.manager.RequirementsManger;
import com.project.traceability.manager.SourceCodeArtefactManager;
import com.project.traceability.manager.UMLArtefactManager;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.RequirementModel;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.tooling.GlobalGraphOperations;

/**
 *
 * @author Thanu
 */
public class GraphDBTest {

    GraphDatabaseService graphDb;
    ArtefactElement sourceElement, UMLElement;
    ArtefactSubElement fieldSubElement, methodSubElement, attributeSubElement, operationSubElement;
    RequirementModel requirement;
    Map<String, ArtefactElement> aretefactElements;
    List<RequirementModel> reqModel;
    GraphDB graphDB;
    List<String> relation = new ArrayList<>();
    Map<String, ArtefactElement> UMLAretefactElements;
    Map<String, ArtefactElement> sourceCodeAretefactElements;
    List<RequirementModel> requirementsAretefactElements;
    String projectPath;
    String projectName;

    public GraphDBTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(PropertyFile.getTestDb()).newGraphDatabase();

//        sourceElement = new ArtefactElement("SC1", "Account", "Class", "public", null);
//        fieldSubElement = new ArtefactSubElement("SC2", "accountNumber", "private", null, "Field");
//        methodSubElement = new ArtefactSubElement("SC3", "getAccountNumber", "private", null, "Method");
//        List<ArtefactSubElement> sourceSubElements = new ArrayList<>();
//        sourceSubElements.add(fieldSubElement);
//        sourceSubElements.add(methodSubElement);
//        sourceElement.setArtefactSubElements(sourceSubElements);
//
//        UMLElement = new ArtefactElement("D1", "Account", "Class", "public", null);
//        attributeSubElement = new ArtefactSubElement("D2", "accountNumber", "private", null, "UMLAttribute");
//        operationSubElement = new ArtefactSubElement("D3", "getAccountNumber", "private", null, "UMLOperation");
//        List<ArtefactSubElement> UMLSubElements = new ArrayList<>();
//        UMLSubElements.add(attributeSubElement);
//        UMLSubElements.add(operationSubElement);
//        sourceElement.setArtefactSubElements(UMLSubElements);
//
//        aretefactElements = new HashMap<>();
//        aretefactElements.put(sourceElement.getArtefactElementId(), sourceElement);
//        aretefactElements.put(UMLElement.getArtefactElementId(), UMLElement);
//
//        requirement = new RequirementModel("RQ3", "R3", "Account details", "The system shall store account details, such as account number, balance, overdraft limit (current account), interest rate (savings account), withdrawal fee (savings account).", "High", "Functional");
//        reqModel = new ArrayList<>();
//        reqModel.add(requirement);
        projectPath = "E:/SATWork/test/abc";
        projectName = "test";

        PropertyFile.setProjectName(projectName);

        ReadFiles.readFiles(projectPath);
        UMLAretefactElements = UMLArtefactManager.UMLAretefactElements;
        sourceCodeAretefactElements = SourceCodeArtefactManager.sourceCodeAretefactElements;
        requirementsAretefactElements = RequirementsManger.requirementElements;

        graphDB = new GraphDB();
        graphDB.graphDb = graphDb;
    }

    @After
    public void tearDown() {
        graphDb.shutdown();
    }

    /**
     * Test of addNodeToGraphDB method, of class GraphDB.
     */
    @Test
    public void testAddNodeToGraphDB() {
        System.out.println("addNodeToGraphDB");

        Transaction tx = graphDb.beginTx();
        try {
            graphDB.addNodeToGraphDB(UMLAretefactElements);//aretefactElements);
            IndexManager index = graphDb.index();
            Index<Node> artefacts = index.forNodes("ArtefactElement");

            IndexHits<Node> first = artefacts.get("ID", sourceElement.getArtefactElementId());
            Node firstNode = first.getSingle();
            assertThat(firstNode.getId(), is(Long.parseLong(sourceElement.getArtefactElementId())));
            assertThat((String) firstNode.getProperty("Name"), is("Account"));
            assertThat((String) firstNode.getProperty("Type"), is("Class"));
            assertThat((String) firstNode.getProperty("Visibility"), is("public"));

            IndexHits<Node> second = artefacts.get("ID", operationSubElement.getSubElementId());
            Node secondNode = second.getSingle();//graphDb.getNodeById(Long.parseLong(operationSubElement.getSubElementId()));
            assertThat(secondNode.getId(), is(Long.parseLong(operationSubElement.getSubElementId())));
            assertThat((String) secondNode.getProperty("Name"), is("getAccountNumber"));
            assertThat((String) secondNode.getProperty("Type"), is("UMLOperation"));
            assertThat((String) secondNode.getProperty("Visibility"), is("private"));
            assertEquals(6, IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllNodes()));

        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.finish();
        }

    }

    /**
     * Test of addRelationTOGraphDB method, of class GraphDB.
     */
    @Test
    public void testAddRelationTOGraphDB() {
        System.out.println("addRelationTOGraphDB");

        relation.add(UMLElement.getArtefactElementId());
        relation.add("UMLClassToSourceClass");
        relation.add(sourceElement.getArtefactElementId());

        relation.add(attributeSubElement.getSubElementId());
        relation.add("UMLAttributeToSourceField");
        relation.add(fieldSubElement.getSubElementId());

        relation.add(operationSubElement.getSubElementId());
        relation.add("UMLOperationToSourceMethod");
        relation.add(methodSubElement.getSubElementId());
        Transaction tx = graphDb.beginTx();
        try {
            graphDB.addNodeToGraphDB(aretefactElements);
            graphDB.addRelationTOGraphDB(relation);
            assertEquals(10, IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllRelationships()));
        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.finish();
        }

    }

    /**
     * Test of addIntraRelationTOGraphDB method, of class GraphDB.
     */
    @Test
    public void testAddIntraRelationTOGraphDB() {
        System.out.println("addIntraRelationTOGraphDB");

        relation.add(fieldSubElement.getSubElementId());
        relation.add("GetterMethod");
        relation.add(methodSubElement.getSubElementId());

        relation.add(attributeSubElement.getSubElementId());
        relation.add("GetterMethod");
        relation.add(operationSubElement.getSubElementId());

        Transaction tx = graphDb.beginTx();
        try {
            graphDB.addNodeToGraphDB(aretefactElements);
            graphDB.addRelationTOGraphDB(relation);
            graphDB.addIntraRelationTOGraphDB(relation);
            assertEquals(12, IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllRelationships()));
        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.finish();
        }

    }

    /**
     * Test of addRequirementsNodeToGraphDB method, of class GraphDB.
     */
    @Test
    public void testAddRequirementsNodeToGraphDB() {
        System.out.println("addRequirementsNodeToGraphDB");
        Transaction tx = graphDb.beginTx();
        try {
            graphDB.addNodeToGraphDB(aretefactElements);
            graphDB.addRequirementsNodeToGraphDB(reqModel);
            IndexManager index = graphDb.index();
            Index<Node> artefacts = index.forNodes("ArtefactElement");

            IndexHits<Node> hits = artefacts.get("ID", requirement.getRequirementId());
            Node firstNode = hits.getSingle();
            assertThat(firstNode.getId(), is(Long.parseLong(requirement.getRequirementId())));
            assertThat((String) firstNode.getProperty("Name"), is("R3"));
            assertThat((String) firstNode.getProperty("Type"), is("Functional"));
            assertThat((String) firstNode.getProperty("Title"), is("Account details"));
            assertThat((String) firstNode.getProperty("Content"), is("The system shall store account details, such as account number, balance, overdraft limit (current account), interest rate (savings account), withdrawal fee (savings account)."));
            assertThat((String) firstNode.getProperty("Priority"), is("High"));

            assertEquals(7, IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllNodes()));

        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.finish();
        }
    }

    /**
     * Test of generateGraphFile method, of class GraphDB.
     */
    @Test
    public void testGenerateGraphFile() {
        System.out.println("generateGraphFile");
        PropertyFile.setProjectName("Test");
        PropertyFile.setGeneratedGexfFilePath(PropertyFile.getTestGraphFile());
        graphDB.generateGraphFile();
        File f = new File(PropertyFile.getTestGraphFile());
        assertTrue(f.exists());
        PropertyFile.setGeneratedGexfFilePath(null);
    }
}
