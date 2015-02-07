/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.db;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.preview.api.Item;
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
public class ItemBuilderTemplateTest {
    
    public ItemBuilderTemplateTest() {
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
     * Test of getItems method, of class ItemBuilderTemplate.
     */
    @Test
    public void testGetItems() {
        System.out.println("getItems");
        Graph graph = null;
        AttributeModel am = null;
        ItemBuilderTemplate instance = new ItemBuilderTemplate();
        Item[] expResult = null;
        Item[] result = instance.getItems(graph, am);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getType method, of class ItemBuilderTemplate.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        ItemBuilderTemplate instance = new ItemBuilderTemplate();
        String expResult = "";
        String result = instance.getType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
