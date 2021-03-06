/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.IntraRelationManager;
import com.project.traceability.manager.ReadFiles;
import com.project.traceability.manager.RequirementSourceClassManager;
import com.project.traceability.manager.RequirementUMLClassManager;
import com.project.traceability.manager.RequirementsManger;
import com.project.traceability.manager.SourceCodeArtefactManager;
import com.project.traceability.manager.UMLArtefactManager;
import com.project.traceability.manager.UMLSourceClassManager;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.RequirementModel;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	static GraphDatabaseService graphDbService;
	static ArtefactElement sourceElement;
	static ArtefactElement UMLElement;
	static ArtefactSubElement fieldSubElement;
	static ArtefactSubElement methodSubElement;
	static ArtefactSubElement attributeSubElement;
	static ArtefactSubElement operationSubElement;
	static RequirementModel requirement;
	static Map<String, ArtefactElement> aretefactElements;
	static List<RequirementModel> reqModel;
	static GraphDB graphDB;
	static List<String> relation = new ArrayList<>();
	static Map<String, ArtefactElement> UMLAretefactElements;
	static Map<String, ArtefactElement> sourceCodeAretefactElements;
	static List<RequirementModel> requirementsAretefactElements;
	static String filePath;
	static String projectName;
	static GraphDBTest test;
	static String projectPath;

	public GraphDBTest() {
		System.out.println("graphDBTest");
	}

	@BeforeClass
	public static void setUpClass() {
		System.out.println("setupclass");
		ReadFiles.readFiles("E:/SATWork/Test/");
		
		sourceElement = new ArtefactElement("SC1", "Account", "Class",
				"public", null);
		fieldSubElement = new ArtefactSubElement("SC2", "accountNumber",
				"private", null, "Field");
		methodSubElement = new ArtefactSubElement("SC8", "getAccountNumber",
				"private", null, "Method");
		List<ArtefactSubElement> sourceSubElements = new ArrayList<>();
		sourceSubElements.add(fieldSubElement);
		sourceSubElements.add(methodSubElement);
		sourceElement.setArtefactSubElements(sourceSubElements);

		UMLElement = new ArtefactElement("D1", "Account", "Class", "public",
				null);
		attributeSubElement = new ArtefactSubElement("D2", "accountNumber",
				"private", null, "UMLAttribute");
		operationSubElement = new ArtefactSubElement("D7", "getAccountNumber",
				"private", null, "UMLOperation");
		List<ArtefactSubElement> UMLSubElements = new ArrayList<>();
		UMLSubElements.add(attributeSubElement);
		UMLSubElements.add(operationSubElement);
		sourceElement.setArtefactSubElements(UMLSubElements);

		aretefactElements = new HashMap<>();
		aretefactElements.put(sourceElement.getArtefactElementId(),
				sourceElement);
		aretefactElements.put(UMLElement.getArtefactElementId(), UMLElement);

		requirement = new RequirementModel(
				"RQ3",
				"R3",
				"Account details",
				"The system shall store account details, such as account number, balance, overdraft limit (current account), interest rate (savings account), withdrawal fee (savings account).",
				"High", "Functional");
		reqModel = new ArrayList<>();
		reqModel.add(requirement);

		filePath = "E:/SATWork/";
		projectPath = "E:/SATWork/Test/";
		projectName = "Test";
		PropertyFile.setFilePath(filePath);
		PropertyFile.setProjectName(projectName);

		UMLAretefactElements = UMLArtefactManager.UMLAretefactElements;
		sourceCodeAretefactElements = SourceCodeArtefactManager.sourceCodeAretefactElements;
		requirementsAretefactElements = RequirementsManger.requirementElements;

		graphDB = new GraphDB();
		graphDB.graphDb = graphDbService;
		test = new GraphDBTest();
	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println("teardownclass");		
	}

	@Before
	public void setUp() {
		System.out.println("setup");
		graphDbService = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(
				PropertyFile.getTestDb()).newGraphDatabase();
		graphDB.graphDb = graphDbService;
	}

	@After
	public void tearDown() {
		System.out.println("teardown");
		graphDbService.shutdown();
	}

	/**
	 * Test of addNodeToGraphDB method, of class GraphDB.
	 */
	@Test
	public void testAddNodeToGraphDB() {
		System.out.println("addNodeToGraphDB");
		Transaction tx = graphDbService.beginTx();
		try {
			graphDB.addNodeToGraphDB(UMLAretefactElements);

			graphDB.addNodeToGraphDB(sourceCodeAretefactElements);
			IndexManager index = graphDbService.index();
			Index<Node> artefacts = index.forNodes("ArtefactElement");

			IndexHits<Node> first = artefacts.get("ID",
					sourceElement.getArtefactElementId());
			Node firstNode = first.getSingle();
			assertEquals(firstNode.getProperty("ID").toString(),
					sourceElement.getArtefactElementId());
			assertEquals(firstNode.getProperty("Name").toString(),
					sourceElement.getName());
			assertEquals(firstNode.getProperty("Type").toString(),
					sourceElement.getType());
			// assertEquals(firstNode.getProperty("Visibility").toString(),sourceElement.getVisibility());

			IndexHits<Node> second = artefacts.get("ID",
					operationSubElement.getSubElementId());
			Node secondNode = second.getSingle();
			assertEquals(secondNode.getProperty("ID").toString(),
					operationSubElement.getSubElementId());
			assertEquals(secondNode.getProperty("Name").toString(),
					operationSubElement.getName());
			assertEquals(secondNode.getProperty("Type").toString(),
					operationSubElement.getType());
			// assertEquals(secondNode.getProperty("Visibility").toString(),operationSubElement.getVisibility());
			assertEquals(147, IteratorUtil.count(GlobalGraphOperations.at(
					graphDbService).getAllNodes()));

		} catch (Exception e) {
			e.printStackTrace();
			tx.failure();
		} finally {
			tx.success();
		}
	}

	/**
	 * Test of addRelationTOGraphDB method, of class GraphDB.
	 */
	@Test
	public void testAddRelationTOGraphDB() {
		System.out.println("addRelationTOGraphDB");
		HomeGUI.projectPath = projectPath;
		relation = UMLSourceClassManager.compareClassNames(projectPath);
		relation.addAll(RequirementSourceClassManager
				.compareClassNames(projectPath));
		relation.addAll(RequirementUMLClassManager
				.compareClassNames(projectPath));

		Transaction tx = graphDbService.beginTx();
		try {
			test.testAddNodeToGraphDB();
			test.testAddRequirementsNodeToGraphDB();
			graphDB.addRelationTOGraphDB(relation);
			assertEquals(251, IteratorUtil.count(GlobalGraphOperations.at(
					graphDbService).getAllRelationships()));
		} catch (Exception e) {
			tx.failure();
		} finally {
			tx.success();
		}

	}

	/**
	 * Test of addIntraRelationTOGraphDB method, of class GraphDB.
	 */
	@Test
	public void testAddIntraRelationTOGraphDB() {
		System.out.println("addIntraRelationTOGraphDB");
		relation = IntraRelationManager.getSourceIntraRelation(projectPath);
		relation.addAll(IntraRelationManager.getUMLIntraRelation(projectPath));

		Transaction tx = graphDbService.beginTx();
		try {
			test.testAddNodeToGraphDB();
			test.testAddRequirementsNodeToGraphDB();
			test.testAddRelationTOGraphDB();
			graphDB.addIntraRelationTOGraphDB(relation);
			assertEquals(251, IteratorUtil.count(GlobalGraphOperations.at(
					graphDbService).getAllRelationships()));
		} catch (Exception e) {
			tx.failure();
		} finally {
			tx.success();
		}

	}

	/**
	 * Test of addRequirementsNodeToGraphDB method, of class GraphDB.
	 */
	@Test
	public void testAddRequirementsNodeToGraphDB() {
		System.out.println("addRequirementsNodeToGraphDB");
		Transaction tx = graphDbService.beginTx();
		try {

			graphDB.addRequirementsNodeToGraphDB(requirementsAretefactElements);
			IndexManager index = graphDbService.index();
			Index<Node> artefacts = index.forNodes("ArtefactElement");

			IndexHits<Node> hits = artefacts.get("ID",
					requirement.getRequirementId());
			Node firstNode = hits.getSingle();

			assertEquals(firstNode.getProperty("ID").toString(),
					requirement.getRequirementId());
			assertEquals(firstNode.getProperty("Name").toString(),
					requirement.getName());
			assertEquals(firstNode.getProperty("Type").toString(),
					requirement.getType());
			assertEquals(firstNode.getProperty("Title").toString(),
					requirement.getTitle());
			assertEquals(firstNode.getProperty("Content").toString(),
					requirement.getContent());
			assertEquals(firstNode.getProperty("Priority").toString(),
					requirement.getPriority());
			assertEquals(147, IteratorUtil.count(GlobalGraphOperations.at(
					graphDbService).getAllNodes()));

		} catch (Exception e) {
			tx.failure();
		} finally {
			tx.success();
		}
	}

	/**
	 * Test of generateGraphFile method, of class GraphDB.
	 */
	@Test
	public void testGenerateGraphFile() {
		System.out.println("generateGraphFile");
		Transaction tx = graphDbService.beginTx();
		try {
			PropertyFile.setProjectName("Test");
			PropertyFile.setGeneratedGexfFilePath(PropertyFile
					.getTestGraphFile());
			test.testAddNodeToGraphDB();
			test.testAddRequirementsNodeToGraphDB();
			test.testAddRelationTOGraphDB();
			test.testAddIntraRelationTOGraphDB();
			graphDB.generateGraphFile();
			File f = new File(PropertyFile.getTestGraphFile());
			assertTrue(f.exists());
			PropertyFile.setGeneratedGexfFilePath(null);
		} catch (Exception e) {
			tx.failure();
		} finally {
			tx.success();
		}
	}
}
