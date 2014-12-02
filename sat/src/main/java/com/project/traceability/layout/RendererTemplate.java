package com.project.traceability.layout;

import org.gephi.graph.api.Node;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PDFTarget;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.api.SVGTarget;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.preview.spi.Renderer;
import org.openide.util.lookup.ServiceProvider;

import processing.core.PGraphics;

//import processing.core.PGraphics;

@ServiceProvider(service = Renderer.class)
public class RendererTemplate implements Renderer, MouseResponsiveRenderer {

	public boolean needsPreviewMouseListener(
			PreviewMouseListener previewMouseListener) {
		return previewMouseListener instanceof MouseListenerTemplate;
	}

	public String getDisplayName() {
		return "Some name";
	}

	public void preProcess(PreviewModel previewModel) {
		// TODO Auto-generated method stub

	}

	public void render(Item item, RenderTarget target,
			PreviewProperties properties) {
		// Retrieve clicked node for the label:
		LabelItem label = (LabelItem) item;
		Node node = label.node;

		// Finally draw your graphics for the node label in each target (or just
		// processing):
		if (target instanceof ProcessingTarget) {
			// Or basic java2d graphics : Graphics g = ((ProcessingTarget)
			// target).getApplet().getGraphics();
			PGraphics g = ((ProcessingTarget) target).getGraphics();

			g.color(0, 0, 0);
			g.fill(0, 0, 0);
			g.ellipse(node.getNodeData().x(), -node.getNodeData().y(), 5, 5);// Note that y axis is inverse for node coordinates
		} else if (target instanceof PDFTarget) {
		} else if (target instanceof SVGTarget) {
		}
	}

	public PreviewProperty[] getProperties() {
		return new PreviewProperty[0];
	}

	public boolean isRendererForitem(Item item, PreviewProperties properties) {
		return item instanceof LabelItem;
	}

	public boolean needsItemBuilder(ItemBuilder itemBuilder,
			PreviewProperties properties) {
		return itemBuilder instanceof ItemBuilderTemplate;
	}
}
