/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Thanu
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({com.project.traceability.manager.ClassCompareManagerTest.class, com.project.traceability.manager.EditManagerTest.class, com.project.traceability.manager.UMLSourceClassManagerTest.class, com.project.traceability.manager.RelationManagerTest.class, com.project.traceability.manager.ReadFilesTest.class, com.project.traceability.manager.InfoExtractionManagerTest.class, com.project.traceability.manager.SourceCodeArtefactManagerTest.class, com.project.traceability.manager.RequirementUMLClassManagerTest.class, com.project.traceability.manager.RequirementSourceClassManagerTest.class, com.project.traceability.manager.ReadXMLTest.class})
public class ManagerTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }    
}
