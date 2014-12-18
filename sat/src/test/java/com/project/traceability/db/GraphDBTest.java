/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.db;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.RequirementModel;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 *
 * @author Thanu
 */
public class GraphDBTest {
    
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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of initiateGraphDB method, of class GraphDB.
     */
    @Test
    public void testInitiateGraphDB() {
        System.out.println("initiateGraphDB");
        GraphDB instance = new GraphDB();
        instance.initiateGraphDB();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addNodeToGraphDB method, of class GraphDB.
     */
    @Test
    public void testAddNodeToGraphDB() {
        System.out.println("addNodeToGraphDB");
        Map<String, ArtefactElement> aretefactElements = null;
        GraphDB instance = new GraphDB();
        instance.addNodeToGraphDB(aretefactElements);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addRelationTOGraphDB method, of class GraphDB.
     */
    @Test
    public void testAddRelationTOGraphDB() {
        System.out.println("addRelationTOGraphDB");
        List<String> relation = null;
        GraphDB instance = new GraphDB();
        instance.addRelationTOGraphDB(relation);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cleanUp method, of class GraphDB.
     */
    @Test
    public void testCleanUp() {
        System.out.println("cleanUp");
        GraphDatabaseService graphDb = null;
        GraphDB instance = new GraphDB();
        instance.cleanUp(graphDb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addRequirementsNodeToGraphDB method, of class GraphDB.
     */
    @Test
    public void testAddRequirementsNodeToGraphDB() {
        System.out.println("addRequirementsNodeToGraphDB");
        List<RequirementModel> requirementsAretefactElements = null;
        GraphDB instance = new GraphDB();
        instance.addRequirementsNodeToGraphDB(requirementsAretefactElements);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateGraphFile method, of class GraphDB.
     */
    @Test
    public void testGenerateGraphFile() {
        System.out.println("generateGraphFile");
        GraphDB instance = new GraphDB();
        instance.generateGraphFile();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
