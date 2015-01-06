package com.project.traceability.db;

import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.common.PropertyFile;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.plugin.partition.PartitionBuilder.EdgePartitionFilter;
import org.gephi.filters.plugin.partition.PartitionBuilder.NodePartitionFilter;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
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
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import processing.core.PApplet;

/**
 * Model to add and visualize generated graph file (Traceability link
 * visualization).
 *
 * @author Thanu
 *
 */
public class VisualizeGraph {

    private String graphType;
    private PreviewController previewController;
    private GraphModel graphModel;

    public PreviewController getPreviewController() {
        return previewController;
    }

    public void setPreviewController(PreviewController previewController) {
        this.previewController = previewController;
    }

    public GraphModel getGraphModel() {
        return graphModel;
    }

    public void setGraphModel(GraphModel graphModel) {
        this.graphModel = graphModel;
    }

    public String getGraphType() {
        return graphType;
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
//        GraphModel graphModel = Lookup.getDefault()
//                .lookup(GraphController.class).getModel();
        AttributeModel attributeModel = Lookup.getDefault()
                .lookup(AttributeController.class).getModel();
//        RankingController rankingController = Lookup.getDefault().lookup(
//                RankingController.class);
        FilterController filterController = Lookup.getDefault().lookup(
                FilterController.class);
        PartitionController partitionController = Lookup.getDefault().lookup(
                PartitionController.class);

        // See if graph is well imported
        DirectedGraph graph = graphModel.getDirectedGraph();

        // Rank size by eccentricity
//		Ranking eccentricityRanking = rankingController.getModel().getRanking(
//				Ranking.NODE_ELEMENT, GraphDistance.ECCENTRICITY);
//		AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController
//				.getModel().getTransformer(Ranking.NODE_ELEMENT,
//						Transformer.RENDERABLE_SIZE);
//		sizeTransformer.setMinSize(30.0f);
//		sizeTransformer.setMaxSize(20.0f);
//		rankingController.transform(eccentricityRanking, sizeTransformer);

        // Partition with 'type' column, which is in the data
        NodePartition node_partition = (NodePartition) partitionController
                .buildPartition(
                attributeModel.getNodeTable().getColumn("Type"), graph);

        // Partition with 'Neo4j Relationship Type' column, which is in the data
        EdgePartition edge_partition = (EdgePartition) partitionController
                .buildPartition(
                attributeModel.getEdgeTable().getColumn(
                "Neo4j Relationship Type"), graph);
        EdgePartitionFilter edgeFilter = new EdgePartitionFilter(
                edge_partition);
        edgeFilter.unselectAll();

        if (graphType.equalsIgnoreCase("Full Graph")) {
        } else if (graphType.equalsIgnoreCase("Source To Target")) {

            edgeFilter.addPart(edge_partition
                    .getPartFromValue("SOURCE_TO_TARGET"));
            Query edge_query = filterController.createQuery(edgeFilter);
            GraphView edge_view = filterController.filter(edge_query);
            graphModel.setVisibleView(edge_view);
        } else if (graphType.equalsIgnoreCase("Sub Elements")) {

            edgeFilter.addPart(edge_partition
                    .getPartFromValue("SUB_ELEMENT"));
            Query edge_query = filterController.createQuery(edgeFilter);
            GraphView edge_view = filterController.filter(edge_query);
            graphModel.setVisibleView(edge_view);
        } else if (graphType.equalsIgnoreCase("Class")) {
            NodePartitionFilter classFilter = new NodePartitionFilter(
                    node_partition);
            classFilter.unselectAll();
            classFilter.addPart(node_partition.getPartFromValue("Class"));
            classFilter.addPart(node_partition.getPartFromValue("Functional"));
            Query class_query = filterController.createQuery(classFilter);
            GraphView class_view = filterController.filter(class_query);
            graphModel.setVisibleView(class_view);
        } else if (graphType.equalsIgnoreCase("Attributes")) {

            NodePartitionFilter attributeFilter = new NodePartitionFilter(
                    node_partition);
            attributeFilter.unselectAll();
            attributeFilter.addPart(node_partition
                    .getPartFromValue("UMLAttribute"));
            attributeFilter.addPart(node_partition.getPartFromValue("Field"));
            Query attribute_query = filterController
                    .createQuery(attributeFilter);
            GraphView attribute_view = filterController.filter(attribute_query);
            graphModel.setVisibleView(attribute_view);
        } else if (graphType.equalsIgnoreCase("Methods")) {
            NodePartitionFilter methodFilter = new NodePartitionFilter(
                    node_partition);
            methodFilter.unselectAll();
            methodFilter.addPart(node_partition.getPartFromValue("Method"));
            methodFilter.addPart(node_partition
                    .getPartFromValue("UMLOperation"));
            Query method_query = filterController.createQuery(methodFilter);
            GraphView method_view = filterController.filter(method_query);
            graphModel.setVisibleView(method_view);
        }

        NodeColorTransformer nodeColorTransformer = new NodeColorTransformer();
        nodeColorTransformer.randomizeColors(node_partition);
        partitionController.transform(node_partition, nodeColorTransformer);

        EdgeColorTransformer edgeColorTransformer = new EdgeColorTransformer();
        edgeColorTransformer.randomizeColors(edge_partition);
        partitionController.transform(edge_partition, edgeColorTransformer);
    }

    public void importFile() {
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
    }

    public void setPreview() {
        // Preview configuration
        previewController = Lookup.getDefault().lookup(
                PreviewController.class);
        PreviewModel previewModel = previewController.getModel();
        previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS,
                Boolean.TRUE);
        previewModel.getProperties().putValue(
                PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR,
                new DependantOriginalColor(Color.BLACK));
        previewModel.getProperties().putValue(
                PreviewProperty.NODE_BORDER_WIDTH, 0.5f);
        Font f = previewModel.getProperties().getFontValue(
                PreviewProperty.NODE_LABEL_FONT);
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT,
                f.deriveFont(Font.BOLD, f.getSize() - 6));
        previewModel.getProperties().putValue(
                PreviewProperty.NODE_BORDER_COLOR,
                new DependantColor(DependantColor.Mode.PARENT));
        previewModel.getProperties()
                .putValue(PreviewProperty.NODE_OPACITY, 100);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED,
                Boolean.FALSE);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR,
                new EdgeColor(EdgeColor.Mode.ORIGINAL));
        previewModel.getProperties()
                .putValue(PreviewProperty.EDGE_OPACITY, 100);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_THICKNESS,
                2.0);
        previewModel.getProperties()
                .putValue(PreviewProperty.EDGE_RADIUS, 0.9f);
        previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS,
                Boolean.TRUE);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_COLOR,
                new DependantOriginalColor(Color.BLACK));
        f = previewModel.getProperties().getFontValue(
                PreviewProperty.EDGE_LABEL_FONT);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_FONT,
                f.deriveFont(Font.BOLD, f.getSize() - 3));
        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR,
                Color.LIGHT_GRAY);
        previewModel.getProperties().putValue("GraphType", graphType);
        //previewModel.getProperties().putValue("Preview",previewController);
        previewController.refreshPreview();
    }

    public void setLayout() {
//        GraphModel graphModel = Lookup.getDefault()
//                .lookup(GraphController.class).getModel();
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
                new AutoLayout.DynamicProperty[]{adjustBySizeProperty,
                    linLogModeProperty, gravityProperty,
                    scallingRatioProperty, dissaudeHubsProperty});
        autoLayout.addLayout(thirdLayout, 0.2f);
        autoLayout.execute();
    }

    public void setGraph(GraphModel model, String graphType) {
        setGraphModel(model);
        setGraphType(graphType);
    }

    public void setGraph(GraphModel model) {
        AttributeModel attributeModel = Lookup.getDefault()
                .lookup(AttributeController.class).getModel();
        PartitionController partitionController = Lookup.getDefault().lookup(
                PartitionController.class);
        setGraphModel(model);
        // See if graph is well imported
        DirectedGraph graph = graphModel.getDirectedGraph();
        // Partition with 'type' column, which is in the data
        NodePartition node_partition = (NodePartition) partitionController
                .buildPartition(
                attributeModel.getNodeTable().getColumn("Type"), graph);

        // Partition with 'Neo4j Relationship Type' column, which is in the data
        EdgePartition edge_partition = (EdgePartition) partitionController
                .buildPartition(
                attributeModel.getEdgeTable().getColumn(
                "Neo4j Relationship Type"), graph);
        //setGraphModel(model);

        NodeColorTransformer nodeColorTransformer = new NodeColorTransformer();
        nodeColorTransformer.randomizeColors(node_partition);
        partitionController.transform(node_partition, nodeColorTransformer);

        EdgeColorTransformer edgeColorTransformer = new EdgeColorTransformer();
        edgeColorTransformer.randomizeColors(edge_partition);
        partitionController.transform(edge_partition, edgeColorTransformer);

    }

    public Button addButton() {
        Button back = new Button("Go Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisualizeGraph preview = new VisualizeGraph();
                preview.importFile();
                GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();
                preview.setGraph(model,graphType);
                preview.showGraph();
            }
        });

        return back;
    }

    public void showGraph() {
        setPreview();
        setLayout();
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
        tabItem.setText(PropertyFile.projectName + "-" + PropertyFile.graphType + " View");
        final Composite composite = new Composite(HomeGUI.tabFolder,
                SWT.EMBEDDED);
        composite.setLayout(new GridLayout(1, false));
        GridData spec = new GridData();
        spec.horizontalAlignment = GridData.FILL;
        spec.grabExcessHorizontalSpace = true;
        spec.verticalAlignment = GridData.FILL;
        spec.grabExcessVerticalSpace = true;
        composite.setLayoutData(spec);
        
        
    }
    public void addPanel(PApplet applet,Composite composite, CTabItem tabItem){
        final Frame frame = SWT_AWT.new_Frame(composite);
        Panel panel = new Panel();
        panel.add(applet);
        frame.add(panel);
        composite.setData(panel);
        tabItem.setControl(composite);
    }
    
    public void addPanel(PApplet applet,Composite composite, CTabItem tabItem, Button btn){
        final Frame frame = SWT_AWT.new_Frame(composite);
        Panel panel = new Panel();
        panel.add(applet);
        panel.add(btn);
        frame.add(panel);
        composite.setData(panel);
        tabItem.setControl(composite);
    }

//        JFrame frame = new JFrame("Test Preview");
//        frame.setLayout(new BorderLayout());
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(applet, BorderLayout.CENTER);
//        frame.pack();
//        frame.setVisible(true);
    
    public static void main(String[] args) {
        VisualizeGraph preview = new VisualizeGraph();
        preview.importFile();
        GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();
        preview.setGraph(model, "Class");
        preview.showGraph();
        preview.addPanel(null, null, null);
    }
}
