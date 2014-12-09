package com.project.traceability.db;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.io.File;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.labelAdjust.LabelAdjust;
import org.gephi.partition.api.EdgePartition;
import org.gephi.partition.api.NodePartition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.plugin.EdgeColorTransformer;
import org.gephi.partition.plugin.NodeColorTransformer;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantColor;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

import processing.core.PApplet;

import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.common.PropertyFile;

/**
 * Model to add and visualize generated graph file (Traceability link
 * visualization).
 * 
 * @author Thanu
 * 
 */
public class VisualizeGraph {

	public static void main(String[] args) {
		VisualizeGraph preview = new VisualizeGraph();
		preview.script();

	}

	public void script() {
		// Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(
				ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// Import file
		ImportController importController = Lookup.getDefault().lookup(
				ImportController.class);
		Container container;
		try {
			File file = new File(PropertyFile.generatedGexfFilePath);
			container = importController.importFile(file);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		// Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		// Preview configuration
		PreviewController previewController = Lookup.getDefault().lookup(
				PreviewController.class);
		PreviewModel previewModel = previewController.getModel();

		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS,
				Boolean.TRUE);
		previewModel.getProperties().putValue(
				PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.TRUE);
		previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR,
				new DependantOriginalColor(Color.BLACK));
		previewModel.getProperties().putValue(
				PreviewProperty.NODE_BORDER_WIDTH, 1.0f);
		Font f = previewModel.getProperties().getFontValue(
				PreviewProperty.NODE_LABEL_FONT);
		previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT,
				f.deriveFont(Font.BOLD, f.getSize() - 7));
		previewModel.getProperties().putValue(
				PreviewProperty.NODE_BORDER_COLOR,
				new DependantColor(DependantColor.Mode.PARENT));
		previewModel.getProperties()
				.putValue(PreviewProperty.NODE_OPACITY, 100);

		previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED,
				Boolean.TRUE);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR,
				new EdgeColor(EdgeColor.Mode.ORIGINAL));
		previewModel.getProperties()
				.putValue(PreviewProperty.EDGE_OPACITY, 100);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_THICKNESS,
				3.0);
		previewModel.getProperties()
				.putValue(PreviewProperty.EDGE_RADIUS, 0.0f);
		previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS,
				Boolean.TRUE);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_COLOR,
				new DependantOriginalColor(Color.BLACK));
		f = previewModel.getProperties().getFontValue(
				PreviewProperty.EDGE_LABEL_FONT);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_FONT,
				f.deriveFont(Font.BOLD, f.getSize() - 1));
		previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR,
				Color.LIGHT_GRAY);
		previewController.refreshPreview();

		GraphModel graphModel = Lookup.getDefault()
				.lookup(GraphController.class).getModel();
		AttributeModel attributeModel = Lookup.getDefault()
				.lookup(AttributeController.class).getModel();
		RankingController rankingController = Lookup.getDefault().lookup(
				RankingController.class);
		FilterController filterController = Lookup.getDefault().lookup(
				FilterController.class);
		PartitionController partitionController = Lookup.getDefault().lookup(
				PartitionController.class);

		// See if graph is well imported
		DirectedGraph graph = graphModel.getDirectedGraph();

		// Rank size by eccentricity
		Ranking eccentricityRanking = rankingController.getModel().getRanking(
				Ranking.NODE_ELEMENT, GraphDistance.ECCENTRICITY);
		AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController
				.getModel().getTransformer(Ranking.NODE_ELEMENT,
						Transformer.RENDERABLE_SIZE);
		sizeTransformer.setMinSize(30.0f);
		sizeTransformer.setMaxSize(20.0f);
		rankingController.transform(eccentricityRanking, sizeTransformer);

		// Partition with 'type' column, which is in the data
		NodePartition node_partition = (NodePartition) partitionController
				.buildPartition(
						attributeModel.getNodeTable().getColumn("Type"), graph);

		// NodePartitionFilter attributeFilter = new NodePartitionFilter(
		// node_partition);
		// attributeFilter.unselectAll();
		// attributeFilter
		// .addPart(node_partition.getPartFromValue("UMLAttribute"));
		// attributeFilter.addPart(node_partition.getPartFromValue("Field"));
		// Query attribute_query =
		// filterController.createQuery(attributeFilter);
		// GraphView attribute_view = filterController.filter(attribute_query);
		//
		// NodePartitionFilter methodFilter = new NodePartitionFilter(
		// node_partition);
		// methodFilter.unselectAll();
		// methodFilter.addPart(node_partition.getPartFromValue("Method"));
		// methodFilter.addPart(node_partition.getPartFromValue("UMLOperation"));
		// Query method_query = filterController.createQuery(methodFilter);
		// GraphView method_view = filterController.filter(method_query);
		//
		// NodePartitionFilter classFilter = new NodePartitionFilter(
		// node_partition);
		// classFilter.unselectAll();
		// classFilter.addPart(node_partition.getPartFromValue("Class"));
		// methodFilter.addPart(node_partition.getPartFromValue("Functional"));
		// Query class_query = filterController.createQuery(classFilter);
		// GraphView class_view = filterController.filter(class_query);
		// graphModel.setVisibleView(attribute_view);

		// UNIONBuilder unionOperator = new UNIONBuilder();
		// IntersectionOperator intersectionOperator = new
		// IntersectionOperator();
		// Query query3 = filterController.createQuery(intersectionOperator);
		// filterController.setSubQuery(query3, query);
		// filterController.setSubQuery(query3, query2);
		// GraphView view2 = filterController.filter(query3);
		// graphModel.setVisibleView(view2);

		// Partition with 'Neo4j Relationship Type' column, which is in the data
		EdgePartition edge_partition = (EdgePartition) partitionController
				.buildPartition(
						attributeModel.getEdgeTable().getColumn(
								"Neo4j Relationship Type"), graph);
		// EdgePartitionFilter edgeFilter = new
		// EdgePartitionFilter(edge_partition);
		// edgeFilter.unselectAll();
		// edgeFilter.addPart(edge_partition.getPartFromValue("SUB_ELEMENT"));
		// Query edge_query = filterController.createQuery(edgeFilter);
		// GraphView edge_view = filterController.filter(edge_query);
		// graphModel.setVisibleView(edge_view);

		NodeColorTransformer nodeColorTransformer = new NodeColorTransformer();
		nodeColorTransformer.randomizeColors(node_partition);
		partitionController.transform(node_partition, nodeColorTransformer);

		EdgeColorTransformer edgeColorTransformer = new EdgeColorTransformer();
		edgeColorTransformer.randomizeColors(edge_partition);
		partitionController.transform(edge_partition, edgeColorTransformer);

		AutoLayout autoLayout = new AutoLayout(1, TimeUnit.SECONDS);
		autoLayout.setGraphModel(graphModel);
		YifanHuLayout firstLayout = new YifanHuLayout(null,
				new StepDisplacement(1f));
		ForceAtlas2 secondLayout = new ForceAtlas2(null);
		LabelAdjust thirdLayout = new LabelAdjust(null);
		AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout
				.createDynamicProperty("forceAtlas2.adjustSizes.name",
						Boolean.TRUE, 0.0f);// True after 10% of layout time
		AutoLayout.DynamicProperty linLogModeProperty = AutoLayout
				.createDynamicProperty("forceAtlas2.linLogMode.name",
						Boolean.TRUE, 0f);// 500 for the complete period
		AutoLayout.DynamicProperty gravityProperty = AutoLayout
				.createDynamicProperty("forceAtlas2.gravity.name", 5d, 0f);
		AutoLayout.DynamicProperty scallingRatioProperty = AutoLayout
				.createDynamicProperty("forceAtlas2.scalingRatio.name", 20d, 0f);
		AutoLayout.DynamicProperty dissaudeHubsProperty = AutoLayout
				.createDynamicProperty(
						"ForceAtlas2.distributedAttraction.name", Boolean.TRUE,
						0f);
		autoLayout.addLayout(firstLayout, 0.4f);
		autoLayout.addLayout(secondLayout, 0.4f,
				new AutoLayout.DynamicProperty[] { adjustBySizeProperty,
						linLogModeProperty, gravityProperty,
						scallingRatioProperty, dissaudeHubsProperty });
		autoLayout.addLayout(thirdLayout, 0.2f);
		autoLayout.execute();

		// New Processing target, get the PApplet
		ProcessingTarget target = (ProcessingTarget) previewController
				.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		PApplet applet = target.getApplet();
		applet.init();

		try {
			Thread.sleep(100);
		} catch (InterruptedException ex) {
			Exceptions.printStackTrace(ex);
		}
		
		
		// Refresh the preview and reset the zoom
		previewController.render(target);
		target.refresh();
		target.resetZoom();


		 CTabItem tabItem = new CTabItem(HomeGUI.tabFolder, SWT.NONE);
		 tabItem.setText("Graph");
		
		 final Composite composite = new Composite(HomeGUI.tabFolder,
		 SWT.EMBEDDED);
		 composite.setLayout(new GridLayout(1, false));
		 GridData spec = new GridData();
		 spec.horizontalAlignment = GridData.FILL;
		 spec.grabExcessHorizontalSpace = true;
		 spec.verticalAlignment = GridData.FILL;
		 spec.grabExcessVerticalSpace = true;
		 
		 composite.setLayoutData(spec);
		 final Frame frame = SWT_AWT.new_Frame(composite);
		
		 Panel panel = new Panel();
		 
		 panel.add(applet);
		 frame.add(panel);
		 composite.setData(panel);
		 tabItem.setControl(composite);

//		applet.init();
//
//		// Add the applet to a JFrame and display
//		 JFrame frame = new JFrame("Test Preview");
//		 frame.setLayout(new BorderLayout());
//		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		 frame.add(applet, BorderLayout.CENTER);
//		 frame.pack();
//		 frame.setVisible(true);

	}
}
