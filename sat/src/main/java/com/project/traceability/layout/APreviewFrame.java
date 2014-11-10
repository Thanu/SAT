package com.project.traceability.layout;

import org.gephi.io.importer.api.*;
import org.gephi.io.importer.api.Container;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.preview.api.*;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import processing.core.PApplet;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public abstract class APreviewFrame
{
    protected Workspace workspace;
    protected PApplet applet;
    protected PreviewModelBuilder modelBuilder = new PreviewModelBuilder();
    protected PreviewController previewController;

    protected APreviewFrame()
    {
        previewController = Lookup.getDefault().lookup(PreviewController.class);

        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);

        pc.newProject();
        workspace = pc.getCurrentWorkspace();
    }

    protected void Import(Container container)
    {
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        importController.process(container, new DefaultProcessor(), workspace);
    }

    protected void AddGraph()
    {
    	// Import file
		ImportController importController = Lookup.getDefault().lookup(
				ImportController.class);
		Container container;
		try {
			File file = new File("C:\\Users\\Thanu\\Documents\\atom-3.gexf");
			container = importController.importFile(file);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		// Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);
//        Container container = Lookup.getDefault().lookup(ContainerFactory.class).newContainer();
//        RandomGraph randomGraph = new RandomGraph();
//        randomGraph.setNumberOfNodes(1);
//        randomGraph.generate(container.getLoader());
//        Import(container);
    }


    protected void Prepare()
    {
        modelBuilder.ApplyModel(previewController);
        previewController.refreshPreview();
        PreviewModel model = previewController.getModel();

        MyRenderer myRenderer = new MyRenderer();
        ArrayList<ManagedRenderer> r =  new ArrayList<ManagedRenderer>(Arrays.asList(model.getManagedRenderers()));

        r.add(new ManagedRenderer(myRenderer, true));

        ManagedRenderer[] c = r.toArray(new ManagedRenderer[r.size()]);

        model.setManagedRenderers(c);

        //New Processing target, get the PApplet
        ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
        applet = target.getApplet();
        applet.init();

        //Refresh the preview and reset the zo
        target.refresh();
        target.resetZoom();
    }

    protected void Display(PApplet applet)
    {
        //Add the applet to a JFrame and display
        JFrame frame = new JFrame("Test Preview");
        frame.setLayout(new BorderLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(applet, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}
