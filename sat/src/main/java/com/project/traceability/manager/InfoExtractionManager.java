package com.project.traceability.manager;

/**
 * @author Gitanjali
 * Nov 6, 2014
 * InfoExtractionManager.java
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.model.RequirementModel;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;

public class InfoExtractionManager {
	private static List<ArtefactElement> expectedClassNames;
	private static List<ArtefactElement> expectedSubClassNames;
	private static List<ArtefactSubElement> expectedAttributeNames;

	private static List<String> classNames;
	private static List<String> functionNames;

	private static List<String> attributeNames;
	private static ArtefactElement artefactElement = null;

	public static List<ArtefactElement> run(
			List<RequirementModel> requirementAretefactElements) {

		expectedClassNames = new ArrayList<ArtefactElement>();
		expectedSubClassNames = new ArrayList<ArtefactElement>();
		for (int i = 0; i < requirementAretefactElements.size(); i++) {
			extactClass(requirementAretefactElements.get(i).getTitle(),
					requirementAretefactElements.get(i).getContent());

			HashSet<String> classSet = new HashSet<String>(classNames);
			System.out.println("Classes : " + classSet);
			HashSet<String> attributeSet = new HashSet<String>(attributeNames);
			System.out.println("Attributes : " + attributeSet);
			HashSet<String> functionSet = new HashSet<String>(functionNames);
			System.out.println("Behaviors : " + functionSet);
		}
		expectedClassNames.addAll(expectedSubClassNames);
		return expectedClassNames;
	}

	public static void extactClass(String title, String content) {

		attributeNames = new ArrayList<String>();
		classNames = new ArrayList<String>();
		functionNames = new ArrayList<String>();
		artefactElement = new ArtefactElement();
		expectedAttributeNames = new ArrayList<ArtefactSubElement>();

		String[] defaultWords = { "database", "record", "system", "company",
				"information", "organization", "detail" };

		if (content.contains("such as")) {

			artefactElement = InfoExtractionManager.getClassName(title); // title
			String[] splitSentence = content.split("such as");

			for (int i = 0; i < defaultWords.length; i++) {
				if (classNames.contains(defaultWords[i])) {
					classNames.remove(defaultWords[i]);
					i--;
				}
			}

			getAttributes(splitSentence[1]);
			artefactElement.setArtefactSubElements(expectedAttributeNames);
			expectedClassNames.add(artefactElement);
			generateBehavior(attributeNames);

		} else {
			functionNames = getBehaviors("do " + title.toLowerCase());
		}
	}

	public static ArrayList<String> getAttributes(String str) {
		attributeNames = new ArrayList<String>();
		AttributeModel attribute = null;
		Pattern p = Pattern.compile("\\((.*?)\\)", Pattern.DOTALL);

		String[] attributeString = str.split(",");
		for (int i = 0; i < attributeString.length; i++) {
			if (attributeString[i].contains("and")) {
				String[] attr = attributeString[i].split("and");
				for (int j = 0; j < attr.length; j++) {
					Matcher m = p.matcher(attr[j].toLowerCase().trim());
					if (m.find()) {
						classNames.add(m.group(1));
						getSubClasses(m.group(1), m.group(0),
								artefactElement.getName());
					} else {
						attribute = new AttributeModel();
						attribute.setSubElementId(UUID.randomUUID().toString());
						attribute.setName(attr[j].toLowerCase().trim());
						expectedAttributeNames.add(attribute);
						attributeNames.add(attr[j].toLowerCase().trim());
					}
				}
			} else {
				Matcher m = p.matcher(attributeString[i].toLowerCase().trim());
				if (m.find()) {
					classNames.add(m.group(1));
					getSubClasses(m.group(1), attributeString[i].replace(m.group(0), ""),
							artefactElement.getName());
				} else {
					attributeNames.add(attributeString[i].toLowerCase().trim());
					attribute = new AttributeModel();
					attribute.setSubElementId(UUID.randomUUID().toString());
					attribute.setName(attributeString[i].toLowerCase().trim());
					expectedAttributeNames.add(attribute);
				}
			}
		}
		generateBehavior(attributeNames);
		return (ArrayList<String>) attributeNames;
	}

	public static void generateBehavior(List<String> attList) {
		for (int i = 0; i < attList.size(); i++) {
			functionNames.add("get" + attList.get(i));
			functionNames.add("set" + attList.get(i));
		}
	}

	public static ArtefactElement getClassName(String str) {

		StanfordCoreNLP pipeline = new StanfordCoreNLP();
		Annotation annotation = new Annotation(str);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation
				.get(CoreAnnotations.SentencesAnnotation.class);

		if (sentences != null && sentences.size() > 0) {
			ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(0);
			Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
			List<Tree> leaves = tree.getLeaves();
			for (int i = 0; i < leaves.size(); i++) {
				Tree leaf = leaves.get(i);
				Tree preLeaf = null;
				Tree preParent = null;
				if (i != 0)
					preLeaf = leaves.get(i - 1);
				Tree parent = leaf.parent(tree);
				if (i != 0)
					preParent = preLeaf.parent(tree);

				if (parent.label().value().equals("IN")) {
					leaves.remove(leaf);
					i--;
				}
				if (i != 0 && preParent.label().value().equals("JJ")
						&& parent.label().value().equals("NN")) {
					artefactElement = new ArtefactElement();
					artefactElement.setName(preLeaf.label().value()
							.toLowerCase()
							+ " " + leaf.label().value().toLowerCase());
					artefactElement.setArtefactElementId(UUID.randomUUID()
							.toString());
					classNames.add(preLeaf.label().value().toLowerCase() + " "
							+ leaf.label().value().toLowerCase());
				} else if (i != 0 && preParent.label().value().equals("NN")
						&& parent.label().value().equals("NN")) {
					artefactElement = new ArtefactElement();
					artefactElement.setName(preLeaf.label().value()
							.toLowerCase()
							+ " " + leaf.label().value().toLowerCase());
					artefactElement.setArtefactElementId(UUID.randomUUID()
							.toString());
					classNames.add(preLeaf.label().value().toLowerCase() + " "
							+ leaf.label().value().toLowerCase());
				} else if (parent.label().value().equals("NN")
						|| parent.label().value().equals("NNP")) {
					artefactElement = new ArtefactElement();
					artefactElement.setName(leaf.label().value().toLowerCase());
					artefactElement.setArtefactElementId(UUID.randomUUID()
							.toString());
					classNames.add(leaf.label().value().toLowerCase());
				}
			}
		}
		return artefactElement;
	}

	public static ArrayList<String> getBehaviors(String str) {

		functionNames = new ArrayList<String>();

		StanfordCoreNLP pipeline = new StanfordCoreNLP();
		Annotation annotation;
		annotation = new Annotation(str);
		pipeline.annotate(annotation);

		System.out.println("The top level annotation");
		System.out.println(annotation.toShorterString());
		List<CoreMap> sentences = annotation
				.get(CoreAnnotations.SentencesAnnotation.class);
		if (sentences != null && sentences.size() > 0) {
			ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(0);
			Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
			List<Tree> leaves = tree.getLeaves();
			for (int i = 0; i < leaves.size(); i++) {
				Tree leaf = leaves.get(i);
				Tree parent = leaf.parent(tree);
				if (parent.label().value().equals("VB")) {
					if (!leaf.label().value().equals("do")) {
						functionNames.add(leaf.label().value().toLowerCase());
					}
				}
			}
		}
		return (ArrayList<String>) functionNames;
	}

	private static boolean checkDefaultWords(String str) {
		boolean isDefault = false;
		String[] defaultWords = { "database", "record", "system", "company",
				"information", "organization", "detail" };
		for (int i = 0; i < defaultWords.length; i++) {
			if (str.equalsIgnoreCase(defaultWords[i]))
				isDefault = true;
		}
		return isDefault;
	}

	private static void getSubClasses(String subClassName,
			String attributeName, String superClassName) {
		ArtefactElement artefact = null;
		ArtefactSubElement attribute = null;
		List<ArtefactSubElement> attributeList = null;
		System.out.println(subClassName + superClassName + attributeName);
		if (expectedSubClassNames.size() == 0) {
			artefact = new ArtefactElement();
			artefact.setName(subClassName);
			artefact.setArtefactElementId(UUID.randomUUID().toString());
			attribute = new ArtefactSubElement();
			attribute.setSubElementId(UUID.randomUUID().toString());
			attribute.setName(attributeName);
			attributeList = new ArrayList<ArtefactSubElement>();
			attributeList.add(attribute);
			artefact.setArtefactSubElements(attributeList);
			expectedSubClassNames.add(artefact);
		} else{
			boolean isExist = false;
			for(int i = 0; i < expectedSubClassNames.size(); i++){
				if(subClassName.equalsIgnoreCase(expectedSubClassNames.get(i).getName())){
					isExist = true;
					artefact = expectedSubClassNames.get(i);
					expectedSubClassNames.remove(i);
					break;
				}				
			}
			if(!isExist){
				artefact = new ArtefactElement();
				artefact.setName(subClassName);
			}
			attribute = new ArtefactSubElement();
			attribute.setSubElementId(UUID.randomUUID().toString());
			attribute.setName(attributeName);
			if(!isExist)
				attributeList = new ArrayList<ArtefactSubElement>();
			else
				attributeList = artefact.getArtefactSubElements();
			attributeList.add(attribute);
			artefact.setArtefactSubElements(attributeList);
			expectedSubClassNames.add(artefact);
		}
	}
}
