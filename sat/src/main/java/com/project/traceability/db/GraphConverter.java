package com.project.traceability.db;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.neo4j.plugin.api.Neo4jImporter;
import org.gephi.neo4j.plugin.api.RelationshipDescription;
import org.gephi.neo4j.plugin.api.TraversalOrder;
import org.gephi.neo4j.plugin.impl.Neo4jImporterImpl;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.openide.util.Lookup;

import java.io.File;
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
		// Get the database and the owner index
		GraphDatabaseService graphDb = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder("D:\\Neo4j\\atomdb.graphdb")
				.newGraphDatabase();
		//owned_addresses = graphDb.index().forNodes(OWNED_ADDRESS_HASH);

		// Find an node id. This will be the start location of the graph
		// traversal and is guarenteed to be part of the graph.
		//final long ownerId = getKnownOwnerId();

		// Import by traversing the entire ownership network along the
		// "transfers" edges
		LOG.info("Importing Ownership Network from Neo4j Database...");
		final Collection<RelationshipDescription> relationshipDescription = new ArrayList<RelationshipDescription>();
		relationshipDescription.add(new RelationshipDescription(
				 GraphDB.RelTypes.SOURCE_TO_TARGET, Direction.BOTH));
		final Neo4jImporter importer = new Neo4jImporterImpl();

		// Load the graph in memory
		importer.importDatabase(graphDb,1, TraversalOrder.BREADTH_FIRST,
				Integer.MAX_VALUE, relationshipDescription);

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
