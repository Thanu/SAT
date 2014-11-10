package com.project.traceability.layout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PreviewJFrame extends APreviewFrame {

    public PreviewJFrame()
    {
        super();
        layoutUpdaterFct = new LayoutUpdaterFct(this);
    }

    private LayoutUpdaterFct layoutUpdaterFct;

    public void Update()
    {
        previewController.refreshPreview();
        applet.redraw();
    }

    public void script() {
        AddGraph();
        Prepare();
        Display(applet);

//        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
//        scheduler.scheduleAtFixedRate(layoutUpdaterFct.createLayoutRunnable(), 0, 100, TimeUnit.MICROSECONDS);
//        scheduler.scheduleAtFixedRate(layoutUpdaterFct.fillGraphRunnable(), 0, 5000, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        PreviewJFrame previewJFrame = new PreviewJFrame();
        previewJFrame.script();
    }
}
