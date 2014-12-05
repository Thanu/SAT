package com.project.traceability.db;

import java.awt.Color;

import javax.swing.JOptionPane;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PDFTarget;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.api.SVGTarget;
import org.gephi.preview.plugin.items.NodeItem;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.preview.spi.Renderer;
import org.gephi.preview.types.DependantColor;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import processing.core.PGraphics;

@ServiceProvider(service = MouseResponsiveRenderer.class)
public class RendererTemplate implements MouseResponsiveRenderer, Renderer {

	@Override
	public String getDisplayName() {
		return "Some name";
	}

	@Override
	public void preProcess(PreviewModel previewModel) {
		//System.err.println("preprocess");
	}

	@Override
	public void render(Item item, RenderTarget target,
			PreviewProperties properties) {
		if (target instanceof ProcessingTarget) {
			renderProcessing(item, (ProcessingTarget) target, properties);
		} else if (target instanceof PDFTarget) {
		} else if (target instanceof SVGTarget) {
		}
	}

	public void renderProcessing(Item item, ProcessingTarget target,
			PreviewProperties properties) {
		System.err.println("render");
		Float x = item.getData(NodeItem.X);
		Float y = item.getData(NodeItem.Y);
		Float size = item.getData(NodeItem.SIZE);
		Color color = item.getData(NodeItem.COLOR);
		Color borderColor = ((DependantColor) properties
				.getValue(PreviewProperty.NODE_BORDER_COLOR)).getColor(color);
		float borderSize = properties
				.getFloatValue(PreviewProperty.NODE_BORDER_WIDTH);
		int alpha = (int) ((properties
				.getFloatValue(PreviewProperty.NODE_OPACITY) / 100f) * 255f);
		if (alpha > 255) {
			alpha = 255;
		}

		// Graphics
		PGraphics graphics = target.getGraphics();

		// x = x - size;
		// y = y - size;
		if (borderSize > 0) {
			graphics.stroke(borderColor.getRed(), borderColor.getGreen(),
					borderColor.getBlue(), alpha);
			graphics.strokeWeight(borderSize);
		} else {
			graphics.noStroke();
		}
		graphics.fill(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		graphics.ellipse(x, y, size, size);
	}

	@Override
	public PreviewProperty[] getProperties() {
		return new PreviewProperty[0];
	}

	@Override
	public boolean isRendererForitem(Item item, PreviewProperties properties) {
		// System.out.println(item.getType().equals(Item.NODE));
		return false;//item.getType().equals(Item.NODE);// item instanceof LabelItem;//
	}

	@Override
	public boolean needsItemBuilder(ItemBuilder itemBuilder,
			PreviewProperties properties) {
		System.err.println("ItemBuilder");
		return true;//itemBuilder instanceof ItemBuilderTemplate;
	}

	@Override
	public boolean needsPreviewMouseListener(PreviewMouseListener pl) {
		System.err.println(pl instanceof MouseListenerTemplate);
		return true;// pl instanceof MouseListenerTemplate;
	}
}
