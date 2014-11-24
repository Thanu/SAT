package com.project.traceability.layout;

import org.gephi.preview.api.*;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.preview.spi.Renderer;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=Renderer.class)
public class MyRenderer implements MouseResponsiveRenderer, Renderer {

    public boolean needsPreviewMouseListener(PreviewMouseListener previewMouseListener) {
        return previewMouseListener instanceof MyPreviewMouseListener;
    }

    @Override
    public String getDisplayName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void preProcess(PreviewModel previewModel) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void render(Item item, RenderTarget renderTarget, PreviewProperties previewProperties) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties previewProperties) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder itemBuilder, PreviewProperties previewProperties) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
