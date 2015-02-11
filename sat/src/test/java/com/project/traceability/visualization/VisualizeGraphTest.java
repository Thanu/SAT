/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.traceability.common.PropertyFile;
import java.io.File;
import org.gephi.graph.api.GraphModel;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 *
 * @author Thanu
 */
public class VisualizeGraphTest {

    VisualizeGraph instance;
    GraphDBTest test;
    GraphDatabaseService graphDb;

    public VisualizeGraphTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = VisualizeGraph.getInstance();
        test = new GraphDBTest();
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(
                PropertyFile.getTestDb()).newGraphDatabase();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setGraphType method, of class VisualizeGraph.
     */
    @Test
    public void testSetGraphType() {
        System.out.println("setGraphType");
        String graphType = "";

        instance.setGraphType(graphType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of importFile method, of class VisualizeGraph.
     */
    @Test
    public void testImportFile() {
        System.out.println("importFile");
        test.testGenerateGraphFile();
        PropertyFile.setProjectName("Test");
        PropertyFile.setGeneratedGexfFilePath(PropertyFile.getTestGraphFile());
        instance.importFile();
        File f = new File(PropertyFile.getTestGraphFile());
        assertTrue(f.exists());
        PropertyFile.setGeneratedGexfFilePath(null);
    }

    /**
     * Test of setPreview method, of class VisualizeGraph.
     */
    @Test
    public void testSetPreview() {
        System.out.println("setPreview");
        VisualizeGraph instance = null;
        instance.setPreview();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLayout method, of class VisualizeGraph.
     */
    @Test
    public void testSetLayout() {
        System.out.println("setLayout");
        VisualizeGraph instance = null;
        instance.setLayout();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setGraph method, of class VisualizeGraph.
     */
    @Test
    public void testSetGraph_GraphModel_String() {
        System.out.println("setGraph");
        GraphModel model = null;
        String graphType = "";
        VisualizeGraph instance = null;
        instance.setGraph(model, graphType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setGraph method, of class VisualizeGraph.
     */
    @Test
    public void testSetGraph_GraphModel() {
        System.out.println("setGraph");
        GraphModel model = null;
        instance.setGraph(model);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showGraph method, of class VisualizeGraph.
     */
    @Test
    public void testShowGraph() {
        System.out.println("showGraph");
        instance.showGraph();
        
    }
}
