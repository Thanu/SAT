/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.db;

import com.project.traceability.common.PropertyFile;
import java.awt.GridLayout;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;

import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Thanu
 */

@ServiceProvider(service = PreviewMouseListener.class)
public class GraphMouseListener implements PreviewMouseListener {

    @Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
        System.err.println("mouse clicked");
        AttributeController ac = Lookup.getDefault().lookup(AttributeController.class);
        AttributeModel model = ac.getModel();

        for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getNodes()) {
            if (clickingInNode(node, event)) {

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
            }
        }

        properties.removeSimpleValue("display-label.node.id");
        event.setConsumed(true);
    }

    @Override
    public void mousePressed(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc) {

    }

    @Override
    public void mouseDragged(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc) {

    }

    @Override
    public void mouseReleased(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc) {

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

}