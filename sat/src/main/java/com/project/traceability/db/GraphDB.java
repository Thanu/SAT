package com.project.traceability.db;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.model.MethodModel;
import com.project.traceability.model.ParameterModel;
import com.project.traceability.model.RequirementModel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;

/**
 * Model to add data to graph DB.
 * 
 * @author Thanu
 * 
 */
public class GraphDB {

	/**
	 * Define relationship type.
	 * 
	 * @author Thanu
	 * 
	 */
	public static enum RelTypes implements RelationshipType {
		SUB_ELEMENT("Sub Element"), SOURCE_TO_TARGET("Source To Target");
		private final String value;

		private RelTypes(String val) {
			this.value = val;
		}

		@Override
		public String toString() {
			return value;
		}

		public String getValue() {
			return value;
		}

		public static RelTypes parseEnum(final String val) {

			RelTypes relType = null;
			for (RelTypes type : RelTypes.values()) {
				if (type.getValue().equals(val)) {
					relType = type;
				}
			}
			return relType;
		}
	}

	/**
	 * Define Node types.
	 * 
	 * @author Thanu
	 * 
	 */
	private static enum NodeTypes implements RelationshipType {
		CLASS("Class"), FIELD("Field"), METHOD("Method"), UMLATTRIBUTE(
				"UMLAttribute"), UMLOPERATION("UMLOperation");
		private final String value;

		private NodeTypes(String val) {
			this.value = val;
		}

		@Override
		public String toString() {
			return value;
		}

		public String getValue() {
			return value;
		}

		public static NodeTypes parseEnum(final String val) {

			NodeTypes nodeType = null;
			for (NodeTypes type : NodeTypes.values()) {
				if (type.getValue().equals(val)) {
					nodeType = type;
				}
			}
			return nodeType;
		}
	}

	GraphDatabaseService graphDb;
	Relationship relationship;

	public void initiateGraphDB() {

		graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(PropertyFile.graphDbPath).newGraphDatabase();
		Transaction tx = graphDb.beginTx();

		try {
			//cleanUp(graphDb);
			tx.success();

		} finally {
			tx.finish();
		}
		registerShutdownHook(graphDb);
	}

	public void addNodeToGraphDB(Map<String, ArtefactElement> aretefactElements) {
		Transaction tx = graphDb.beginTx();
		try {

			Iterator<Entry<String, ArtefactElement>> iterator = aretefactElements
					.entrySet().iterator();

			while (iterator.hasNext()) {

				Map.Entry pairs = iterator.next();
				ArtefactElement artefactElement = (ArtefactElement) pairs
						.getValue();
				Label myLabel = DynamicLabel.label(artefactElement.getType());

				IndexManager index = graphDb.index();
				Index<Node> artefacts = index.forNodes("ArtefactElement");

				IndexHits<Node> hits = artefacts.get("ID",
						artefactElement.getArtefactElementId());
				Node node = hits.getSingle();
				if (node == null) {
					Node n = graphDb.createNode();

					n.addLabel(myLabel);
					n.setProperty("ID", artefactElement.getArtefactElementId());
					n.setProperty("Name", artefactElement.getName());
					n.setProperty("Type", artefactElement.getType());
					artefacts.add(n, "ID", n.getProperty("ID"));
					List<ArtefactSubElement> artefactsSubElements = artefactElement
							.getArtefactSubElements();

					for (int i = 0; i < artefactsSubElements.size(); i++) {
						Node m = graphDb.createNode();
						ArtefactSubElement temp = artefactsSubElements.get(i);
						myLabel = DynamicLabel.label(temp.getType());
						m.addLabel(myLabel);
						m.setProperty("ID", temp.getSubElementId());
						m.setProperty("Name", temp.getName());
						m.setProperty("Type", temp.getType());

						if (null != temp.getVisibility()) {
							m.setProperty("Visibility", temp.getVisibility());
						}
						if (temp.getType().equalsIgnoreCase("UMLOperation")
								|| temp.getType().equalsIgnoreCase("Method")) {
							MethodModel mtemp = (MethodModel) temp;
							if (null != mtemp.getReturnType()) {
								m.setProperty("Return Type",
										mtemp.getReturnType());
							}
							if (null != mtemp.getParameters()) {
								List<ParameterModel> params = mtemp
										.getParameters();
								String parameters = "";
								for (int p = 0; p < params.size(); p++) {
									parameters += params.get(p).getName() + ":"
											+ params.get(p).getVariableType();
									if (p < params.size() - 1)
										parameters += ",";
								}
								m.setProperty("Parameters", parameters);
							}
							if (null != mtemp.getContent()) {
								m.setProperty("Content", mtemp.getContent());
							}
						} else if (temp.getType().equalsIgnoreCase(
								"UMLAttribute")
								|| temp.getType().equalsIgnoreCase("Field")) {
							AttributeModel mtemp = (AttributeModel) temp;
							if (null != mtemp.getVariableType()) {
								m.setProperty("Variable Type",
										mtemp.getVariableType());
							}

						}
						artefacts.add(m, "ID", m.getProperty("ID"));
						relationship = n.createRelationshipTo(m,
								RelTypes.SUB_ELEMENT);
						relationship.setProperty("message",
								RelTypes.SUB_ELEMENT.getValue());
					}
				} else {
					if (!node.getProperty("Name").equals(
							artefactElement.getName())) {
						System.out.println("Node name updated");
					} else if (!node.getProperty("Type").equals(
							artefactElement.getType())) {
						System.out.println("Node type updated");
					} else {
						Iterator<Relationship> relations = node
								.getRelationships(RelTypes.SUB_ELEMENT)
								.iterator();
						List<ArtefactSubElement> artefactsSubElements = artefactElement
								.getArtefactSubElements();

						while (relations.hasNext()) {
							Node test = relations.next().getOtherNode(node);
							for (int i = 0; i < artefactsSubElements.size(); i++) {
								if (test.getProperty("ID").equals(
										artefactsSubElements.get(i)
												.getSubElementId())) {
									System.out
											.println("SubElement already exists.....");
									break;
								}
							}
						}
						System.out.println("Node already exists.....");
					}
				}
			}
			tx.success();

		} finally {
			tx.finish();
		}
	}

	public void addRelationTOGraphDB(List<String> relation) {
		Transaction tx = graphDb.beginTx();
		try {
			IndexManager index = graphDb.index();
			Index<Node> artefacts = index.forNodes("ArtefactElement");

			for (int i = 0; i < relation.size(); i++) {
				IndexHits<Node> hits = artefacts.get("ID", relation.get(i));
				Node source = hits.getSingle();
				hits = artefacts.get("ID", relation.get(++i));
				Node target = hits.getSingle();

				Iterator<Relationship> relations = source.getRelationships()
						.iterator();
				boolean exist = false;
				while (relations.hasNext()) {
					if (relations.next().getOtherNode(source).equals(target)) {
						exist = true;
						System.out.println("Relationship already exists.....");
					}
				}
				if (!exist) {
					relationship = source.createRelationshipTo(target,
							RelTypes.SOURCE_TO_TARGET);
					relationship.setProperty("message",
							RelTypes.SOURCE_TO_TARGET.getValue());
				}
			}
			tx.success();			
		} finally {
			tx.finish();
		}
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void cleanUp(final GraphDatabaseService graphDb) {
		IndexManager index = graphDb.index();
		Index<Node> artefact = index.forNodes("ArtefactElement");
		artefact.delete();
		for (Node node : graphDb.getAllNodes()) {
			for (Relationship rel : node.getRelationships()) {
				rel.delete();
			}
			artefact.remove(node);
			node.delete();
		}
	}

	public void addRequirementsNodeToGraphDB(
			List<RequirementModel> requirementsAretefactElements) {
		Transaction tx = graphDb.beginTx();
		try {

			for(int i=0;i<requirementsAretefactElements.size();i++){

				RequirementModel requirement = requirementsAretefactElements.get(i);
				
				Label myLabel = DynamicLabel.label(requirement.getType());

				IndexManager index = graphDb.index();
				Index<Node> artefacts = index.forNodes("ArtefactElement");

				IndexHits<Node> hits = artefacts.get("ID",requirement.getRequirementId());
				Node node = hits.getSingle();
				if (node == null) {
					Node n = graphDb.createNode();

					n.addLabel(myLabel);
					n.setProperty("ID", requirement.getRequirementId());
					n.setProperty("Name", requirement.getName());
					n.setProperty("Type", requirement.getType());
					n.setProperty("Content", requirement.getContent());
					n.setProperty("Priority", requirement.getPriority());
					n.setProperty("Title", requirement.getTitle());				
					artefacts.add(n, "ID", n.getProperty("ID"));
				} else {
					if (!node.getProperty("Name").equals(
							requirement.getName())) {
						System.out.println("Node name updated");
					} else if (!node.getProperty("Type").equals(
							requirement.getType())) {
						System.out.println("Node type updated");
					} else {
						System.out.println("Node already exists.....");
					}
				}
			}
			tx.success();

		} finally {
			tx.finish();
		}
	}

	public void generateGraphFile() {
		GraphFileGenerator preview = new GraphFileGenerator();
		preview.generateGraphFile(graphDb);
		//preview.updateGraphFile(graphDb);
                graphDb.shutdown();
	}

}
