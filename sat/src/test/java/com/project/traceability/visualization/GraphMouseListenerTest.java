/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.traceability.visualization.GraphMouseListener;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.project.api.Workspace;
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
public class GraphMouseListenerTest {
    
    public GraphMouseListenerTest() {
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
     * Test of mouseClicked method, of class GraphMouseListener.
     */
    @Test
    public void testMouseClicked() {
        System.out.println("mouseClicked");
        PreviewMouseEvent event = null;
        PreviewProperties properties = null;
        Workspace workspace = null;
        GraphMouseListener instance = new GraphMouseListener();
        instance.mouseClicked(event, properties, workspace);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mousePressed method, of class GraphMouseListener.
     */
    @Test
    public void testMousePressed() {
        System.out.println("mousePressed");
        PreviewMouseEvent pme = null;
        PreviewProperties pp = null;
        Workspace wrkspc = null;
        GraphMouseListener instance = new GraphMouseListener();
        instance.mousePressed(pme, pp, wrkspc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mouseDragged method, of class GraphMouseListener.
     */
    @Test
    public void testMouseDragged() {
        System.out.println("mouseDragged");
        PreviewMouseEvent pme = null;
        PreviewProperties pp = null;
        Workspace wrkspc = null;
        GraphMouseListener instance = new GraphMouseListener();
        instance.mouseDragged(pme, pp, wrkspc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mouseReleased method, of class GraphMouseListener.
     */
    @Test
    public void testMouseReleased() {
        System.out.println("mouseReleased");
        PreviewMouseEvent pme = null;
        PreviewProperties pp = null;
        Workspace wrkspc = null;
        GraphMouseListener instance = new GraphMouseListener();
        instance.mouseReleased(pme, pp, wrkspc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
