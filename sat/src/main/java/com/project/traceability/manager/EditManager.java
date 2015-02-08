/**
 * 
 */
package com.project.traceability.manager;

import java.io.File;
import java.io.IOException;

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

import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;

/**
 * @author Gitanjali Dec 1, 2014
 */
public class EditManager {

	public static void main(String args[]) {
		// addLink("Customer", "accountNumber");
	}

	public static void addLink(Object className,
			Object nextName) {
		System.out.println(className + " " + nextName);
		
		File file = new File(HomeGUI.projectPath + "\\Relations.xml");
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = (Document) dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList artefactNodeList = doc.getElementsByTagName("Relations");
			System.out.println(artefactNodeList.getLength());

			for (int i = 0; i < artefactNodeList.getLength(); i++) {

				Node artefactNode = (Node) artefactNodeList.item(i);
				if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {

					NodeList artefactElementList = doc
							.getElementsByTagName("Relation");
					Node artefactElementNode = (Node) artefactElementList
							.item(artefactElementList.getLength() - 1);
					Element artefact = (Element) artefactElementNode;
					String artefact_id = artefact.getAttribute("id");
					int numId = Integer.parseInt(artefact_id);
					int newId = ++numId;
					Element school = doc.createElement("Relation");
					artefactElementNode.getParentNode().appendChild(school);

					// add attributes to school
					Attr attribute = doc.createAttribute("id");
					attribute.setValue("" + newId + "");
					school.setAttributeNode(attribute);
					
					//add source code
					Element firstname = doc.createElement("SourceNode");
					if(className instanceof ArtefactElement)
						firstname.appendChild(doc.createTextNode(((ArtefactElement) className)
								.getArtefactElementId()));
					else if(className instanceof ArtefactSubElement)
						firstname.appendChild(doc.createTextNode(((ArtefactSubElement) className)
								.getSubElementId()));
					school.appendChild(firstname);
					
					//add relation description
					String desString = ""; 
					Element description = doc.createElement("RelationPath");
					if(className.getClass().equals(ArtefactSubElement.class) && nextName.getClass().equals(ArtefactSubElement.class)){						
						desString = createSubElementDescription(className, nextName);
						if(desString.equals(""))
							desString = createSubElementDescription(nextName, className);
						description.appendChild(doc.createTextNode(desString));
					} else if(className.getClass().equals(ArtefactElement.class) && nextName.getClass().equals(ArtefactElement.class)){
						System.out.println("}POIYGBNUIIUhighugfyuf");
							desString = createArefactDescription(className, nextName);
							if(desString.equals(""))
								desString = createArefactDescription(nextName, className);
							description.appendChild(doc.createTextNode(desString));
					}
					school.appendChild(description);

					// lastname elements
					Element lastname = doc.createElement("TargetNode");
					if(className instanceof ArtefactElement)
							lastname.appendChild(doc.createTextNode(((ArtefactElement) nextName)
									.getArtefactElementId()));
					else if(className instanceof ArtefactSubElement)
						lastname.appendChild(doc.createTextNode(((ArtefactSubElement) nextName)
								.getSubElementId()));
					school.appendChild(lastname);
					System.out.println(school.getAttribute("id"));
				}
			}
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			//System.out.println(PropertyFile.xmlFilePath);
			StreamResult result = new StreamResult(new File(HomeGUI.projectPath + "\\Relations.xml").getPath());
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
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
	
	public static void deleteLink(TreeItem treeItem){
		Object obj1 = treeItem.getData("0");
		Object obj2 = treeItem.getData("1");
		String sourceId = null;
		String targetId = null;
		if(obj1 instanceof ArtefactElement && obj2 instanceof ArtefactElement){
			sourceId = ((ArtefactElement) obj1).getArtefactElementId();
			targetId = ((ArtefactElement) obj2).getArtefactElementId();
		} else if(obj1 instanceof ArtefactSubElement && obj2 instanceof ArtefactSubElement){
			sourceId = ((ArtefactSubElement) obj1).getSubElementId();
			targetId = ((ArtefactSubElement) obj2).getSubElementId();
		}
		
		if(sourceId.length() > 4)
			sourceId = sourceId.substring(sourceId.length() - 3);
		if(targetId.length() > 4)
			targetId = targetId.substring(targetId.length() - 3);
		boolean found = false;
       
        File file = new File(HomeGUI.projectPath + "\\Relations.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document) dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList artefactNodeList = doc
                    .getElementsByTagName("Relations");

            for (int i = 0; i < artefactNodeList.getLength() && found != true; i++) {

                Node artefactNode = (Node) artefactNodeList.item(i);
                if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {

                    NodeList artefactElementList = doc
                            .getElementsByTagName("Relation");
  
                    for (int j = 0; j < artefactElementList.getLength() && !found; j++) {
                    	
                       Node artefactElementNode = (Node) artefactElementList
                                .item(j);
                        Element artefact = (Element) artefactElementNode;
                        String source = artefact.getElementsByTagName("SourceNode").item(0).getTextContent();
                        String target = artefact.getElementsByTagName("TargetNode").item(0).getTextContent();
                        String artefact_id = artefact.getAttribute("id");
                        if ((source.equals(sourceId) || source.equals(targetId)) && (target.equals(sourceId) || target.equals(targetId))) {
                        	System.out.println("PPPPP");
                            artefactElementNode.getParentNode().removeChild(artefactElementNode);
                            found = true;
                            System.out.println("Artefact " + artefact_id + " deleted");
                            break;
                        }
                        
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(HomeGUI.projectPath + "\\Relations.xml").getPath());
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
	
	private static String createArefactDescription(Object className, Object nextName){
		String desString = "";
		if(((ArtefactElement) className).getArtefactElementId().contains("RQ") && 
				((ArtefactElement) nextName).getArtefactElementId().contains("SC"))
			desString += "ReqClassToSourceClass";
		else if(((ArtefactElement) className).getArtefactElementId().contains("RQ") && 
				((ArtefactElement) nextName).getArtefactElementId().contains("D"))
			desString += "ReqClassToUMLClass";
		else if(((ArtefactElement) className).getArtefactElementId().contains("D") && 
				((ArtefactElement) nextName).getArtefactElementId().contains("SC"))
			desString += "UMLClassToSourceClass";
		return desString;
	}
	
	private static String createSubElementDescription(Object className, Object nextName){
		String desString = "";
		if(((ArtefactSubElement) className).getSubElementId().contains("RQ") && 
				((ArtefactSubElement) nextName).getSubElementId().contains("SC"))
			if(className instanceof AttributeModel)
				desString += "ReqAttributToSourceAttribute";
			else 
				desString += "ReqMethodToSourceMethod";
		else if(((ArtefactSubElement) className).getSubElementId().contains("RQ") && 
				((ArtefactSubElement) nextName).getSubElementId().contains("D"))
			if(className instanceof AttributeModel)
				desString += "ReqAttributeToUMLAttribute";
			else 
				desString += "ReqMethodToUMLMethod";
		else if(((ArtefactSubElement) className).getSubElementId().contains("D") && 
				((ArtefactSubElement) nextName).getSubElementId().contains("SC"))
			if(className instanceof AttributeModel)
				desString += "UMLAttributeToSourceAttribute";
			else 
				desString += "UMLMethodToSourceMethod";

		return desString;
	}
}
