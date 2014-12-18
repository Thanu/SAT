/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.db;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
     * Test of main method, of class VisualizeGraph.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        VisualizeGraph.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of script method, of class VisualizeGraph.
     */
    @Test
    public void testScript() {
        System.out.println("script");
        String graphType = "";
        VisualizeGraph instance = new VisualizeGraph();
        instance.script(graphType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
