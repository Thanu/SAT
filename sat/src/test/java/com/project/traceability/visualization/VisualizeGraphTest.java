/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import org.gephi.graph.api.GraphModel;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Thanu
 */
public class VisualizeGraphTest {
    
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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class VisualizeGraph.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        VisualizeGraph expResult = VisualizeGraph.getInstance();
        VisualizeGraph result = VisualizeGraph.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setGraphType method, of class VisualizeGraph.
     */
    @Test
    public void testSetGraphType() {
        System.out.println("setGraphType");
        String graphType = "";
        VisualizeGraph instance = null;
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
        VisualizeGraph instance = null;
        instance.importFile();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        VisualizeGraph instance = null;
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
        VisualizeGraph instance = null;
        instance.showGraph();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
