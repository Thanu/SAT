package com.project.traceability.db;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.neo4j.plugin.api.Neo4jImporter;
import org.gephi.neo4j.plugin.api.RelationshipDescription;
import org.gephi.neo4j.plugin.impl.Neo4jImporterImpl;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.openide.util.Lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

public class GraphConverter {
	private final static Logger LOG = Logger.getLogger(GraphConverter.class.getName());
	public static void main(String[] args) {
		GraphConverter gc = new GraphConverter();
		gc.convertToGexf();
	}

	public void convertToGexf() {
		LOG.info("Begin Building GEXF from Neo4j");
		LOG.info("Fetching graph...");
		
		// Get the database
		GraphDatabaseService graphDb = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder("D:\\Neo4j\\atomdb.graphdb")
				.newGraphDatabase();
		
		// Import by traversing the entire ownership network
		LOG.info("Importing Ownership Network from Neo4j Database...");
		final Collection<RelationshipDescription> relationshipDescription = new ArrayList<RelationshipDescription>();
		relationshipDescription.add(new RelationshipDescription(
				 GraphDB.RelTypes.SOURCE_TO_TARGET, Direction.BOTH));
		final Neo4jImporter importer = new Neo4jImporterImpl();

		// Load the graph in memory
		importer.importDatabase(graphDb);//,1, TraversalOrder.BREADTH_FIRST,Integer.MAX_VALUE, relationshipDescription);

		// Grab the graph that was loaded from the importer
		final ProjectController projectController = Lookup.getDefault().lookup(
				ProjectController.class);
		final Workspace workspace = projectController.getCurrentWorkspace();
		final GraphModel graph = Lookup.getDefault()
				.lookup(GraphController.class).getModel(workspace);
		LOG.info("Graph Imported.  Nodes: "
				+ graph.getDirectedGraph().getNodeCount() + "Edges: "
				+ graph.getDirectedGraph().getEdgeCount());

	}
}
