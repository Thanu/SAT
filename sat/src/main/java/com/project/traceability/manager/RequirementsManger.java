package com.project.traceability.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.map.HashedMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.RequirementModel;
import com.project.traceability.utils.Constants;
import com.project.traceability.utils.Constants.ArtefactType;

public class RequirementsManger {

	private ArtefactType artefactType = Constants.ArtefactType.REQUIREMENT;

	public static List<RequirementModel> requirementElements = null;
	public static List<ArtefactElement> expectedRequirementElements = null;
	public static Map<String, ArtefactElement> requirementArtefactElements = null; // find

	// using
	// nlp

	/**
	 * read UMLXml file and store data in a map
	 */
	public static Map<String, ArtefactElement> readXML() {
		// get the xml file
		File umlXmlFile = new File(PropertyFile.requirementXMLPath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document UMLDoc = (Document) dBuilder.parse(umlXmlFile);
			UMLDoc.getDocumentElement().normalize();

			NodeList artefactList = UMLDoc.getElementsByTagName("Artefact"); // find
																				// the
																				// "Artefact"
																				// tag
			System.out.println("********************"+artefactList.getLength());
			for (int temp = 0; temp < artefactList.getLength(); temp++) {

				Node artefactNode = (Node) artefactList.item(temp); // identify
																	// a node

				if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {

					NodeList artefactElementList = UMLDoc
							.getElementsByTagName("ArtefactElement"); // get all
																		// "Artefact"
																		// elements
					requirementElements = new ArrayList<RequirementModel>();
					RequirementModel requirement = null;
					for (int temp1 = 0; temp1 < artefactElementList.getLength(); temp1++) {

						Node artefactElementNode = (Node) artefactElementList
								.item(temp1);
						Element artefact = (Element) artefactElementNode;
						String id = artefact.getAttribute("id");
						String name = artefact.getAttribute("name");
						String title = artefact.getElementsByTagName("Title")
								.item(0).getTextContent();
						String content = artefact
								.getElementsByTagName("Content").item(0)
								.getTextContent();
						String priority = artefact
								.getElementsByTagName("Priority").item(0)
								.getTextContent();
						String type = artefact.getElementsByTagName("Type")
								.item(0).getTextContent();
						requirement = new RequirementModel(id, name, title,
								content, priority, type);
						requirementElements.add(requirement);
					}
					
					System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+requirementElements.size());

					NodeList intraConnectionsList = UMLDoc
							.getElementsByTagName("IntraConnections");
					readIntraConnectionsXML(intraConnectionsList);
					expectedRequirementElements = InfoExtractionManager
							.run(requirementElements);
					requirementArtefactElements = new HashMap<String, ArtefactElement>();
					for (int i = 0; i < expectedRequirementElements.size(); i++) {
						requirementArtefactElements.put(
								expectedRequirementElements.get(i)
										.getArtefactElementId(),
								expectedRequirementElements.get(i));
						System.out.println("££££££££££££££"+expectedRequirementElements.get(i).getArtefactElementId());
						System.out.println(expectedRequirementElements.get(i)
								.getName() + "*********"+expectedRequirementElements.get(i).getArtefactElementId());
						for (int j = 0; j < expectedRequirementElements.get(i)
								.getArtefactSubElements().size(); j++) {
							System.out.println(expectedRequirementElements
									.get(i).getArtefactSubElements().get(j)
									.getName()
									+ "___________"+expectedRequirementElements.get(i).getArtefactElementId());
						}
					}
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {

			e.printStackTrace();
		}
		return requirementArtefactElements;
	}

	public static void readIntraConnectionsXML(NodeList intraConnectionsList) {
		for (int temp1 = 0; temp1 < intraConnectionsList.getLength(); temp1++) {

			Node intraConnectionNode = (Node) intraConnectionsList.item(temp1);
			Element intraConnectionElement = (Element) intraConnectionNode;
			NodeList connectionsList = intraConnectionElement
					.getElementsByTagName("Connection");
		}

	}

}
