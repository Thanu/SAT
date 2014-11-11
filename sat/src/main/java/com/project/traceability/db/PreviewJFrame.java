package com.project.traceability.db;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Attributes;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.RankingController;
import org.gephi.statistics.plugin.ClusteringCoefficient;
import org.gephi.statistics.plugin.Degree;
import org.gephi.statistics.plugin.EigenvectorCentrality;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.statistics.plugin.Hits;
import org.gephi.statistics.plugin.Modularity;
import org.gephi.statistics.plugin.PageRank;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.MapUtil;
import org.openide.util.Lookup;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

public class PreviewJFrame {
	public static void main(String[] args) {
		PreviewJFrame preview = new PreviewJFrame();
		GraphDatabaseService graphDb = new GraphDatabaseFactory()
		.newEmbeddedDatabaseBuilder("D:\\Neo4j\\atomdb.graphdb")
		.newGraphDatabase();
		preview.script(graphDb);
	}

	public void script(GraphDatabaseService graphDb) {

		System.out.println("Begin Building GEXF from Neo4j");
		System.out.println("Fetching graph...");
		// Get the database and the owner index
//		GraphDatabaseService graphDb = new GraphDatabaseFactory()
//				.newEmbeddedDatabaseBuilder("D:\\Neo4j\\atomdb.graphdb")
//				.newGraphDatabase();

		Neo4j2Graph ngraph = new Neo4j2Graph(graphDb);
		// Graph ngraph = new Neo4jGraph(graphDb);
		try {
			GraphMLWriter.outputGraph(ngraph,
					"C:\\Users\\Thanu\\Documents\\atom.gexf");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			registerShutdownHook(graphDb);
		}

		// Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(
				ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// Import file
		ImportController importController = Lookup.getDefault().lookup(
				ImportController.class);
		Container container;
		try {
			File file = new File("C:\\Users\\Thanu\\Documents\\atom.gexf");
			container = importController.importFile(file);
			System.out.println("bncbnd");
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		// Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		GraphModel graphModel = Lookup.getDefault()
				.lookup(GraphController.class).getModel();

		AttributeModel attributeModel = Lookup.getDefault()
				.lookup(AttributeController.class).getModel();

		RankingController rankingController = Lookup.getDefault().lookup(
				RankingController.class);

		// See if graph is well imported
		DirectedGraph graph = graphModel.getDirectedGraph();
		System.out.println(graph.getEdgeCount());

		final UndirectedGraph ugraph = graphModel.getUndirectedGraph();

		Degree degree = new Degree();
		System.out.println("Calculating degrees...");
		degree.execute(graphModel,
				 attributeModel);

		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		System.out.println("Calculating Graph Distance...");
		distance.execute(graphModel,
				attributeModel);

		ClusteringCoefficient clustercoefficient = new ClusteringCoefficient();
		System.out.println("Calculating Clustering Coefficient");
		clustercoefficient.execute(graphModel,
				 attributeModel);

		Hits hits = new Hits();
		System.out.println("Calculating HITS...");
		hits.execute(graphModel,
				attributeModel);

		PageRank pagerank = new PageRank();
		System.out.println("Calculating PageRank...");
		pagerank.execute(graphModel,
				 attributeModel);
		

		Modularity modularity = new Modularity();
		modularity.execute(graphModel, attributeModel);

		EigenvectorCentrality eigenvectorcentrality = new EigenvectorCentrality();
		eigenvectorcentrality.execute(graphModel,
				 attributeModel);

		LinkedList<Map<String, Object>> nodes = new LinkedList<Map<String, Object>>();
		
		Map<String, Object> properties = MapUtil.map();
		for (Node n : graph.getNodes()) {

			Attributes attr = n.getAttributes();
			
//			AttributeColumn deg = attributeModel.getNodeTable().getColumn(
//					Degree.DEGREE);
//			AttributeColumn pag = attributeModel.getNodeTable().getColumn(
//					PageRank.PAGERANK);
//			AttributeColumn auth = attributeModel.getNodeTable().getColumn(
//					Hits.AUTHORITY);
//			AttributeColumn hub = attributeModel.getNodeTable().getColumn(
//					Hits.HUB);

			AttributeColumn betweeness = attributeModel.getNodeTable()
					.getColumn(distance.BETWEENNESS);
			AttributeColumn closseness = attributeModel.getNodeTable()
					.getColumn(distance.CLOSENESS);
			AttributeColumn eccentricity = attributeModel.getNodeTable()
					.getColumn(distance.ECCENTRICITY);

//			AttributeColumn clustering = attributeModel.getNodeTable()
//					.getColumn(clustercoefficient.CLUSTERING_COEFF);
//			AttributeColumn eigenvector = attributeModel.getNodeTable()
//					.getColumn(eigenvectorcentrality.EIGENVECTOR);
//			AttributeColumn modularityclass = attributeModel.getNodeTable()
//					.getColumn(modularity.MODULARITY_CLASS);

//			int d = (Integer) n.getNodeData().getAttributes()
//					.getValue(deg.getIndex()); // degree
//			double p = (Double) n.getNodeData().getAttributes()
//					.getValue(pag.getIndex()); // pageRank
//			float a = (Float) n.getNodeData().getAttributes()
//					.getValue(auth.getIndex()); // auth
//			float h = (Float)  n.getNodeData().getAttributes()
//					.getValue(hub.getIndex()); // hub
//			double cc = (Double)  n.getNodeData().getAttributes()
//					.getValue(clustering.getIndex()); // clustering coefficient
//			double eg = (Double)  n.getNodeData().getAttributes()
//					.getValue(eigenvector.getIndex()); // eigen vector centrality

			double b = (Double) n.getNodeData().getAttributes()
					.getValue(betweeness.getIndex()); // betweeness
			double c = (Double) n.getNodeData().getAttributes()
					.getValue(closseness.getIndex()); // closseness
			double e = (Double) n.getNodeData().getAttributes()
					.getValue(eccentricity.getIndex()); // eccentricity
//			double cl = (Double) n.getNodeData().getAttributes()
//					.getValue(clustering.getIndex()); // clustering
//			int m = (Integer) n.getNodeData().getAttributes()
//					.getValue(modularityclass.getIndex()); // modularityclass

			properties.put("id", attr.getValue(0));
			properties.put("ename", attr.getValue(3));
			properties.put("etype", attr.getValue(2));
			//properties.put("degree", d);

			properties.put("eccentricity", e);
			properties.put("closeness", c);
			properties.put("betweeness", b);

//			properties.put("authority", a);
//			properties.put("hub", h);
//			properties.put("pagerank", p);
//			properties.put("cc", cc);
//			properties.put("eigenvector", eg);
//			properties.put("clustering", cl);
//			properties.put("modularityclass", m);
			//properties.put("Label", n.getNodeData().getAttributes().getValue("ID"));
			nodes.add(properties);

			// Write values to nodes
			System.out.println("e: "+e+"\tb: "+b);
			n.getNodeData().getAttributes().setValue("closnesscentrality", c);
			
		}

		System.out.println("Begin Building GEXF from Neo4j");
		System.out.println("Fetching graph...");
		
		Neo4j2Graph newgraph = new Neo4j2Graph(graphDb);
		Iterable<Vertex> vertex = newgraph.getVertices();//
		for (Vertex v : vertex) {
			v.setProperty("Label", v.getProperty("ID"));
			v.setProperty("Eccentricity", properties.get("eccentricity"));
//			v.setProperty("closenesscentrality",properties.get("closeness"));
//			v.setProperty("betweenesscentrality",properties.get("betweeness"));
//			v.setProperty("clustering",properties.get("clustering"));
//			v.setProperty("eigencentrality",properties.get("eigenvector"));
//			v.setProperty("modularity_class",properties.get("modularityclass"));
//			System.out.print( v.getProperty("ID")+ "\t"
//					+  v.getProperty("Eccentricity"));
//			System.out.println("\t"
//					+  v.getProperty("closenesscentrality"));
		}
		try {
			GraphMLWriter.outputGraph(newgraph,
					"C:\\Users\\Thanu\\Documents\\atom.gexf");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally{
			registerShutdownHook(graphDb);
		}
	}
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

}