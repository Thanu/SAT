/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package java.com.project.traceability.db;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Thanu
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({com.project.traceability.db.VisualizeGraphTest.class, com.project.traceability.db.GraphMouseListenerTest.class, com.project.traceability.db.GraphFileGeneratorTest.class, com.project.traceability.db.GraphRendererTest.class, com.project.traceability.db.ItemBuilderTemplateTest.class, com.project.traceability.db.GraphDBTest.class})
public class DbSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
