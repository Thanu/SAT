package com.project.traceability.db;

import it.uniroma1.dis.wsngroup.gexf4j.core.Edge;
import it.uniroma1.dis.wsngroup.gexf4j.core.EdgeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.Gexf;
import it.uniroma1.dis.wsngroup.gexf4j.core.Graph;
import it.uniroma1.dis.wsngroup.gexf4j.core.Mode;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.ClusteringCoefficient;
import org.gephi.statistics.plugin.Degree;
import org.gephi.statistics.plugin.EigenvectorCentrality;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.statistics.plugin.Modularity;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;
import org.openide.util.Lookup;

import com.project.traceability.common.PropertyFile;

/**
 * Model to add generate graph file graph db.
 * 
 * @author Thanu
 *
 */
public class GraphFileGenerator {
	ExecutionEngine engine;
	ExecutionResult result;
	GraphDatabaseService graphDb;
	Graph graph;
	HashMap<String, it.uniroma1.dis.wsngroup.gexf4j.core.Node> nodes;

	public static void main(String[] args) {

		GraphDatabaseService graphDb = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder(PropertyFile.graphDbPath)
				.newGraphDatabase();
		GraphFileGenerator gg = new GraphFileGenerator();
		gg.generateGraphFile(graphDb);
		gg.updateGraphFile(graphDb);
	}

	public void generateGraphFile(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
		Gexf gexf = new GexfImpl();
		gexf.setVisualization(true);
		graph = gexf.getGraph();
		graph.setDefaultEdgeType(EdgeType.DIRECTED).setMode(Mode.STATIC);
		engine = new ExecutionEngine(graphDb);

		addNodes();
		//addEdges();

		StaxGraphWriter graphWriter = new StaxGraphWriter();
		File f = new File(PropertyFile.generatedGexfFilePath);
		Writer out;
		try {
			out = new FileWriter(f, false);
			graphWriter.writeToStream(gexf, out, "UTF-8");
			System.out.println(f.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addNodes() {
		AttributeList nodeAttrList = new AttributeListImpl(AttributeClass.NODE);
		graph.getAttributeLists().add(nodeAttrList);

		HashMap<String, Attribute> val = new HashMap<String, Attribute>();

		try (Transaction ignored = graphDb.beginTx()) {

			result = engine.execute("MATCH (n) RETURN n");

			Iterator<org.neo4j.graphdb.Node> n_column = result.columnAs("n");
			nodes = new HashMap<String, it.uniroma1.dis.wsngroup.gexf4j.core.Node>();

			for (org.neo4j.graphdb.Node node : IteratorUtil
					.asIterable(n_column)) {
				Iterable<String> property = node.getPropertyKeys();

				String id = (String) node.getProperty("ID");
				it.uniroma1.dis.wsngroup.gexf4j.core.Node new_node = graph
						.createNode(id);
				new_node.setLabel(id);

				for (String prop : property) {
					if (!val.containsKey(prop)) {
						Attribute attr = nodeAttrList.createAttribute(prop,
								AttributeType.STRING, prop);
						val.put(prop, attr);
						new_node.getAttributeValues().addValue(attr,
								(String) node.getProperty(prop));
					} else {
						new_node.getAttributeValues().addValue(val.get(prop),
								(String) node.getProperty(prop));
					}
				}
				nodes.put(id, new_node);
			}
		}
	}

	public void addNodes(HashMap<String, HashMap<String, Double>> nodes_props) {
		AttributeList nodeAttrList = new AttributeListImpl(AttributeClass.NODE);
		graph.getAttributeLists().add(nodeAttrList);

		Attribute attr_ec = nodeAttrList.createAttribute("eccentricity",
				AttributeType.DOUBLE, "Eccentricity");
		Attribute attr_cc = nodeAttrList.createAttribute("closenesscentrality",
				AttributeType.DOUBLE, "Closeness Centrality");
		Attribute attr_b = nodeAttrList.createAttribute("betweenesscentrality",
				AttributeType.DOUBLE, "Betweeness Centrality");
		Attribute attr_cl = nodeAttrList.createAttribute("clustering",
				AttributeType.DOUBLE, "Clustering Coefficient");
		Attribute attr_evc = nodeAttrList.createAttribute("eigencentrality",
				AttributeType.DOUBLE, "Eigen Vector Centrality");
		Attribute attr_mc = nodeAttrList.createAttribute("modularity_class",
				AttributeType.STRING, "Modularity Class");

		HashMap<String, Attribute> val = new HashMap<String, Attribute>();

		try (Transaction ignored = graphDb.beginTx()) {
			result = engine.execute("MATCH (n) RETURN n");

			Iterator<org.neo4j.graphdb.Node> n_column = result.columnAs("n");
			nodes = new HashMap<String, it.uniroma1.dis.wsngroup.gexf4j.core.Node>();

			for (org.neo4j.graphdb.Node node : IteratorUtil
					.asIterable(n_column)) {
				Iterable<String> property = node.getPropertyKeys();

				String id = (String) node.getProperty("ID");
				it.uniroma1.dis.wsngroup.gexf4j.core.Node new_node = graph
						.createNode(id);
				new_node.setLabel(id);

				for (String prop : property) {
					if (!val.containsKey(prop)) {
						Attribute attr = nodeAttrList.createAttribute(prop,
								AttributeType.STRING, prop);
						val.put(prop, attr);
						new_node.getAttributeValues().addValue(attr,
								(String) node.getProperty(prop));
					} else {
						new_node.getAttributeValues().addValue(val.get(prop),
								(String) node.getProperty(prop));
					}
				}
				val.put(attr_ec.getId(), attr_ec);
				new_node.getAttributeValues().addValue(attr_ec,
						nodes_props.get(id).get(attr_ec.getId()).toString());
				val.put(attr_cc.getId(), attr_cc);
				new_node.getAttributeValues().addValue(attr_cc,
						nodes_props.get(id).get(attr_cc.getId()).toString());
				val.put(attr_b.getId(), attr_b);
				new_node.getAttributeValues().addValue(attr_b,
						nodes_props.get(id).get(attr_b.getId()).toString());
				val.put(attr_cl.getId(), attr_cl);
				new_node.getAttributeValues().addValue(attr_cl,
						nodes_props.get(id).get(attr_cl.getId()).toString());
				val.put(attr_mc.getId(), attr_mc);
				new_node.getAttributeValues().addValue(attr_mc,
						nodes_props.get(id).get(attr_mc.getId()).toString());
				val.put(attr_evc.getId(), attr_evc);
				new_node.getAttributeValues().addValue(attr_evc,
						nodes_props.get(id).get(attr_evc.getId()).toString());
				nodes.put(id, new_node);
			}
		}
	}

	public void addEdges() {
		AttributeList edgeAttrList = new AttributeListImpl(AttributeClass.EDGE);
		graph.getAttributeLists().add(edgeAttrList);

		Attribute attr_rt = edgeAttrList.createAttribute("neo4j_rt",
				AttributeType.STRING, "Neo4j Relationship Type");
		Attribute attr_msg = edgeAttrList.createAttribute("message",
				AttributeType.STRING, "Message");

		try (Transaction ignored = graphDb.beginTx()) {
			result = engine.execute("MATCH (n) RETURN n");
			Iterator<org.neo4j.graphdb.Node> column = result.columnAs("n");

			HashMap<String, it.uniroma1.dis.wsngroup.gexf4j.core.Edge> edges = new HashMap<String, it.uniroma1.dis.wsngroup.gexf4j.core.Edge>();

			for (org.neo4j.graphdb.Node node : IteratorUtil.asIterable(column)) {

				Iterable<Relationship> relationships = node
						.getRelationships(Direction.OUTGOING);

				for (Relationship rel : relationships) {
					it.uniroma1.dis.wsngroup.gexf4j.core.Node source = nodes
							.get(node.getProperty("ID"));
					it.uniroma1.dis.wsngroup.gexf4j.core.Node target = nodes
							.get(rel.getEndNode().getProperty("ID"));
					String id = Long.toString(rel.getId());

					if (!edges.containsKey(id)) {
//						System.out.print(node.getProperty("ID")+ "*******");
//						System.out.println(rel.getEndNode().getProperty("ID"));
						Edge e = source
								.connectTo(Long.toString(rel.getId()), rel
										.getType().name(), EdgeType.DIRECTED,
										target);
						e.getAttributeValues().addValue(attr_msg,
								(String) rel.getProperty("message"));
						e.getAttributeValues().addValue(attr_rt,
								rel.getType().name());
						edges.put(id.toString(), e);
					}

				}
			}
		}
	}

	public HashMap<String, HashMap<String, Double>> importGraphFile() {
		ProjectController pc = Lookup.getDefault().lookup(
				ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// Import file
		ImportController importController = Lookup.getDefault().lookup(
				ImportController.class);
		Container container;
		try {
			File file = new File(PropertyFile.generatedGexfFilePath);
			container = importController.importFile(file);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		// Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		GraphModel graphModel = Lookup.getDefault()
				.lookup(GraphController.class).getModel();

		AttributeModel attributeModel = Lookup.getDefault()
				.lookup(AttributeController.class).getModel();

		DirectedGraph graph = graphModel.getDirectedGraph();

		Degree degree = new Degree();
		System.out.println("Calculating degrees...");
		degree.execute(graphModel, attributeModel);

		GraphDistance distance = new GraphDistance();
		distance.setDirected(false);
		System.out.println("Calculating Graph Distance...");
		distance.execute(graphModel, attributeModel);

		ClusteringCoefficient clustercoefficient = new ClusteringCoefficient();
		System.out.println("Calculating Clustering Coefficient");
		clustercoefficient.execute(graphModel, attributeModel);

		Modularity modularity = new Modularity();
		modularity.execute(graphModel, attributeModel);

		EigenvectorCentrality eigenvectorcentrality = new EigenvectorCentrality();
		eigenvectorcentrality.execute(graphModel, attributeModel);

		HashMap<String, HashMap<String, Double>> nodes_props = new HashMap<String, HashMap<String, Double>>();

		HashMap<String, Double> properties;
		for (org.gephi.graph.api.Node n : graph.getNodes()) {

			properties = new HashMap<String, Double>();

			AttributeColumn betweeness = attributeModel.getNodeTable()
					.getColumn(GraphDistance.BETWEENNESS);
			AttributeColumn closseness = attributeModel.getNodeTable()
					.getColumn(GraphDistance.CLOSENESS);
			AttributeColumn eccentricity = attributeModel.getNodeTable()
					.getColumn(distance.ECCENTRICITY);

			AttributeColumn clustering = attributeModel.getNodeTable()
					.getColumn(ClusteringCoefficient.CLUSTERING_COEFF);
			AttributeColumn eigenvector = attributeModel.getNodeTable()
					.getColumn(EigenvectorCentrality.EIGENVECTOR);
			AttributeColumn modularityclass = attributeModel.getNodeTable()
					.getColumn(Modularity.MODULARITY_CLASS);

			double b = (Double) n.getNodeData().getAttributes()
					.getValue(betweeness.getIndex()); // betweeness
			double c = (Double) n.getNodeData().getAttributes()
					.getValue(closseness.getIndex()); // closseness
			double e = (Double) n.getNodeData().getAttributes()
					.getValue(eccentricity.getIndex()); // eccentricity
			double cl = (Double) n.getNodeData().getAttributes()
					.getValue(clustering.getIndex()); // clustering
			int m = (Integer) n.getNodeData().getAttributes()
					.getValue(modularityclass.getIndex()); // modularityclass
			double eg = (Double) n.getNodeData().getAttributes()
					.getValue(eigenvector.getIndex()); // eigen vector
														// centrality

			properties.put("eccentricity", e);
			properties.put("closenesscentrality", c);
			properties.put("betweenesscentrality", b);
			properties.put("eigencentrality", eg);
			properties.put("clustering", cl);
			properties.put("modularity_class", (double) m);

			nodes_props.put(
					(String) n.getNodeData().getAttributes().getValue("ID"),
					properties);
		}
		return nodes_props;
	}

	public void updateGraphFile(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;

		HashMap<String, HashMap<String, Double>> nodes_props = importGraphFile();

		Gexf gexf = new GexfImpl();
		gexf.setVisualization(true);
		graph = gexf.getGraph();
		graph.setDefaultEdgeType(EdgeType.DIRECTED).setMode(Mode.STATIC);

		addNodes(nodes_props);
		addEdges();

		StaxGraphWriter graphWriter = new StaxGraphWriter();
		File f = new File(PropertyFile.generatedGexfFilePath);
		Writer out;
		try {
			out = new FileWriter(f, false);
			graphWriter.writeToStream(gexf, out, "UTF-8");
			System.out.println(f.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
