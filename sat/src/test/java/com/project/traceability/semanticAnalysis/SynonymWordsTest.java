package com.project.traceability.semanticAnalysis;

import junit.framework.TestCase;

/**
 * 7 Dec 2014
 *
 * @author K.Kamalan
 *
 */
public class SynonymWordsTest extends TestCase {

    /**
     * Test method for
     * {@link com.project.traceability.semanticAnalysis.SynonymWords#getSynSetWords(java.lang.String)}.
     */
    public void testGetSynSetWords() {
//		fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.project.traceability.semanticAnalysis.SynonymWords#checkSymilarity(java.lang.String, java.lang.String, java.lang.String)}.
     */
    public void testCheckSymilarity() {
       // assertEquals(true, SynonymWords.checkSymilarity("getAccountNo", "getAccountNumber", "Field"));
       // assertEquals(true, SynonymWords.checkSymilarity("airplane", "plane", "Attribute"));
        assertEquals(false, SynonymWords.checkSymilarity("getAccountNo", "setAccountNo", "Attribute"));
        assertEquals(false, SynonymWords.checkSymilarity("AccountNo", "AccountHolder", "Attribute", "Account"));
//		fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.project.traceability.semanticAnalysis.SynonymWords#isFirstletterChanged(java.lang.String, java.lang.String)}.
     */
    public void testIsFirstletterChanged() {
        assertEquals(true, SynonymWords.isFirstletterChanged("getTelNo", "setTelNo"));
        assertEquals(false, SynonymWords.isFirstletterChanged("getTelNo", "getTelNo"));
//		fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.project.traceability.semanticAnalysis.SynonymWords#HasSimilarWords(java.lang.String, java.lang.String, java.lang.String)}.
     */
    public void testHasSimilarWords() {
//		fail("Not yet implemented");
    }

}
