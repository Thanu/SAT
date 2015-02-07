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
import com.project.traceability.GUI.NewProjectWindow;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;

/**
 * @author Gitanjali Dec 1, 2014
 */
public class EditManager {

	public static void main(String args[]) {
		// addLink("Customer", "accountNumber");
	}

	public static void addLink(Object className,
			Object subElementName) {
		System.out.println(className + " " + subElementName);
		
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
					Element description = doc.createElement("RelationPath");
					if(className instanceof ArtefactElement){
						description.appendChild(doc.createTextNode("Class"));
					}
					else if(className instanceof ArtefactSubElement){
						description.appendChild(doc.createTextNode("Sub Element"));
					}
					school.appendChild(description);

					// lastname elements
					Element lastname = doc.createElement("TargetNode");
					if(className instanceof ArtefactElement)
							lastname.appendChild(doc.createTextNode(((ArtefactElement) subElementName)
									.getArtefactElementId()));
					else if(className instanceof ArtefactSubElement)
						lastname.appendChild(doc.createTextNode(((ArtefactSubElement) subElementName)
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
}
