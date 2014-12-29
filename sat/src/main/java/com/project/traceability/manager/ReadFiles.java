/**
 *
 */
package com.project.traceability.manager;

import com.project.traceability.common.DefaultWords;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Gitanjali Nov 19, 2014
 */
public class ReadFiles {

    static String projectPath;
    public static List<String> relationNodes = new ArrayList<String>();
    
    public static void readFiles(String path) {
        projectPath = path;
        SourceCodeArtefactManager.readXML(projectPath);
        UMLArtefactManager.readXML(projectPath);
        RequirementsManger.readXML(projectPath);
        DefaultWords.getDefaultWords();
    }

    public static void deleteArtefact(String id) {
       projectPath = "E:/SATWork/abc/";

        boolean found = false;
        char type = id.toLowerCase().charAt(0);
        File file = new File(projectPath
                + "SourceCodeArtefactFile.xml");
        String xml = null;
        switch (type) {
            case 's':
                xml = "SourceCodeArtefactFile.xml";
                break;
            case 'r':
                xml = "RequirementArtefactFile.xml";
                break;
            case 'd':
                xml = "UMLArtefactFile.xml";
                break;
        }
        file = new File(projectPath, xml);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document) dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList artefactNodeList = doc
                    .getElementsByTagName("Artefact");

            for (int i = 0; i < artefactNodeList.getLength() && found != true; i++) {

                Node artefactNode = (Node) artefactNodeList.item(i);
                if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {

                    NodeList artefactElementList = doc
                            .getElementsByTagName("ArtefactElement");

                    //ArtefactElement artefactElement = null;
                    // List<ArtefactSubElement> artefactsSubElements = null;
                    for (int j = 0; j < artefactElementList.getLength() && !found; j++) {
                       // System.out.println(artefactNodeList.getLength() + " " + artefactElementList.getLength());

                        // artefactsSubElements = new ArrayList<ArtefactSubElement>();

                        Node artefactElementNode = (Node) artefactElementList
                                .item(j);
                        Element artefact = (Element) artefactElementNode;
                        String artefact_id = artefact.getAttribute("id");
                        if (id.equals(artefact_id)) {
                            artefactElementNode.getParentNode().removeChild(artefactElementNode);
                            found = true;
                            System.out.println("Artefact " + artefact_id + " deleted");
                            break;
                        }
                        NodeList artefactSubElementList = doc
                                .getElementsByTagName("ArtefactSubElement");
                        for (int k = 0; k < artefactSubElementList.getLength() && found != true; k++) {
                            //System.out.println(artefactNodeList.getLength() + " " + artefactElementList.getLength()+" "+artefactSubElementList.getLength());

                            Node artefactSubElementNode = (Node) artefactSubElementList
                                    .item(k);
                            Element subArtefact = (Element) artefactSubElementNode;
                            String sub_artefact_id = subArtefact.getAttribute("id");
                            if (id.equals(sub_artefact_id)) {
                                artefactSubElementNode.getParentNode().removeChild(artefactSubElementNode);
                                found = true;
                                System.out.println("Artefact " + sub_artefact_id + " deleted");
                                break;
                            }
                        }
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(projectPath, xml).getPath());
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
