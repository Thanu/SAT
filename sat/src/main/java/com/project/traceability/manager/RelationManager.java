package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.project.traceability.common.PropertyFile;

public class RelationManager {

	public static Document document;
	public static Element rootElement;
	
	public static void createXML(List<String> relationNodes) {
		try {

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();

			// define root elements
			document = documentBuilder.newDocument();
			rootElement = document.createElement("Relations");
			document.appendChild(rootElement);

			for(int i = 0, j = 1; i < relationNodes.size(); i++,j++){
			
			// define school elements
			Element school = document.createElement("Relation");
			rootElement.appendChild(school);

			// add attributes to school
			Attr attribute = document.createAttribute("id");
			attribute.setValue("" + j + "");
			school.setAttributeNode(attribute);
			//System.out.println(relationNodes.get(i));
			Element firstname = document.createElement("SourceNode");
			firstname.appendChild(document.createTextNode(relationNodes.get(i)));
			school.appendChild(firstname);

			// lastname elements
			Element lastname = document.createElement("TargetNode");
			lastname.appendChild(document.createTextNode(relationNodes.get(++i)));
			school.appendChild(lastname);
		}
			
			//System.out.println(relationNodes.size());
			

			// creating and writing to xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(PropertyFile.relationshipXMLPath);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(domSource, streamResult);

			System.out.println("File saved to specified path!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
	
}
