package com.project.traceability.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import scala.Array;

import com.project.traceability.GUI.NewProjectWindow;
import com.project.traceability.common.PropertyFile;

public class RelationManager {

	public static void createXML(List<String> relationNodes) {
		try {

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();

			// define root elements
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("Relations");
			document.appendChild(rootElement);
			System.out.println("start");

			// System.out.println(relationNodes.size());
			for (int i = 0, j = 1; i < relationNodes.size(); i++, j++) {

				// define school elements
				Element school = document.createElement("Relation");
				rootElement.appendChild(school);

				// add attributes to school
				Attr attribute = document.createAttribute("id");
				attribute.setValue("" + j + "");
				school.setAttributeNode(attribute);
				// System.out.println(relationNodes.get(i));
				Element firstname = document.createElement("SourceNode");
				firstname.appendChild(document.createTextNode(relationNodes
						.get(i)));
				school.appendChild(firstname);

				Element relationName = document.createElement("RelationPath");
				relationName.appendChild(document.createTextNode(relationNodes
						.get(++i)));
				school.appendChild(relationName);

				// lastname elements
				Element lastname = document.createElement("TargetNode");
				lastname.appendChild(document.createTextNode(relationNodes
						.get(++i)));
				school.appendChild(lastname);
			}

			// creating and writing to xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(
					PropertyFile.relationshipXMLPath);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(domSource, streamResult);

			System.out.println("File saved to specified path!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public static void createXML(String projectPath) {
		try {

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();

			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("Relations");
			document.appendChild(rootElement);
			System.out.println("start");

			// creating and writing to xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(projectPath
					+ "\\Relations.xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(domSource, streamResult);

			System.out.println("File saved to specified path!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public static void addLinks(List<String> relationNodes) {
		File file = new File(NewProjectWindow.projectPath + "\\Relations.xml");

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = (Document) dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList artefactNodeList = doc.getElementsByTagName("Relations");
			for (int i = 0; i < artefactNodeList.getLength(); i++) {

				Node artefactNode = (Node) artefactNodeList.item(i);
				Node relationNode;
				if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {

					NodeList artefactElementList = doc
							.getElementsByTagName("Relation");
					System.out.println(artefactElementList.getLength());
					System.out.println(artefactNodeList.getLength());
					int newId = 0;
					Node artefactElementNode = null;
					Element element = null;
					if (artefactElementList.getLength() == 0) {
						newId = 1;
						System.out.println("00000000000");
						relationNode = artefactNode;
					} else {
						artefactElementNode = (Node) artefactElementList
								.item(artefactElementList.getLength() - 1);
						Element artefact = (Element) artefactElementNode;
						String artefact_id = artefact.getAttribute("id");
						int numId = Integer.parseInt(artefact_id);
						newId = ++numId;
						relationNode = artefactElementNode.getParentNode();
					}
					for (int j = 0; j < relationNodes.size(); j++) {

						element = doc.createElement("Relation");
						relationNode.appendChild(element);

						// add attributes to school
						Attr attribute = doc.createAttribute("id");
						attribute.setValue("" + newId++ + "");
						element.setAttributeNode(attribute);
						Element firstname = doc.createElement("SourceNode");

						firstname.appendChild(doc.createTextNode(relationNodes
								.get(j++)));
						element.appendChild(firstname);

						Element relationName = doc
								.createElement("RelationPath");
						relationName.appendChild(doc
								.createTextNode(relationNodes.get(j++)));
						element.appendChild(relationName);

						// lastname elements
						Element lastname = doc.createElement("TargetNode");

						lastname.appendChild(doc.createTextNode(relationNodes
								.get(j)));
						element.appendChild(lastname);
					}

				}
			}
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			// System.out.println(PropertyFile.xmlFilePath);
			StreamResult result = new StreamResult(new File(
					NewProjectWindow.projectPath + "\\Relations.xml").getPath());
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}

	public static void readAll() {
		File file = new File(NewProjectWindow.projectPath + "\\Relations.xml");
		List<String> existingNodes = new ArrayList<String>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = (Document) dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList artefactNodeList = doc.getElementsByTagName("Relations");
			for (int i = 0; i < artefactNodeList.getLength(); i++) {

				Node artefactNode = (Node) artefactNodeList.item(i);
				Node relationNode;
				if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {

					NodeList artefactElementList = doc
							.getElementsByTagName("Relation");
					for (int j = 0; j < artefactElementList.getLength(); j++) {
						Node node = (Node) artefactElementList.item(j);
						Element artefact = (Element) node;
						String source = artefact
								.getElementsByTagName("SourceNode").item(0)
								.getTextContent();
						String description = artefact
								.getElementsByTagName("RelationPath").item(0)
								.getTextContent();
						String target = artefact
								.getElementsByTagName("TargetNode").item(0)
								.getTextContent();
						existingNodes.add(source);
                                                existingNodes.add(description);
                                                existingNodes.add(target);
					}

					Node artefactElementNode = null;
					Element element = null;
					artefactElementNode = (Node) artefactElementList
							.item(artefactElementList.getLength() - 1);
					Element artefact = (Element) artefactElementNode;
					String artefact_id = artefact.getAttribute("id");
					int numId = Integer.parseInt(artefact_id);

					relationNode = artefactElementNode.getParentNode();
				}
			}
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			// System.out.println(PropertyFile.xmlFilePath);
			StreamResult result = new StreamResult(new File(
					NewProjectWindow.projectPath + "\\Relations.xml").getPath());
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
