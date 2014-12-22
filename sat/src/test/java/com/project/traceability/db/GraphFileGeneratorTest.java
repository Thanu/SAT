/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.db;

import it.uniroma1.dis.wsngroup.gexf4j.core.Graph;
import it.uniroma1.dis.wsngroup.gexf4j.core.Node;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 *
 * @author Thanu
 */
public class GraphFileGeneratorTest {
    
    public GraphFileGeneratorTest() {
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
     * Test of getEngine method, of class GraphFileGenerator.
     */
    @Test
    public void testGetEngine() {
        System.out.println("getEngine");
        GraphFileGenerator instance = new GraphFileGenerator();
        ExecutionEngine expResult = null;
        ExecutionEngine result = instance.getEngine();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEngine method, of class GraphFileGenerator.
     */
    @Test
    public void testSetEngine() {
        System.out.println("setEngine");
        ExecutionEngine engine = null;
        GraphFileGenerator instance = new GraphFileGenerator();
        instance.setEngine(engine);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getResult method, of class GraphFileGenerator.
     */
    @Test
    public void testGetResult() {
        System.out.println("getResult");
        GraphFileGenerator instance = new GraphFileGenerator();
        ExecutionResult expResult = null;
        ExecutionResult result = instance.getResult();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setResult method, of class GraphFileGenerator.
     */
    @Test
    public void testSetResult() {
        System.out.println("setResult");
        ExecutionResult result_2 = null;
        GraphFileGenerator instance = new GraphFileGenerator();
        instance.setResult(result_2);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGraphDb method, of class GraphFileGenerator.
     */
    @Test
    public void testGetGraphDb() {
        System.out.println("getGraphDb");
        GraphFileGenerator instance = new GraphFileGenerator();
        GraphDatabaseService expResult = null;
        GraphDatabaseService result = instance.getGraphDb();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setGraphDb method, of class GraphFileGenerator.
     */
    @Test
    public void testSetGraphDb() {
        System.out.println("setGraphDb");
        GraphDatabaseService graphDb = null;
        GraphFileGenerator instance = new GraphFileGenerator();
        instance.setGraphDb(graphDb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGraph method, of class GraphFileGenerator.
     */
    @Test
    public void testGetGraph() {
        System.out.println("getGraph");
        GraphFileGenerator instance = new GraphFileGenerator();
        Graph expResult = null;
        Graph result = instance.getGraph();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setGraph method, of class GraphFileGenerator.
     */
    @Test
    public void testSetGraph() {
        System.out.println("setGraph");
        Graph graph = null;
        GraphFileGenerator instance = new GraphFileGenerator();
        instance.setGraph(graph);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNodes method, of class GraphFileGenerator.
     */
    @Test
    public void testGetNodes() {
        System.out.println("getNodes");
        GraphFileGenerator instance = new GraphFileGenerator();
        HashMap expResult = null;
        HashMap result = instance.getNodes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNodes method, of class GraphFileGenerator.
     */
    @Test
    public void testSetNodes() {
        System.out.println("setNodes");
        HashMap<String, Node> nodes = null;
        GraphFileGenerator instance = new GraphFileGenerator();
        instance.setNodes(nodes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class GraphFileGenerator.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        GraphFileGenerator.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateGraphFile method, of class GraphFileGenerator.
     */
    @Test
    public void testGenerateGraphFile() {
        System.out.println("generateGraphFile");
        GraphDatabaseService graphDb = null;
        GraphFileGenerator instance = new GraphFileGenerator();
        instance.generateGraphFile(graphDb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addNodes method, of class GraphFileGenerator.
     */
    @Test
    public void testAddNodes_0args() {
        System.out.println("addNodes");
        GraphFileGenerator instance = new GraphFileGenerator();
        instance.addNodes();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addNodes method, of class GraphFileGenerator.
     */
    @Test
    public void testAddNodes_HashMap() {
        System.out.println("addNodes");
        HashMap<String, HashMap<String, Double>> nodes_props = null;
        GraphFileGenerator instance = new GraphFileGenerator();
        instance.addNodes(nodes_props);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addEdges method, of class GraphFileGenerator.
     */
    @Test
    public void testAddEdges() {
        System.out.println("addEdges");
        GraphFileGenerator instance = new GraphFileGenerator();
        instance.addEdges();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of importGraphFile method, of class GraphFileGenerator.
     */
    @Test
    public void testImportGraphFile() {
        System.out.println("importGraphFile");
        GraphFileGenerator instance = new GraphFileGenerator();
        HashMap expResult = null;
        HashMap result = instance.importGraphFile();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateGraphFile method, of class GraphFileGenerator.
     */
    @Test
    public void testUpdateGraphFile() {
        System.out.println("updateGraphFile");
        GraphDatabaseService graphDb = null;
        GraphFileGenerator instance = new GraphFileGenerator();
        instance.updateGraphFile(graphDb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
