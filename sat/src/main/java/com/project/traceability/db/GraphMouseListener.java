/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.db;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.ReadFiles;
import java.awt.GridLayout;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.gephi.data.attributes.api.AttributeController;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.cypher.javacompat.ExecutionEngine;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.tooling.GlobalGraphOperations;

import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Thanu
 */
@ServiceProvider(service = PreviewMouseListener.class)
public class GraphMouseListener implements PreviewMouseListener {

    GraphDatabaseService graphDb;
    ExecutionEngine engine;
    ExecutionResult result;

    @Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {

        for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getNodes()) {
            if (clickingInNode(node, event)) {

                graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(PropertyFile.graphDbPath);//.newGraphDatabase();
                Transaction tx = graphDb.beginTx();
                try {
                    System.out.println("Nodes: " + IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllNodes()));
                    System.out.println("Edges: " + IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllRelationships()));

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
                    artefacts.remove(neo4j_node);
                    
                    GraphFileGenerator gg = new GraphFileGenerator();
                    //gg.importGraphFile();
                    gg.generateGraphFile(graphDb);

                    String graphType = properties.getProperties("GraphType").toString();
                    //System.out.println("Type: "+graphType);
                    VisualizeGraph visz = new VisualizeGraph();
                    visz.showGraph(graphType);

                    tx.success();
                } finally {
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
        //JOptionPane.showMessageDialog(null, "Delete?", "Node properties", JOptionPane.PLAIN_MESSAGE);
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

        engine = new ExecutionEngine(graphDb);
        final HashMap<String, Object> node_props = nodeProps;
        final JPanel panel = new JPanel(new GridLayout(0, 1));
        for (String key : nodeProps.keySet()) {
            Object val = nodeProps.get(key);
            if (null != val) {
                JTextField field = new JTextField(nodeProps.get(key).toString());
                panel.add(new JLabel(key + ": "));
                panel.add(field);
            }
        }

        Object[] options = {"Ok", "Delete"};

        int value = JOptionPane.showOptionDialog(null, panel, "Node properties", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (value == JOptionPane.NO_OPTION) {
            int val = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            String id = node_props.get("ID").toString();
            if (val == JOptionPane.YES_OPTION) {
                result = engine.execute("MATCH (n)-[q]-() WHERE n.ID={id} WITH n,q OPTIONAL MATCH (n)-[r:SUB_ELEMENT]-(m) WITH n,q,m MATCH(m)-[k]-(o) DELETE n,q,m,k", MapUtil.map("id", id));
                //result = engine.execute("MATCH (n)-[r]-() WHERE n.ID = {id} DELETE n,r", MapUtil.map("id", id));
                System.out.println(id);
                System.out.println("Nodes: " + IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllNodes()));
                System.out.println("Edges: " + IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllRelationships()));

                ReadFiles.deleteArtefact(id);
            } else {
                result = engine.execute("CREATE (n {name:{name}}) RETURN n", MapUtil.map("name", "ATOM"));
                System.out.println(result.toString());
            }
        } else {
            System.out.println("Nodes: " + IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllNodes()));
            System.out.println("Edges: " + IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllRelationships()));

            //System.out.println("OK");
        }

    }
}
