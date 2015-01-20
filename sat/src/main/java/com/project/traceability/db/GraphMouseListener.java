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
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.plugin.graph.EgoBuilder.EgoFilter;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.tooling.GlobalGraphOperations;
import org.openide.util.Exceptions;
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
//                    System.out.println("DB Nodes: " + IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllNodes()));
//                    System.out.println("DB Edges: " + IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllRelationships()));

                    IndexManager index = graphDb.index();
                    Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");

                    IndexHits<org.neo4j.graphdb.Node> hits = artefacts.get("ID", node.getNodeData().getAttributes().getValue("ID"));
                    org.neo4j.graphdb.Node neo4j_node = hits.getSingle();

                    HashMap<String, Object> nodeProps = new HashMap<>();
                    for (String col : neo4j_node.getPropertyKeys()) {
                        Object val = neo4j_node.getProperty(col);
                        nodeProps.put(col, val);
                    }

                    HashMap<String, Object> values = showPopup(nodeProps);
                    int value = Integer.parseInt(values.get("Value").toString());
                    final String id = values.get("ID").toString();

//                    System.out.println("Value: " + value + " ID: " + id);
                    if (value == JOptionPane.NO_OPTION) {
                        tx.success();
                        GraphFileGenerator gg = new GraphFileGenerator();
                        gg.generateGraphFile(graphDb);
                        VisualizeGraph visual = VisualizeGraph.getInstance();//PropertyFile.getVisual();
                        visual.importFile();
                        GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();
                        visual.setGraph(model, PropertyFile.graphType);
                        visual.setPreview();
                        visual.setLayout();
                    } else if (value == JOptionPane.YES_OPTION) {
                        VisualizeGraph preview = VisualizeGraph.getInstance();//PropertyFile.getVisual();//new VisualizeGraph();
                        preview.importFile();
                        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
                        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
                        EgoFilter egoFilter = new EgoFilter();
                        egoFilter.setPattern(id);
                        egoFilter.setDepth(1);
                        egoFilter.setSelf(true);
                        Query queryEgo = filterController.createQuery(egoFilter);
                        GraphView viewEgo = filterController.filter(queryEgo);
                        graphModel.setVisibleView(viewEgo);
                        preview.setGraph(graphModel);
                        preview.setPreview();
                        preview.setLayout();
                    }

                } catch (Exception e) {
                    Exceptions.printStackTrace(e);
                    System.out.println(e.toString());
                } finally {
                    tx.finish();
                    graphDb.shutdown();
                    return;
                }
            }
        }
//        for (Edge edge : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getEdges()) {
//            if (clickingInEdge(edge, event)) {
//                System.out.println("Edge is clicked");
//                graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(PropertyFile.graphDbPath);//.newGraphDatabase();
//                Transaction tx = graphDb.beginTx();
//                try {
//                    IndexManager index = graphDb.index();
//                    Index<Relationship> edges = index.forRelationships(edge.getEdgeData().getLabel());
//                    String sourceId = edge.getEdgeData().getSource().getId();
//                    String targetId = edge.getEdgeData().getTarget().getId();
//                    System.out.println(sourceId+"-"+targetId);
//
//                    IndexHits<Relationship> hits = edges.get("ID",sourceId+"-"+targetId);
//                    Relationship rel = hits.getSingle();
//
//                    HashMap<String, Object> edgeProps = new HashMap<>();
//                    for (String col : rel.getPropertyKeys()) {
//                        Object val = rel.getProperty(col);
//                        edgeProps.put(col, val);
//                    }
//                    edgeProps.put("Source Node",sourceId);
//                    edgeProps.put("Target Node",targetId);
//
//                    showPopup(edgeProps);
//                    tx.success();
//                    //artefacts.remove(neo4j_node);
//
//                    GraphFileGenerator gg = new GraphFileGenerator();
//                    gg.generateGraphFile(graphDb);
//
//                    VisualizeGraph visz = new VisualizeGraph();
//                    visz.importFile();
//                    GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();
//                    visz.setGraph(model, PropertyFile.graphType);
//                    visz.showGraph();
//                    visz.addPanel(visz.getApplet(), visz.getComposite(), visz.getTabItem());
//
//                } finally {
//                    tx.finish();
//                    graphDb.shutdown();
//                    return;
//                }
//            }
//        }

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

    private boolean clickingInEdge(Edge edge, PreviewMouseEvent event) {
//        float x = edge.getEdgeData().x();
//        float y = edge.getEdgeData().y();
        float xdiff = edge.getEdgeData().x() - event.x;
        float ydiff = -edge.getEdgeData().y() - event.y;//Note that y axis is inverse for node coordinates
        //float radius = edge.getEdgeData().getRadius();
        //System.out.println("x: "+x+" ex: "+event.x+" y: "+y+" ey: "+event.y+" X: "+xdiff+" Y: "+ydiff+" diff: "+xdiff * xdiff + ydiff * ydiff);
        return xdiff * xdiff + ydiff * ydiff < 100;
    }

    private HashMap<String, Object> showPopup(HashMap<String, Object> nodeProps) {
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

        Object[] options = {"Show Impact", "Delete"};

        int value = JOptionPane.showOptionDialog(VisualizeGraph.getInstance().getFrame(), panel, "Node properties", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, null);//options[0]);
        HashMap<String, Object> returnVal = new HashMap<>();
        String id = node_props.get("ID").toString();
        returnVal.put("ID", id);
        if (value == JOptionPane.NO_OPTION) {
            int val = JOptionPane.showConfirmDialog(VisualizeGraph.getInstance().getFrame(), "Are you sure you want to delete?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (val == JOptionPane.YES_OPTION) {
                result = engine.execute("MATCH (n)-[q]-() WHERE n.ID={id} WITH n,q OPTIONAL MATCH (n)-[r:SUB_ELEMENT]-(m) WITH n,q,m OPTIONAL MATCH(m)-[k]-(o) DELETE n,q,m,k", MapUtil.map("id", id));

//                System.out.println(id);
//                System.out.println("Nodes: " + IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllNodes()));
//                System.out.println("Edges: " + IteratorUtil.count(GlobalGraphOperations.at(graphDb).getAllRelationships()));

                ReadFiles.deleteArtefact(id);
            } else {
            }
        }
        returnVal.put("Value", value);
        return returnVal;

    }
}
