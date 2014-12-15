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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.GUI.CompareWindow;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.semanticAnalysis.SynonymWords;

/**
 * 13 Nov 2014
 *
 * @author K.Kamalan
 *
 */
public class RequirementSourceClassManager {

<<<<<<< HEAD
	static String projectPath;
	static TableItem tableItem;
	static TreeItem item;

	/**
	 * check whether the requirement classes are implemented in sourcecode
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> compareClassNames(String projectPath) {
		RequirementSourceClassManager.projectPath = projectPath;
		RequirementsManger.readXML(projectPath);
		Map<String, ArtefactElement> reqMap = RequirementsManger.requirementArtefactElements;
		Iterator<Entry<String, ArtefactElement>> requirementIterator = reqMap
				.entrySet().iterator();
		SourceCodeArtefactManager.readXML(projectPath);
		Map<String, ArtefactElement> sourceMap = SourceCodeArtefactManager.sourceCodeAretefactElements;
		Iterator<Entry<String, ArtefactElement>> sourceIterator = null;
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
					if (sourceArtefactElement.getType().equalsIgnoreCase(
							"Class")
							&& (SynonymWords
									.checkSymilarity(
											sourceArtefactElement.getName(),
											name,
											reqArtefactElement.getType()))) {

						relationNodes.add(reqArtefactElement
								.getArtefactElementId().substring(
										reqArtefactElement
												.getArtefactElementId()
												.length() - 3));
						relationNodes.add(sourceArtefactElement
								.getArtefactElementId());
						if (CompareWindow.tree != null
								&& !CompareWindow.tree.isDisposed()) {
							item = new TreeItem(CompareWindow.tree, SWT.NONE);
							item.setText(1, sourceArtefactElement.getName());
							item.setForeground(Display.getDefault()
									.getSystemColor(SWT.COLOR_DARK_BLUE));
							item.setText(0, reqArtefactElement.getName());
=======
    static List<String> sourceCodeClasses = new ArrayList<String>();
    static List<String> requirementClasses = new ArrayList<String>();
    static List<String> relationNodes = new ArrayList<String>();

    static String projectPath;
    static TableItem tableItem;
    static TreeItem item;

    /**
     * check whether the requirement classes are implemented in sourcecode
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<String> compareClassNames(String projectPath) {
        RequirementSourceClassManager.projectPath = projectPath;
        RequirementsManger.readXML(projectPath);
        requirementClasses = ClassManager.getReqClassName(projectPath);
        Map<String, ArtefactElement> reqMap = RequirementsManger.requirementArtefactElements;
        Iterator<Entry<String, ArtefactElement>> requirementIterator = reqMap
                .entrySet().iterator();
        SourceCodeArtefactManager.readXML(projectPath);
        Map<String, ArtefactElement> sourceMap = SourceCodeArtefactManager.sourceCodeAretefactElements;
        Iterator<Entry<String, ArtefactElement>> sourceIterator = null;
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
                    if (sourceArtefactElement.getType().equalsIgnoreCase(
                            "Class")
                            && (SynonymWords
                            .checkSymilarity(
                                    sourceArtefactElement.getName(),
                                    name,
                                    reqArtefactElement.getType()))) {

                        relationNodes.add(reqArtefactElement
                                .getArtefactElementId().substring(
                                        reqArtefactElement
                                        .getArtefactElementId()
                                        .length() - 3));
                        relationNodes.add(sourceArtefactElement
                                .getArtefactElementId());
                        if (CompareWindow.tree != null
                                && !CompareWindow.tree.isDisposed()) {
                            item = new TreeItem(CompareWindow.tree, SWT.NONE);
                            item.setText(1, sourceArtefactElement.getName());
                            item.setForeground(Display.getDefault()
                                    .getSystemColor(SWT.COLOR_DARK_BLUE));
                            item.setText(0, reqArtefactElement.getName());

                        }
>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94

                        ArrayList<String> reqAttributesList = new ArrayList<String>();
                        ArrayList<String> reqMethodsList = new ArrayList<String>();

                        ArrayList<String> sourceAttributesList = new ArrayList<String>();
                        ArrayList<String> sourceMethodsList = new ArrayList<String>();

                        List<ArtefactSubElement> sourceAttributeElements = sourceArtefactElement
                                .getArtefactSubElements();
                        for (int i = 0; i < sourceAttributeElements.size(); i++) {
                            ArtefactSubElement sourceAttribute = sourceAttributeElements
                                    .get(i);
                            for (int j = 0; j < reqAttributeElements.size(); j++) {
                                ArtefactSubElement requElement = reqAttributeElements
                                        .get(j);
                                if (SynonymWords.checkSymilarity(
                                        sourceAttribute.getName(),
                                        requElement.getName(),
                                        sourceAttribute.getType(), requirementClasses)) {
                                    relationNodes.add(requElement
                                            .getSubElementId().substring(
                                                    requElement.getSubElementId()
                                                    .length() - 3));
                                    relationNodes.add(sourceAttribute
                                            .getSubElementId());

<<<<<<< HEAD
						List<ArtefactSubElement> sourceAttributeElements = sourceArtefactElement
								.getArtefactSubElements();
						for (int i = 0; i < sourceAttributeElements.size(); i++) {
							ArtefactSubElement sourceAttribute = sourceAttributeElements
									.get(i);
							for (int j = 0; j < reqAttributeElements.size(); j++) {
								ArtefactSubElement requElement = reqAttributeElements
										.get(j);
								if (SynonymWords.checkSymilarity(
										sourceAttribute.getName(),
										requElement.getName(),
										sourceAttribute.getType(),sourceArtefactElement.getName())) {
									relationNodes.add(requElement
											.getSubElementId().substring(
													requElement.getSubElementId()
													.length() - 3));
									relationNodes.add(sourceAttribute
											.getSubElementId());
									
									if (CompareWindow.tree != null
											&& !CompareWindow.tree.isDisposed()) {
										if (requElement.getType()
												.equalsIgnoreCase("Field")) {
											sourceAttributesList
													.add(sourceAttribute
														.getName());
											reqAttributesList.add(requElement
													.getName());

										}
=======
                                    if (CompareWindow.tree != null
                                            && !CompareWindow.tree.isDisposed()) {
                                        if (requElement.getType()
                                                .equalsIgnoreCase("Field")) {
                                            sourceAttributesList
                                                    .add(sourceAttribute
                                                            .getName());
                                            reqAttributesList.add(requElement
                                                    .getName());

                                        } else if ((requElement.getType())
                                                .equalsIgnoreCase("Method")) {
                                            sourceMethodsList
                                                    .add(sourceAttribute
                                                            .getName());
                                            reqMethodsList.add(requElement
                                                    .getName());
                                        }
>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94

                                        sourceAttributeElements
                                                .remove(sourceAttribute);
                                        reqAttributeElements
                                                .remove(requElement);
                                        i--;
                                        j--;
                                        break;
                                    }
                                }
                            }
                        }
                        if (CompareWindow.tree != null
                                && !CompareWindow.tree.isDisposed()) {
                            for (int k = 0; k < sourceAttributesList.size(); k++) {
                                TreeItem subItem = new TreeItem(item,
                                        SWT.NONE);
                                subItem.setText(1,
                                        sourceAttributesList.get(k));
                                subItem.setText(0, reqAttributesList.get(k));
                            }

<<<<<<< HEAD
										sourceAttributeElements
												.remove(sourceAttribute);
										reqAttributeElements
												.remove(requElement);
										i--;
										j--;
										break;
									}
								}
							}
						}
							if (CompareWindow.tree != null
									&& !CompareWindow.tree.isDisposed()) {
								for (int k = 0; k < sourceAttributesList.size(); k++) {
									TreeItem subItem = new TreeItem(item,
											SWT.NONE);
									subItem.setText(1,
											sourceAttributesList.get(k));
									subItem.setText(0, reqAttributesList.get(k));
								}

								for (int k = 0; k < sourceMethodsList.size(); k++) {
									TreeItem subItem = new TreeItem(item,
											SWT.NONE);
									subItem.setText(1, sourceMethodsList.get(k));
									subItem.setText(0, reqMethodsList.get(k));
								}
						
							

								if (sourceAttributeElements.size() > 0
										|| reqAttributeElements.size() > 0) {
									if (sourceAttributeElements.size() > 0) {

										for (ArtefactSubElement model : sourceAttributeElements) {
											TreeItem subItem = new TreeItem(
													item, SWT.NONE);
											subItem.setText(1, model.getName());
											subItem.setForeground(Display
													.getDefault()
													.getSystemColor(
															SWT.COLOR_RED));
										}

									}

									if (reqAttributeElements.size() > 0) {

										for (ArtefactSubElement model : reqAttributeElements) {
											TreeItem subItem = new TreeItem(
													item, SWT.NONE);
											subItem.setText(0, model.getName());
											subItem.setForeground(Display
													.getDefault()
													.getSystemColor(
															SWT.COLOR_RED));
										}

									}
								}
							}
						
						sourceMap.remove(sourceArtefactElement
								.getArtefactElementId());
						reqMap.remove(reqArtefactElement.getArtefactElementId());
						requirementIterator = reqMap.entrySet().iterator();
						break;
					}
				}
			}
		}
		if (sourceMap.size() > 0 || reqMap.size() > 0) {
			requirementIterator = reqMap.entrySet().iterator();
			sourceIterator = sourceMap.entrySet().iterator();

			while (requirementIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = requirementIterator
						.next();
				if (CompareWindow.tree != null
						&& !CompareWindow.shell.isDisposed()) {
					TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
					item.setText(0, artefact.getValue().getName());
					item.setForeground(Display.getDefault().getSystemColor(
							SWT.COLOR_RED));
				}
			}
=======
                            for (int k = 0; k < sourceMethodsList.size(); k++) {
                                TreeItem subItem = new TreeItem(item,
                                        SWT.NONE);
                                subItem.setText(1, sourceMethodsList.get(k));
                                subItem.setText(0, reqMethodsList.get(k));
                            }

                            if (sourceAttributeElements.size() > 0
                                    || reqAttributeElements.size() > 0) {
                                if (sourceAttributeElements.size() > 0) {

                                    for (ArtefactSubElement model : sourceAttributeElements) {
                                        TreeItem subItem = new TreeItem(
                                                item, SWT.NONE);
                                        subItem.setText(1, model.getName());
                                        subItem.setForeground(Display
                                                .getDefault()
                                                .getSystemColor(
                                                        SWT.COLOR_RED));
                                    }

                                }

                                if (reqAttributeElements.size() > 0) {

                                    for (ArtefactSubElement model : reqAttributeElements) {
                                        TreeItem subItem = new TreeItem(
                                                item, SWT.NONE);
                                        subItem.setText(0, model.getName());
                                        subItem.setForeground(Display
                                                .getDefault()
                                                .getSystemColor(
                                                        SWT.COLOR_RED));
                                    }

                                }
                            }
                        }

                        sourceMap.remove(sourceArtefactElement
                                .getArtefactElementId());
                        reqMap.remove(reqArtefactElement.getArtefactElementId());
                        requirementIterator = reqMap.entrySet().iterator();
                        break;
                    }
                }
            }
        }
        if (sourceMap.size() > 0 || reqMap.size() > 0) {
            requirementIterator = reqMap.entrySet().iterator();
            sourceIterator = sourceMap.entrySet().iterator();

            while (requirementIterator.hasNext()) {
                Map.Entry<String, ArtefactElement> artefact = requirementIterator
                        .next();
                if (CompareWindow.tree != null
                        && !CompareWindow.shell.isDisposed()) {
                    TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
                    item.setText(0, artefact.getValue().getName());
                    item.setForeground(Display.getDefault().getSystemColor(
                            SWT.COLOR_RED));
                }
            }
>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94

            while (sourceIterator.hasNext()) {
                Map.Entry<String, ArtefactElement> artefact = sourceIterator
                        .next();

<<<<<<< HEAD
				if (CompareWindow.tree != null
						&& !CompareWindow.shell.isDisposed()) {
					TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
					item.setText(1, artefact.getValue().getName());
					item.setForeground(Display.getDefault().getSystemColor(
							SWT.COLOR_RED));
				}
			}
		}
=======
                if (CompareWindow.tree != null
                        && !CompareWindow.shell.isDisposed()) {
                    TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
                    item.setText(1, artefact.getValue().getName());
                    item.setForeground(Display.getDefault().getSystemColor(
                            SWT.COLOR_RED));
                }
            }
        }
>>>>>>> 05c4f90cacadc3a4e3998fe3986e28b8afe24f94

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

}
