/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.db;

import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.PreviewMouseListener;
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
public class GraphRendererTest {
    
    public GraphRendererTest() {
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
     * Test of needsPreviewMouseListener method, of class GraphRenderer.
     */
    @Test
    public void testNeedsPreviewMouseListener() {
        System.out.println("needsPreviewMouseListener");
        PreviewMouseListener pl = null;
        GraphRenderer instance = new GraphRenderer();
        boolean expResult = false;
        boolean result = instance.needsPreviewMouseListener(pl);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDisplayName method, of class GraphRenderer.
     */
    @Test
    public void testGetDisplayName() {
        System.out.println("getDisplayName");
        GraphRenderer instance = new GraphRenderer();
        String expResult = "";
        String result = instance.getDisplayName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of preProcess method, of class GraphRenderer.
     */
    @Test
    public void testPreProcess() {
        System.out.println("preProcess");
        PreviewModel pm = null;
        GraphRenderer instance = new GraphRenderer();
        instance.preProcess(pm);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of render method, of class GraphRenderer.
     */
    @Test
    public void testRender() {
        System.out.println("render");
        Item item = null;
        RenderTarget target = null;
        PreviewProperties properties = null;
        GraphRenderer instance = new GraphRenderer();
        instance.render(item, target, properties);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProperties method, of class GraphRenderer.
     */
    @Test
    public void testGetProperties() {
        System.out.println("getProperties");
        GraphRenderer instance = new GraphRenderer();
        PreviewProperty[] expResult = null;
        PreviewProperty[] result = instance.getProperties();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isRendererForitem method, of class GraphRenderer.
     */
    @Test
    public void testIsRendererForitem() {
        System.out.println("isRendererForitem");
        Item item = null;
        PreviewProperties pp = null;
        GraphRenderer instance = new GraphRenderer();
        boolean expResult = false;
        boolean result = instance.isRendererForitem(item, pp);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of needsItemBuilder method, of class GraphRenderer.
     */
    @Test
    public void testNeedsItemBuilder() {
        System.out.println("needsItemBuilder");
        ItemBuilder ib = null;
        PreviewProperties pp = null;
        GraphRenderer instance = new GraphRenderer();
        boolean expResult = false;
        boolean result = instance.needsItemBuilder(ib, pp);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
