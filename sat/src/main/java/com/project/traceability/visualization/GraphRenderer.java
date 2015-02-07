/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import java.awt.Color;
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
import org.openide.util.lookup.ServiceProvider;
import processing.core.PGraphics;

/**
 *
 * @author Thanu
 */
@ServiceProvider(service = Renderer.class)
public class GraphRenderer implements MouseResponsiveRenderer, Renderer {

    @Override
    public boolean needsPreviewMouseListener(PreviewMouseListener pl) {
        //System.out.println(pl instanceof GraphMouseListener);
        return pl instanceof GraphMouseListener;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void preProcess(PreviewModel pm) {
        //System.out.println("preprocess");
    }

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {
        if (target instanceof ProcessingTarget) {
            renderProcessing(item, (ProcessingTarget) target, properties);
        } else if (target instanceof PDFTarget) {
        } else if (target instanceof SVGTarget) {
        }
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties pp) {
        return item.getType().equals(Item.NODE);
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder ib, PreviewProperties pp) {
        return ib instanceof ItemBuilderTemplate;
    }

    private void renderProcessing(Item item, ProcessingTarget target, PreviewProperties properties) {
//        Float x = item.getData(NodeItem.X);
//        Float y = item.getData(NodeItem.Y);
//        Float size = item.getData(NodeItem.SIZE);
//        Color color = item.getData(NodeItem.COLOR);
//        Color borderColor = ((DependantColor) properties
//                .getValue(PreviewProperty.NODE_BORDER_COLOR)).getColor(color);
//        float borderSize = properties
//                .getFloatValue(PreviewProperty.NODE_BORDER_WIDTH);
//        int alpha = (int) ((properties
//                .getFloatValue(PreviewProperty.NODE_OPACITY) / 100f) * 255f);
//        if (alpha > 255) {
//            alpha = 255;
//        }
//
//        // Graphics		
//        PGraphics graphics = target.getGraphics();
//
//        // x = x - size;		
//        // y = y - size;		
//        if (borderSize > 0) {
//            graphics.stroke(borderColor.getRed(), borderColor.getGreen(),
//                    borderColor.getBlue(), alpha);
//            graphics.strokeWeight(borderSize);
//        } else {
//            graphics.noStroke();
//        }
//       // graphics.fill(color.getRed(), color.getGreen(), color.getBlue(), alpha);
//        graphics.ellipse(x, y, size+1.0f, size+1.0f);		
    }
}
