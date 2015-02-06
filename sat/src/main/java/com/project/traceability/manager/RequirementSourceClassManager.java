/**
 *
 */
package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.GUI.CompareWindow;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.semanticAnalysis.SynonymWords;
import com.project.traceability.model.WordsMap;
import com.project.traceability.utils.Constants.ImageType;

/**
 * 13 Nov 2014
 *
 * @author K.Kamalan
 *
 */
public class RequirementSourceClassManager {

    static List<String> sourceCodeClasses = new ArrayList<String>();
    static List<String> requirementClasses = new ArrayList<String>();
    static List<String> relationNodes = new ArrayList<String>();

    static String projectPath;
    static TableItem tableItem;
    static TreeItem classItem;

    static Image exactImage = new Image(CompareWindow.display, PropertyFile.imagePath + "/" + "exact.jpg");
    static Image violateImage = new Image(CompareWindow.display, PropertyFile.imagePath + "/" + "violation.jpg");

    /**
     * check whether the requirement classes are implemented in sourcecode
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<String> compareClassNames(String projectPath) {
        RequirementSourceClassManager.projectPath = projectPath;
        requirementClasses = ClassManager.getReqClassName(projectPath);
        RequirementsManger.readXML(projectPath);
        Map<String, ArtefactElement> reqMap = RequirementsManger.requirementArtefactElements;
        Iterator<Entry<String, ArtefactElement>> requirementIterator = reqMap
                .entrySet().iterator();
        SourceCodeArtefactManager.readXML(projectPath);
        Map<String, ArtefactElement> sourceMap = SourceCodeArtefactManager.sourceCodeAretefactElements;
        Iterator<Entry<String, ArtefactElement>> sourceIterator = null;

        if (CompareWindow.tree != null && HomeGUI.isComaparing) {
            TreeColumn column1 = new TreeColumn(CompareWindow.tree, SWT.LEFT);
            column1.setText("SourceCodeXML File");
            column1.setWidth(300);

            TreeColumn column2 = new TreeColumn(CompareWindow.tree, SWT.LEFT);
            column2.setText("RequirementsXML File");
            column2.setWidth(300);
        }
        while (requirementIterator.hasNext()) {
            Map.Entry pairs = requirementIterator.next();
            ArtefactElement reqArtefactElement = (ArtefactElement) pairs
                    .getValue();
            String name = reqArtefactElement.getName();
            List<ArtefactSubElement> reqAttributeElements = reqArtefactElement
                    .getArtefactSubElements();
            if (reqArtefactElement.getType().equalsIgnoreCase("Class")) {
                sourceIterator = sourceMap.entrySet().iterator();
                while (sourceIterator.hasNext()) {
                    Map.Entry pairs1 = sourceIterator.next();
                    ArtefactElement sourceArtefactElement = (ArtefactElement) pairs1
                            .getValue();
                    WordsMap w6 = new WordsMap();
                    w6 = SynonymWords.checkSymilarity(sourceArtefactElement.getName(),
                            name, reqArtefactElement.getType());
                    if (sourceArtefactElement.getType().equalsIgnoreCase(
                            "Class")
                            && (sourceArtefactElement.getName()
                            .equalsIgnoreCase(name) | w6.isIsMatched())) {
                        compareSubElements(classItem, reqArtefactElement,
                                sourceArtefactElement);
                        sourceMap.remove(sourceArtefactElement
                                .getArtefactElementId());
                        reqMap.remove(reqArtefactElement.getArtefactElementId());
                        requirementIterator = reqMap.entrySet().iterator();
                        break;
                    }
                }
            }
        }

        RelationManager.addLinks(relationNodes);
        if (sourceMap.size() > 0 || reqMap.size() > 0) {
            requirementIterator = reqMap.entrySet().iterator();
            sourceIterator = sourceMap.entrySet().iterator();

            while (requirementIterator.hasNext()) {
                Map.Entry<String, ArtefactElement> artefact = requirementIterator
                        .next();
                if (CompareWindow.tree != null
                        && !CompareWindow.shell.isDisposed() && HomeGUI.isComaparing) {
                    TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
                    item.setText(1, artefact.getValue().getName());
                    item.setData("1", artefact.getValue());
                    item.setImage(1, violateImage);
                    item.setForeground(Display.getDefault().getSystemColor(
                            SWT.COLOR_RED));
                    addSubItems(1, item, artefact.getValue()
                            .getArtefactSubElements());

                }
            }

            while (sourceIterator.hasNext()) {
                Map.Entry<String, ArtefactElement> artefact = sourceIterator
                        .next();

                if (CompareWindow.tree != null
                        && !CompareWindow.shell.isDisposed() && HomeGUI.isComaparing) {
                    TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
                    item.setText(0, artefact.getValue().getName());
                    item.setData("0", artefact.getValue());
                    item.setImage(0, violateImage);
                    item.setForeground(Display.getDefault().getSystemColor(
                            SWT.COLOR_RED));
                    addSubItems(0, item, artefact.getValue()
                            .getArtefactSubElements());
                }
            }
        }

        return relationNodes;
    }

    @SuppressWarnings("rawtypes")
    public static int compareClassCount() {
        SourceCodeArtefactManager.readXML(projectPath);
        RequirementsManger.readXML(projectPath);
        Iterator it = SourceCodeArtefactManager.sourceCodeAretefactElements
                .entrySet().iterator();
        int countSourceClass = 0;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            ArtefactElement artefactElement = (ArtefactElement) pairs
                    .getValue();
            if (artefactElement.getType().equalsIgnoreCase("Class")) {
                countSourceClass++;
            }
            List<ArtefactSubElement> artefactSubElements = artefactElement
                    .getArtefactSubElements();
            it.remove(); // avoids a ConcurrentModificationException
        }
        // UMLArtefactManager.readXML();
        Iterator it1 = RequirementsManger.requirementArtefactElements
                .entrySet().iterator();
        int countReqClass = 0;
        while (it1.hasNext()) {
            Map.Entry pairs = (Entry) it1.next();
            ArtefactElement artefactElement = (ArtefactElement) pairs
                    .getValue();
            if (artefactElement.getType().equalsIgnoreCase("Class")) {
                countReqClass++;
            }
            List<ArtefactSubElement> artefactSubElements = artefactElement
                    .getArtefactSubElements();
            it1.remove(); // avoids a ConcurrentModificationException
        }

        if (countSourceClass == countReqClass) {
            System.out.println("class compared");
        }
        return countSourceClass;
    }

    public static void addSubItems(int column, TreeItem item,
            List<ArtefactSubElement> list) {
        for (int i = 0; i < list.size(); i++) {
            TreeItem subItem = new TreeItem(item, SWT.NONE);
            subItem.setText(column, list.get(i).getName());
            subItem.setData("" + column + "", list.get(i));
        }
    }

    public static void compareSubElements(TreeItem classItem,
            ArtefactElement reqArtefactElement,
            ArtefactElement sourceArtefactElement) {
        if (CompareWindow.tree != null && !CompareWindow.tree.isDisposed() && HomeGUI.isComaparing) {
            classItem = new TreeItem(CompareWindow.tree, SWT.NONE);
            classItem.setText(0, sourceArtefactElement.getName());
            classItem.setData("0", sourceArtefactElement);
            classItem.setImage(exactImage);
            classItem.setForeground(Display.getDefault().getSystemColor(
                    SWT.COLOR_DARK_BLUE));
            classItem.setText(1, reqArtefactElement.getName());
            classItem.setData("1", reqArtefactElement);

        }
        relationNodes.add(reqArtefactElement.getArtefactElementId().substring(
                reqArtefactElement.getArtefactElementId().indexOf("RQ")));
        relationNodes.add("Req Class To Source Class");
        relationNodes.add(sourceArtefactElement.getArtefactElementId());

        ArrayList<ArtefactSubElement> reqAttributesList = new ArrayList<ArtefactSubElement>();
        ArrayList<ArtefactSubElement> reqMethodsList = new ArrayList<ArtefactSubElement>();

        ArrayList<ArtefactSubElement> sourceAttributesList = new ArrayList<ArtefactSubElement>();
        ArrayList<ArtefactSubElement> sourceMethodsList = new ArrayList<ArtefactSubElement>();

        ArrayList<WordsMap> methodWordsMapList = new ArrayList<WordsMap>();
        ArrayList<WordsMap> attributeWordsMapList = new ArrayList<WordsMap>();

        List<ArtefactSubElement> sourceAttributeElements = sourceArtefactElement
                .getArtefactSubElements();
        List<ArtefactSubElement> reqAttributeElements = reqArtefactElement
                .getArtefactSubElements();
        for (int i = 0; i < sourceAttributeElements.size(); i++) {
            ArtefactSubElement sourceAttribute = sourceAttributeElements.get(i);
            for (int j = 0; j < reqAttributeElements.size(); j++) {
                ArtefactSubElement requElement = reqAttributeElements.get(j);

                WordsMap w7 = new WordsMap();


                w7 = SynonymWords.checkSymilarity(sourceAttribute.getName(),
                        requElement.getName(), sourceAttribute.getType(), requElement.getType(),
                        requirementClasses);
                if (w7.isIsMatched()) {
                    System.out.println(requElement.getSubElementId().indexOf("RQ"));
                    System.out.println(requElement.getSubElementId().substring(requElement.getSubElementId().indexOf("RQ")));
                    relationNodes.add(requElement.getSubElementId().substring(requElement.getSubElementId().indexOf("RQ")));
                    relationNodes.add("Req "+requElement.getType()+" To Source "+ sourceAttribute.getType());
                    relationNodes.add(sourceAttribute.getSubElementId());

                    if (CompareWindow.tree != null
                            && !CompareWindow.tree.isDisposed() && HomeGUI.isComaparing) {
                        if (requElement.getType().equalsIgnoreCase("Field")) {
                            sourceAttributesList.add(sourceAttribute);
                            reqAttributesList.add(requElement);
                            attributeWordsMapList.add(w7);
                        } else if ((requElement.getType())
                                .equalsIgnoreCase("Method")) {
                            sourceMethodsList.add(sourceAttribute);
                            reqMethodsList.add(requElement);
                            methodWordsMapList.add(w7);
                        }

                        sourceAttributeElements.remove(sourceAttribute);
                        reqAttributeElements.remove(requElement);
                        i--;
                        j--;
                        break;
                    }
                }
            }
        }
        if (CompareWindow.tree != null && !CompareWindow.tree.isDisposed() && HomeGUI.isComaparing) {
            TreeItem subAttribute = new TreeItem(classItem, SWT.NONE);
            subAttribute.setText("Attributes");
            subAttribute.setForeground(Display.getDefault().getSystemColor(
                    SWT.COLOR_GREEN));
            for (int k = 0; k < sourceAttributesList.size(); k++) {
                TreeItem subItem = new TreeItem(subAttribute, SWT.NONE);
                subItem.setText(0, sourceAttributesList.get(k).getName());
                subItem.setData("0", sourceAttributesList.get(k));
                subItem.setImage(0, ImageType.getImage(attributeWordsMapList.get(k)).getValue());
                subItem.setText(1, reqAttributesList.get(k).getName());
                subItem.setData("1", reqAttributesList.get(k));
                subItem.setImage(1, ImageType.getImage(attributeWordsMapList.get(k)).getValue());
            }

            TreeItem subMethod = new TreeItem(classItem, SWT.NONE);
            subMethod.setText("Methods");
            subMethod.setForeground(Display.getDefault().getSystemColor(
                    SWT.COLOR_GREEN));
            for (int k = 0; k < sourceMethodsList.size(); k++) {
                TreeItem subItem = new TreeItem(subMethod, SWT.NONE);
                subItem.setText(0, sourceMethodsList.get(k).getName());
                subItem.setData("0", sourceMethodsList.get(k));
                subItem.setImage(0, ImageType.getImage(methodWordsMapList.get(k)).getValue());
                subItem.setText(1, reqMethodsList.get(k).getName());
                subItem.setData("1", reqMethodsList.get(k));
                subItem.setImage(1, ImageType.getImage(methodWordsMapList.get(k)).getValue());
            }
            if (reqAttributeElements.size() > 0) {
                for (ArtefactSubElement model : reqAttributeElements) {
                    if (model.getType().equalsIgnoreCase("Field")) {
                        TreeItem subItem = new TreeItem(subAttribute, SWT.NONE);
                        subItem.setText(1, model.getName());
                        subItem.setData("1", model);
                        subItem.setImage(1, violateImage);
                        subItem.setForeground(Display.getDefault()
                                .getSystemColor(SWT.COLOR_RED));
                    } else if (model.getType().equalsIgnoreCase("Method")) {
                        TreeItem subItem = new TreeItem(subMethod, SWT.NONE);
                        subItem.setText(1, model.getName());
                        subItem.setData("1", model);
                        subItem.setImage(1, violateImage);
                        subItem.setForeground(Display.getDefault()
                                .getSystemColor(SWT.COLOR_RED));
                    }

                }

            }
            if (sourceAttributeElements.size() > 0) {
                for (ArtefactSubElement model : sourceAttributeElements) {
                    if (model.getType().equalsIgnoreCase("Field")) {
                        TreeItem subItem = new TreeItem(subAttribute, SWT.NONE);
                        subItem.setText(0, model.getName());
                        subItem.setData("0", model);
                        subItem.setImage(0, violateImage);
                        subItem.setForeground(Display.getDefault()
                                .getSystemColor(SWT.COLOR_RED));
                    } else if (model.getType().equalsIgnoreCase("Method")) {
                        TreeItem subItem = new TreeItem(subMethod, SWT.NONE);
                        subItem.setText(0, model.getName());
                        subItem.setData("0", model);
                        subItem.setImage(0, violateImage);
                        subItem.setForeground(Display.getDefault()
                                .getSystemColor(SWT.COLOR_RED));
                    }
                }

            }

        }
    }
}
