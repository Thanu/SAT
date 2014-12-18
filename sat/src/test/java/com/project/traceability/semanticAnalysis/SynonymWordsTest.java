/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project.traceability.semanticAnalysis;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author K.Kamalan
 */
public class SynonymWordsTest {
    
    public SynonymWordsTest() {
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
     * Test of checkSymilarity method, of class SynonymWords.
     */
    @Test
    public void testCheckSymilarity_3args() {
        System.out.println("checkSymilarity");
        String term1 = "Account";
        String term2 = "account";
        String type = "class";
        boolean expResult = true;
        boolean result = SynonymWords.checkSymilarity(term1, term2, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of checkSymilarity method, of class SynonymWords.
     */
    @Test
    public void testCheckSymilarity_4args() {
        System.out.println("checkSymilarity");
        String term1 = "AccountNo";
        String term2 = "AccountHolder";
        String type = "attribute";
        List<String> classNames = new ArrayList<>();
        classNames.add("account");
        classNames.add("customer");
        boolean expResult = false;
        boolean result = SynonymWords.checkSymilarity(term1, term2, type, classNames);
        assertEquals(expResult, result);
        assertEquals(false, SynonymWords.checkSymilarity("getAccount", "getAccountNo","Field",classNames));
        assertEquals(true, SynonymWords.checkSymilarity("airplane", "plane","Attribute",classNames));
        assertEquals(false, SynonymWords.checkSymilarity("isPaid", "isAc","Attribute",classNames));
        assertEquals(true, SynonymWords.checkSymilarity("telNo", "telephone number","Attribute",classNames));
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of isFirstletterChanged method, of class SynonymWords.
     */
    @Test
    public void testIsFirstletterChanged() {
        System.out.println("isFirstletterChanged");
        String term1 = "getName";
        String term2 = "setName";
        boolean expResult = true;
        boolean result = SynonymWords.isFirstletterChanged(term1, term2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    
    
}
