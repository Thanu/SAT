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
@Suite.SuiteClasses({com.project.traceability.visualization.VisualizeGraphTest.class, com.project.traceability.visualization.GraphMouseListenerTest.class, com.project.traceability.visualization.GraphFileGeneratorTest.class, com.project.traceability.visualization.GraphRendererTest.class, com.project.traceability.visualization.ItemBuilderTemplateTest.class, com.project.traceability.visualization.GraphDBTest.class})
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
