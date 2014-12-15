/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.db;

<<<<<<< HEAD
import com.project.traceability.common.PropertyFile;
=======
>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94
import java.awt.GridLayout;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
<<<<<<< HEAD
=======
import org.gephi.data.attributes.api.AttributeColumn;
>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
<<<<<<< HEAD
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
=======
>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Thanu
 */
<<<<<<< HEAD
@ServiceProvider( service = PreviewMouseListener.class)
=======
@ServiceProvider(service = PreviewMouseListener.class)
>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94
public class GraphMouseListener implements PreviewMouseListener {

    @Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
        System.err.println("mouse clicked");
        AttributeController ac = Lookup.getDefault().lookup(AttributeController.class);
        AttributeModel model = ac.getModel();

        for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getNodes()) {
            if (clickingInNode(node, event)) {
<<<<<<< HEAD
                GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(PropertyFile.graphDbPath).newGraphDatabase();
                Transaction tx = graphDb.beginTx();
                try {

                    IndexManager index = graphDb.index();
                    Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");

                    IndexHits<org.neo4j.graphdb.Node> hits = artefacts.get("ID", node.getNodeData().getAttributes().getValue("ID"));
                    org.neo4j.graphdb.Node neo4j_node = hits.getSingle();

                    HashMap<String, Object> nodeProps = new HashMap<>();
                    for (String col : neo4j_node.getPropertyKeys()) {
                        Object val = neo4j_node.getProperty(col);
                        nodeProps.put(col, val);
                    }
                    showPopup(nodeProps);
                    properties.putValue("display-label.node.id", node.getNodeData().getId());
                    event.setConsumed(true);//So the renderer is executed and the graph repainted
                    tx.success();
                } finally {
                    tx.finish();
                    graphDb.shutdown();
                    return;
                }
=======
                HashMap<String, Object> nodeProps = new HashMap<String, Object>();
                for (AttributeColumn col : model.getNodeTable().getColumns()) {
                    String column = col.getTitle();
                    Object val = node.getNodeData().getAttributes().getValue(column);
                    // System.out.println(col + ": " + val);
                    nodeProps.put(column, val);
                }
                showPopup(nodeProps);
                properties.putValue("display-label.node.id", node.getNodeData().getId());
                event.setConsumed(true);//So the renderer is executed and the graph repainted
                return;
>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94
            }
        }

        properties.removeSimpleValue("display-label.node.id");
        event.setConsumed(true);
    }

    @Override
    public void mousePressed(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc) {
<<<<<<< HEAD
=======

>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94
    }

    @Override
    public void mouseDragged(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc) {
<<<<<<< HEAD
=======

>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94
    }

    @Override
    public void mouseReleased(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc) {
<<<<<<< HEAD
=======

>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94
    }

    private boolean clickingInNode(Node node, PreviewMouseEvent event) {
        float xdiff = node.getNodeData().x() - event.x;
        float ydiff = -node.getNodeData().y() - event.y;//Note that y axis is inverse for node coordinates
        float radius = node.getNodeData().getRadius();

        return xdiff * xdiff + ydiff * ydiff < radius * radius;
    }

    private void showPopup(HashMap<String, Object> nodeProps) {

        JPanel panel = new JPanel(new GridLayout(0, 1));

        for (String key : nodeProps.keySet()) {
            Object val = nodeProps.get(key);
            if (null != val) {
                JTextField field = new JTextField(nodeProps.get(key).toString());
                panel.add(new JLabel(key + ": "));
                panel.add(field);
            }
        }
        JOptionPane.showMessageDialog(null, panel, "Node properties", JOptionPane.PLAIN_MESSAGE);
    }
<<<<<<< HEAD
=======

>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94
}
