/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.traceability.visualization.VisualizeGraph;
import java.awt.Frame;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.gephi.graph.api.GraphModel;
import org.gephi.preview.api.PreviewController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import processing.core.PApplet;

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
     * Test of getPreviewController method, of class VisualizeGraph.
     */
    @Test
    public void testGetPreviewController() {
        System.out.println("getPreviewController");
        VisualizeGraph instance = VisualizeGraph.getInstance();
        PreviewController expResult = VisualizeGraph.getInstance().getPreviewController();
        PreviewController result = instance.getPreviewController();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPreviewController method, of class VisualizeGraph.
     */
    @Test
    public void testSetPreviewController() {
        System.out.println("setPreviewController");
        PreviewController previewController = null;
        VisualizeGraph instance = null;
        instance.setPreviewController(previewController);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGraphModel method, of class VisualizeGraph.
     */
    @Test
    public void testGetGraphModel() {
        System.out.println("getGraphModel");
        VisualizeGraph instance = null;
        GraphModel expResult = null;
        GraphModel result = instance.getGraphModel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setGraphModel method, of class VisualizeGraph.
     */
    @Test
    public void testSetGraphModel() {
        System.out.println("setGraphModel");
        GraphModel graphModel = null;
        VisualizeGraph instance = null;
        instance.setGraphModel(graphModel);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGraphType method, of class VisualizeGraph.
     */
    @Test
    public void testGetGraphType() {
        System.out.println("getGraphType");
        VisualizeGraph instance = null;
        String expResult = "";
        String result = instance.getGraphType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApplet method, of class VisualizeGraph.
     */
    @Test
    public void testGetApplet() {
        System.out.println("getApplet");
        VisualizeGraph instance = null;
        PApplet expResult = null;
        PApplet result = instance.getApplet();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setApplet method, of class VisualizeGraph.
     */
    @Test
    public void testSetApplet() {
        System.out.println("setApplet");
        PApplet applet = null;
        VisualizeGraph instance = null;
        instance.setApplet(applet);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTabItem method, of class VisualizeGraph.
     */
    @Test
    public void testGetTabItem() {
        System.out.println("getTabItem");
        VisualizeGraph instance = null;
        CTabItem expResult = null;
        CTabItem result = instance.getTabItem();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTabItem method, of class VisualizeGraph.
     */
    @Test
    public void testSetTabItem() {
        System.out.println("setTabItem");
        CTabItem tabItem = null;
        VisualizeGraph instance = null;
        instance.setTabItem(tabItem);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComposite method, of class VisualizeGraph.
     */
    @Test
    public void testGetComposite() {
        System.out.println("getComposite");
        VisualizeGraph instance = null;
        Composite expResult = null;
        Composite result = instance.getComposite();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setComposite method, of class VisualizeGraph.
     */
    @Test
    public void testSetComposite() {
        System.out.println("setComposite");
        Composite composite = null;
        VisualizeGraph instance = null;
        instance.setComposite(composite);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFrame method, of class VisualizeGraph.
     */
    @Test
    public void testGetFrame() {
        System.out.println("getFrame");
        VisualizeGraph instance = null;
        Frame expResult = null;
        Frame result = instance.getFrame();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setFrame method, of class VisualizeGraph.
     */
    @Test
    public void testSetFrame() {
        System.out.println("setFrame");
        Frame frame = null;
        VisualizeGraph instance = null;
        instance.setFrame(frame);
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
}
