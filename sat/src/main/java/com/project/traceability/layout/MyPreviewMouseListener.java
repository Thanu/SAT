package com.project.traceability.layout;

import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = PreviewMouseListener.class)
public class MyPreviewMouseListener implements PreviewMouseListener{
    @Override
    public void mouseClicked(PreviewMouseEvent event,
                             PreviewProperties properties, Workspace workspace) {
        System.out.println("I'm clicked!!");
    }
    @Override
    public void mousePressed(PreviewMouseEvent event,
                             PreviewProperties properties, Workspace workspace) {}
    @Override
    public void mouseDragged(PreviewMouseEvent event,
                             PreviewProperties properties, Workspace workspace) {}
    @Override
    public void mouseReleased(PreviewMouseEvent event,
                              PreviewProperties properties, Workspace workspace) {}
}